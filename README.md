## 1. 个人技术学习笔记

记录工作和学习中遇到的一些算法思想、学习未知技术的一些demo等，方便后续复习和参考。善用工具挺重要的，利用ChatGPT可以学得很快，当然他给的回答需要甄别，有部分是错误的，还是需要有自己的思路，然后利用他强大的搜索能力，辅助自己去学习。


## 2. 数据结构和算法

记录我写算法的一些习惯（平时从别人那里或网上看到的，然后自己实践并总结）：

- 在学习算法的过程中，重点是思路，代码是对思路的一个实现，实际上可能会做一些优化和压缩。如果想要从别人写的代码中反推出它的思路，有一些题目往往是很难的。通过适当添加注释可以在一定程度上解决反推算法思路的问题。
- 练习算法题目时，可以忽略一些无关轻重的细节，例如：
  -  java 中String 按索引获取和设置某个字符时，需要用 str.charAt(i) 和 str.setCharAt(i, ch) 这种挺繁琐的，可以直接转化成字符数组，简化写法。这种适合自己学习，在面试中肯定要征询面试官同意才行，毕竟使用了额外的 O(n) 的时间复杂度。
  - 一些在线编程，给了框架代码，传入的参数很长，那么可以适当缩短成单个字母，这样更能反应算法的本质，不会被长的变量名所干扰。
  - 代码规范问题，例如 if 语句只有一条语句且很短，我习惯放在一行，代码更短一点。【实际开发当然要遵从规范，IDE有代码规范检查插件，保存时自动格式化即可】

- 同一个算法题目，可能有很多种思路，有的思路特别容易懂，也容易描述，有的则很晦涩，对于 LeetCode 上的题目，可以多看看评论，吸收别人优秀的思路。特别是自己的思路不好想，或者实现比较长且丑陋，可以看看别人的分析。
- 对于算法实现上来说，有的实现方式更不容易出错，例如反向双指针的写法、二分查找的写法、快速排序的partition函数的写法等等，养成自己的一个习惯很重要，不容易出错。


笔记列表：

- [使用双指针算法解决两个 Leetcode 难题](algorithm/01-two-pointers-problems.md)  【思路：双指针】
- [从多个递增的生成序列中找第n个数](algorithm/02-find-n-elem-from-sequence.md) 【思路：双指针合并有序数组】【去重技巧】
- [我的二分查找模板](algorithm/03-binary-search-template.md)【二分总结】
- [旋转数组的二分搜索](algorithm/04-rotated-array-algorithm.md) 【二分应用】
- [巧妙的二进制思想](algorithm/05-good-binary-bit-trick.md) 【二进制思想】
- [容易迷糊的区间问题](algorithm/06-interval-related-problem.md) 【区间，排序，插入】
- [排列、组合、子集相关题目](algorithm/07-排列-组合-子集相关题目.md)
- [思路简单但实现复杂的题目汇总](algorithm/08-思路简单但实现复杂的题目汇总.md)
- [系列题-相互关联](algorithm/09-系列题-相互关联.md)
- [滑动窗口算法总结](algorithm/10-sliding-window-pattern.md)
- [动态规划算法总结](algorithm/11-dp-pattern.md)
- [回溯算法总结](algorithm/12-backtrack-pattern.md)

## 3. 实践 Demo

笔记列表：

- [利用 Redis 实现分布式锁](demo/P01_RedisDistributedLock/README.md) 【setnx, Redission, Redlock】
- [演示HashMap 1.8 并发安全问题](demo/01-HashMap-concurrent-issue.md)【HashMap 1.8, 并发安全问题】
- [key-value-app: 练习NodeJs+MongoDB+Docker/Compose 开发部署 Restful API](demo/P02_key-value-app/readme.md)
- [ES6 New Features](demo/P03_ES6-new-feature/readme.md) - master it with demos
- [GraphQL 初步探索](demo/P05_GraphQL/readme.md)
- [利用 WebSocket 实现公共聊天室](demo/P07_Websocket/readme.md) 【演示 WebSocket 协议的使用方法，nodejs 的 ws 库】
- [redis+lua 脚本实现原子操作](demo/P08_redis_lua/DemoRedisLuaScript/readme.md) 【演示 java 程序怎么访问 redis 去执行 lua 脚本扣减库存】

设计模式系列：
- [Java 实现单例模式的几种方式](demo/P04_Singleton/readme.md)
- [观察者模式](demo/P11_ObserverDesignPattern/readme.md)









