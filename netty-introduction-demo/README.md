# netty-introduction-demo  
Netty的入门实例

构建并运行第一个Netty的客户端和服务器
采用ECHO协议，也就是客户端请求什么，服务器就返回什么。

ChannelOption.SO_BACKLOG:   
服务器的TCP内核维护两个队列A和B  
客户端向服务端请求connect时, 发送SYN(第一次握手)  
服务端收到SYN后, 向客户端发送SYN ACK(第二次握手),  TCP内核将连接放入队列A  
客户端收到后向服务端发送ACK(第三次握手),  TCP内核将连接从A->B, accept返回, 连接完成  
A/B队列的长度和即为BACKLOG, 当accept速度跟不上（也就是同时握手过多）, A/B队列使得BACKLOG满了, 客户端连接就会被TCP内核拒绝  
可以调大SO_BACKLOG缓解这一现象.默认值为50.  





