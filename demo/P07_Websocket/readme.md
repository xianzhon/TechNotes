# WebSocket Demo

## 需求

使用 WebSocket 实现一个在线的聊天室的功能，用户可以创建聊天室，在同一个聊天室的人可以看到彼此的聊天信息。

## 代码实现

### websocket server:

依赖安装：`npm install ws`

使用 nodejs 很方便实现，利用 ws 库创建一个 server 服务，监听在 8080 端口，如下代码：
```js
const WebSocket = require('ws');
const wss = new WebSocket.Server({ port: 8080 });
```

绑定 connection 事件 和 client 的 message /  close事件。
使用一个 Map  of Set 来保存每个房间里面的 client （socket）信息，
- 每当一个新的 client 加入，则放到对应房间的 Set 里面。
- 每当 client 退出了，则从 Set 中删除。
- 每当用户发送消息，则群发给同一房间的其他 client（要排除它自己）
### Demo client

依赖安装：`npx wscat`

```bash
# 分别创建几个 shell terminal，然后执行相同的命令：

npx wscat -c ws://localhost:8080/?room=room1
npx wscat -c ws://localhost:8080/?room=room1
npx wscat -c ws://localhost:8080/?room=room1
npx wscat -c ws://localhost:8080/?room=room2

# 演示在 room1 里面聊天，用户发的内容在其他 client 那里都能看到，在 room2 的用户窗口里看不到。
```


Reference:
- [Design Google Docs - System Design Interview - YouTube](https://www.youtube.com/watch?v=9JKBlkwg0yM)  google docs的协作用的是quic协议，但本质上用 WebSocket 协议也能实现。