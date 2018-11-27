# cordova-chat-plugin
Cordova聊天插件（基于netty，适用于基于cordova的web项目）
# 什么是Cordova？
Cordova提供了一组设备相关的API，通过这组API，移动应用能够以JavaScript访问原生的设备功能，如摄像头、麦克风等。
Cordova还提供了一组统一的JavaScript类库，以及为这些类库所用的设备相关的原生后台代码。
Cordova支持如下移动操作系统：iOS, Android,ubuntu phone os, Blackberry, Windows Phone, Palm WebOS, Bada 和 Symbian。（这些是百度百科上的内容）
##### 个人理解：Cordova为web应用提供了调用系统硬件的中间层，而cordova plugin正是这些功能的具体实现者。
#### cordova调用硬件的方法是调用相关平台的开发语言，所以也可以说cordova提供了一座连通平台原生开发语言和web(html,js,css)开发语言之间的桥梁。
# 该插件功能
该插件是基于java语言提供了建立长连接的方法
# 为什么不用websocket？
相对于websocket使用netty的nio更接近安卓原生开发、定制度更高,后端服务无缝接轨(后端使用netty的情况下)
# 怎么在web应用中使用？
### 1.安装nodejs<a href="https://nodejs.org/en/">：请参照node官网完成安装</a>
### 2.安装cordova：npm install -g cordova
### 3.安装ionic本例上
