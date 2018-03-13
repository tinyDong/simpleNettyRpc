"# simpleNettyRpc" 
###一个简单的Rpc模型

1.获取接口所有信息

2.客户端链接到服务端

3.客户端发送编码接口信息并且发送到服务端,阻塞等待结果返回

4.服务端解码信息,根据接口信息查找实现类并且调用

5.将调用结果封装成信息并且编码后发送到客户端

6.客户端拿到结果后解码,进行处理

###使用方法

先启动server

在启动client即可

###技术点

netty作为网络传输框架

Future获取结果

对象序列化反序列化

对象反射

java动态代理

###common提供了公共接口

RPCBase是核心部分

coder负责message和Object进行转换

connecter 负责网络链接

dto 网络传输对象的统一封装

proxy jdk原生动态代理

serialization 负责序列化
