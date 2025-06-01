const WebSocket = require('ws');
const wss = new WebSocket.Server({ port: 8080 });

// roomId -> Set of WebSocket connections
const rooms = new Map();

wss.on('connection', (socket, req) => {
    let roomId = 'lobby';
    
    try {
        const url = new URL(req.url, 'http://localhost');
        roomId = url.searchParams.get('room') || 'lobby';
    } catch (e) {
        console.error('Error parsing URL:', e.message);
    }

    // Add socket to the room
    if (!rooms.has(roomId)) {
        rooms.set(roomId, new Set());
    }
    rooms.get(roomId).add(socket);

    console.log(`Client joined room: ${roomId}`);

    socket.on('message', (data) => {
        // broadcast msg to everyone else in the same room
        const room = rooms.get(roomId);
        if (room) {
            for (const client of room) {
                if (client !== socket && client.readyState === WebSocket.OPEN) {
                    client.send(data);
                }
            }
        }
    });

    socket.on('error', (error) => {
        console.error('WebSocket error:', error.message);
    });

    socket.on('close', () => {
        const room = rooms.get(roomId);
        if (room) {
            room.delete(socket);
            if (room.size === 0) {
                rooms.delete(roomId);
                console.log(`Room ${roomId} deleted (no more clients)`);
            }
        }
    });
});

console.log('WebSocket server running on ws://localhost:8080');

// Then connect from terminal: 
//      npx wscat -c ws://localhost:8080/?room=room1
