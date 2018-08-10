# netty-heartbeat-detection-demo

使用netty进行服务器和客户端的心跳检测  
或者是主服务器和从服务器之间的心跳检测，让主服务器知道从服务器的状态    
客户端每隔5-10s给服务器进行发送心跳包  
通过netty和定时任务实现    
使用Sigar工具获取服务器的信息，cpu，内存等  




