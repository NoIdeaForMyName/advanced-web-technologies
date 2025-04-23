import { Server } from "socket.io";

const io = new Server(3000, {
    cors: {
        origin: "http://127.0.0.1:5500",
        methods: ["GET", "POST"]
      }
});

io.on("connection", (socket) => {
    socket.emit("hello", "world");

    socket.on("howdy", (arg) => {
        console.log(arg);
    });
});
