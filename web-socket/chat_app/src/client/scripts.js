let nickname = prompt("Enter your nickname:", "User" + Math.floor(Math.random() * 1000));
nickname = nickname == null || nickname == "" ? "anonymous" : nickname;
document.getElementById('username').textContent = nickname;

let currentRoom = 'general';
let replyToMessage = null; // Przechowuje wiadomość, na którą odpowiadamy
var socket = io();

var messages = document.getElementById('messages');
var typingInfo = document.getElementById('typing-info');
var form = document.getElementById('form');
var input = document.getElementById('input');
var fileUpload = document.getElementById('myFile');
var roomList = document.getElementById('room-list');
var userList = document.getElementById('user-list');
var newRoomInput = document.getElementById('new-room-input');
var newRoomButton = document.getElementById('new-room-button');
var replyPreview = document.getElementById('reply-preview');
var replyPreviewText = document.getElementById('reply-preview-text');
var cancelReplyButton = document.getElementById('cancel-reply');

let currentFile;
let typingInfosTimeoutData = {};
const TYPING_INFO_TIMEOUT = 2000; //ms

// Dołącz do domyślnego pokoju
socket.emit('join', { nickname, room: currentRoom });

// SOCKET INPUT LISTENERS...
socket.on('chat message', (msg) => {
  if (msg.room === currentRoom) {
    addMessageItemToHTML(msg, msg.author === nickname);
    if (typingInfosTimeoutData[msg.author]) {
      const [info, timeoutId] = typingInfosTimeoutData[msg.author];
      removeTypingInfo(msg.author, info);
      clearTimeout(timeoutId);
    }
  }
});

socket.on('typing', (typingNickname) => {
  // Pokazuj informację tylko jeśli to nie my piszemy
  if (typingNickname !== nickname) {
    showTypingInfoHTML(typingNickname);
  }
});

socket.on('user joined', (nickname) => {
  addNotification(`${nickname} joined the room`);
});

socket.on('user left', (nickname) => {
  addNotification(`${nickname} left the room`);
});

socket.on('room users', (users) => {
  updateUserList(users);
});

socket.on('room list', (rooms) => {
  updateRoomList(rooms);
});

form.addEventListener('submit', function(e) {
  e.preventDefault();
  if ((input.value && input.value.trim() !== '') || currentFile) {
    const msg = {
      'author': nickname,
      'content': input.value,
      'image': currentFile ? 
      {
        'data': currentFile.data,
        'type': currentFile.type
      } : null,
      'timestamp': new Date(),
      'room': currentRoom,
      'replyTo': replyToMessage ? {
        'id': replyToMessage.id,
        'author': replyToMessage.author,
        'content': replyToMessage.content
      } : null
    };
    socket.emit('chat message', msg);
    input.value = '';
    currentFile = null;
    fileUpload.value = '';
    cancelReply();
  }
});

input.addEventListener('input', (event) => {
  socket.emit('typing', nickname);
});

fileUpload.addEventListener('change', (event) => {
  const file = event.target.files[0];
  if (!file) return;

  if (!file.type.match('image.*')) {
    alert("Please upload an image file");
    event.target.value = '';
    currentFile = null;
    return;
  }
  const reader = new FileReader();
  
  reader.onload = (e) => {
    currentFile = {
      'data': e.target.result,
      'type': file.type
    };
  };

  reader.onerror = () => {
    alert("Error reading file");
    event.target.value = '';
    currentFile = null;
  };

  reader.readAsArrayBuffer(file);
});

newRoomButton.addEventListener('click', () => {
  const roomName = newRoomInput.value.trim();
  if (roomName && roomName !== currentRoom) {
    joinRoom(roomName);
    newRoomInput.value = '';
  }
});

cancelReplyButton.addEventListener('click', cancelReply);

function joinRoom(room) {
  socket.emit('join', { nickname, room });
  currentRoom = room;
  messages.innerHTML = '';
  addNotification(`You joined ${room}`);
  cancelReply();
}

function updateRoomList(rooms) {
  roomList.innerHTML = '';
  rooms.forEach(room => {
    const li = document.createElement('li');
    li.textContent = room;
    if (room === currentRoom) {
      li.classList.add('active');
    }
    li.addEventListener('click', () => {
      if (room !== currentRoom) {
        joinRoom(room);
      }
    });
    roomList.appendChild(li);
  });
}

function updateUserList(users) {
  userList.innerHTML = '';
  users.forEach(user => {
    const li = document.createElement('li');
    li.textContent = user;
    userList.appendChild(li);
  });
}

function addMessageItemToHTML(msg, author=false) {
  const item = createMessageItem(msg);
  messages.appendChild(item);
  messages.scrollTo(0, messages.scrollHeight);
  if (author) item.classList.add('author');
  
  // Dodaj unikalne ID do wiadomości
  const messageId = 'msg-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9);
  item.id = messageId;
  msg.id = messageId; // Dodaj ID do obiektu wiadomości
  
  // Obsługa kliknięcia na wiadomość
  item.addEventListener('click', () => {
    // Usuń podświetlenie z poprzednio wybranej wiadomości
    const previouslySelected = document.querySelector('.message.selected');
    if (previouslySelected) {
      previouslySelected.classList.remove('selected');
    }
    
    // Podświetl wybraną wiadomość
    item.classList.add('selected');
    
    // Ustaw jako wiadomość do odpowiedzi
    replyToMessage = msg;
    
    // Pokaż podgląd odpowiedzi
    replyPreviewText.textContent = msg.content.length > 12 
      ? msg.content.substring(0, 12) + '...' 
      : msg.content;
    replyPreview.classList.remove('hidden');
  });
}

function createMessageItem(msg) {
  const item = document.createElement('li');
  item.className = 'message';

  // Dodaj informację o odpowiedzi jeśli istnieje
  if (msg.replyTo) {
    const replyInfo = document.createElement('div');
    replyInfo.className = 'reply-info';
    replyInfo.textContent = `Replying to ${msg.replyTo.author}: ${msg.replyTo.content.length > 10 ? msg.replyTo.content.substring(0, 10) + '...' : msg.replyTo.content}`;
    item.appendChild(replyInfo);
  }

  const author = document.createElement('div');
  author.className = 'author';
  author.textContent = msg.author;
  item.appendChild(author);

  const content = document.createElement('div');
  content.className = 'content';
  content.textContent = msg.content;
  item.appendChild(content);

  if (msg.image) {
    const blob = new Blob([msg.image.data], { type: msg.image.type });
    const imgURL = URL.createObjectURL(blob);

    const img = document.createElement('img');
    img.src = imgURL;
    img.alt = 'attachment';
    img.className = 'image';
    
    item.append(img);
  }

  const date = document.createElement('div');
  date.className = 'timestamp';
  date.textContent = new Date(msg.timestamp).toLocaleTimeString();
  item.appendChild(date);

  return item;
}

function showTypingInfoHTML(nickname) {
  let currentInfo;
  
  if (typingInfosTimeoutData[nickname]) {
    let currentTimeoutId;
    [currentInfo, currentTimeoutId] = typingInfosTimeoutData[nickname];
    clearTimeout(currentTimeoutId);
  }
  else {
    currentInfo = document.createElement('li');
    currentInfo.textContent = `${nickname} is typing...`;
    typingInfo.appendChild(currentInfo);
  }
  
  typingInfosTimeoutData[nickname] = [currentInfo, setTimeout(() => removeTypingInfo(nickname, currentInfo), TYPING_INFO_TIMEOUT)];
}

function removeTypingInfo(nickname, info) {
  info.remove();
  typingInfosTimeoutData[nickname] = null;
}

function addNotification(text) {
  const notification = document.createElement('li');
  notification.className = 'notification';
  notification.textContent = text;
  messages.appendChild(notification);
  messages.scrollTo(0, messages.scrollHeight);
}

function cancelReply() {
  replyToMessage = null;
  replyPreview.classList.add('hidden');
  const selected = document.querySelector('.message.selected');
  if (selected) {
    selected.classList.remove('selected');
  }
}

// Obsługa emoji pickera
const picker = document.getElementById('emoji-picker');
const emojiButton = document.getElementById('emoji-button');

emojiButton.addEventListener('click', () => {
  picker.classList.toggle('hidden');
});

picker.addEventListener('emoji-click', event => {
  input.value += event.detail.unicode;
  picker.classList.add('hidden');
});

// Zamknij picker przy kliknięciu gdzie indziej
document.addEventListener('click', (e) => {
  if (!picker.contains(e.target) && e.target !== emojiButton) {
    picker.classList.add('hidden');
  }
});