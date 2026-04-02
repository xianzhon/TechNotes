## 学习和拆解开源项目

参考： https://www.bilibili.com/video/BV1ScPkz4EPy

- deepwiki: 只需要把github的网址中github替换为 deepwiki 即可。
- imersive translate 可以翻译整个网页
- claude: 可以让它制作课程大纲，每一章节内容，保存文件，用vs code打开

生成大纲的提示词：
```
制作一个课程大纲，能够用大概30节课的内容覆盖所有知识点。
注意：课程内容要能够循序渐进，内容保存到Tutorial 文件夹中。
```
然后，逐章填充内容：开始写第1课的内容. @CLAUDE.md 文件 （只需要引入一次）

CLAUDE.md 文件内容： `我正在学习这个项目。你制作的所有教程文件，都保存到 Tutorial 文件夹中.`
