const express = require('express');
const app = express();
const http = require('http');
const server = http.createServer(app);
const { Server } = require("socket.io");
const io = new Server(server);

app.get('/', (req, res) => {
  res.sendFile(__dirname + '/client.html');
});

// Przechowujemy informacje o pokojach i użytkownikach
const rooms = new Map();
rooms.set('general', new Set()); // Domyślny pokój

io.on('connection', (socket) => {
  let currentUser = { nickname: null, room: null };

  socket.on('join', ({ nickname, room }) => {
    // Jeśli pokój nie istnieje, utwórz go
    if (!rooms.has(room)) {
      rooms.set(room, new Set());
    }

    // Opuść poprzedni pokój jeśli był
    if (currentUser.room) {
      leaveRoom(currentUser.room, currentUser.nickname);
    }

    // Dołącz do nowego pokoju
    currentUser = { nickname, room };
    rooms.get(room).add(nickname);
    socket.join(room);

    // Powiadom innych w pokoju
    socket.to(room).emit('user joined', nickname);
    io.to(room).emit('room users', Array.from(rooms.get(room)));
    
    // Wyślij aktualną listę pokoi do wszystkich
    io.emit('room list', Array.from(rooms.keys()));
    
    console.log(`${nickname} joined room ${room}`);
  });

  socket.on('disconnect', () => {
    if (currentUser.room && currentUser.nickname) {
      leaveRoom(currentUser.room, currentUser.nickname);
    }
  });

  socket.on('chat message', (msg) => {
    if (currentUser.room) {
      msg.room = currentUser.room;
      io.to(currentUser.room).emit('chat message', msg);
    }
  });

  socket.on('typing', () => {
    if (currentUser.room && currentUser.nickname) {
      io.to(currentUser.room).emit('typing', currentUser.nickname);
    }
  });

  function leaveRoom(room, nickname) {
    socket.leave(room);
    rooms.get(room).delete(nickname);
    
    // Powiadom innych w pokoju
    io.to(room).emit('user left', nickname);
    io.to(room).emit('room users', Array.from(rooms.get(room)));
    
    // Jeśli pokój jest pusty, usuń go (oprócz general)
    if (rooms.get(room).size === 0 && room !== 'general') {
      rooms.delete(room);
    }
    
    // Wyślij aktualną listę pokoi do wszystkich
    io.emit('room list', Array.from(rooms.keys()));
    
    console.log(`${nickname} left room ${room}`);
  }
});

server.listen(3000, () => {
  console.log('Listening on http://localhost:3000 ...');
});