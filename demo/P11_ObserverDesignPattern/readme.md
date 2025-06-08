# 观察者设计模式 / 发布订阅

- Observer / publisher-subscribe design pattern.
- 视频教程：[【设计模式实战】观察者模式_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1QXEnzGEg6?spm_id_from=333.788.videopod.sections&vd_source=3b4e5325419175ea5ebf5c16e5e29278)  （非常好的视频，干货满满，本篇笔记是根据视频整理且做了一些修改得到的）

## 通过 Demo 学习

### v1 基础需求：设计一个简单的观察者模式

有一个天气监测站，它每隔 3s 会得到一个监测数据（晴天、雨天）。有一个 User 类，它的实例代表某个人根据天气的信息做出一些计划（晴天打篮球，雨天在家打游戏）。

![代码 1](ObserverV1.java)
Sample output:
```
Alice 去打篮球
Ross will go hiking
Alice 去打篮球
Ross will go hiking
Alice 在家打游戏
Alice 去打篮球
Ross will go hiking
```
### V2: 引入TVStation专门负责消息订阅和发送
目的：解耦消息的生成者（WeatherStation）和消息的订阅与发布（TVStation）。

![ObserverV2](ObserverV2.java)

Sample output:
```
Alice 去打篮球
Ross will go hiking
Alice 去打篮球
Ross will go hiking
Alice 去打篮球
Ross will go hiking
Alice 去打篮球
Ross will go hiking
Alice 在家打游戏
```

### V3: 支持订阅新闻类的消息

![ObserverV3](ObserverV3.java)
Sample output:
```
Alice 去打篮球
Ross will go hiking
Alice received News at Sun Jun 08 19:42:56 CST 2025
Ross received News at Sun Jun 08 19:42:56 CST 2025
Alice 在家打游戏
Alice 在家打游戏
Alice received News at Sun Jun 08 19:43:02 CST 2025
Ross received News at Sun Jun 08 19:43:02 CST 2025
Alice 去打篮球
Ross will go hiking
Alice 在家打游戏
Alice received News at Sun Jun 08 19:43:08 CST 2025
Ross received News at Sun Jun 08 19:43:08 CST 2025
```

### V4: 每个 User 可以自由选择订阅什么消息
![ObserverV4](ObserverV4.java)
Sample output:
```
Alice 去打篮球
Ross will go hiking
Alice received News at Sun Jun 08 19:41:55 CST 2025
Alice 在家打游戏
Ross will do some coding tasks at home
Alice 在家打游戏
Ross will do some coding tasks at home
Alice received News at Sun Jun 08 19:42:01 CST 2025
Alice 在家打游戏
Ross will do some coding tasks at home
Alice 在家打游戏
Ross will do some coding tasks at home
Alice received News at Sun Jun 08 19:42:07 CST 2025
```
