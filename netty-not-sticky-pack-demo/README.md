# netty-not-sticky-pack-demo

## 解决tcp传输数据，粘包问题  
比较主流的解决方法由如下几种：  
1、消息定长，报文大小固定长度，例如每个报文的长度固定为200字节，如果不够空位补空格；  
2、包尾添加特殊分隔符，例如每条报文结束都添加回车换行符（例如FTP协议）或者指定特殊字符作为报文分隔符，接收方通过特殊分隔符切分报文区分；  
3、将消息分为消息头和消息体，消息头中包含表示信息的总长度（或者消息体长度）的字段；  
4、自定义更复杂的应用层协议。  

## Netty粘包和拆包解决方案
Netty提供了多个解码器，可以进行分包的操作，分别是：  
LineBasedFrameDecoder  
DelimiterBasedFrameDecoder（添加特殊分隔符报文来分包）  
FixedLengthFrameDecoder（使用定长的报文来分包）  
LengthFieldBasedFrameDecoder  

## LengthFieldBasedFrameDecoder
本实例使用LengthFieldBasedFrameDecoder屏蔽TCP底层的拆包和粘包问题  
使用对象进行传输  
LengthFieldBasedFrameDecoder的构造函数：  
````java
public class LengthFieldBasedFrameDecoder extends ByteToMessageDecoder {
    //...
  public LengthFieldBasedFrameDecoder(ByteOrder byteOrder, 
                                    int maxFrameLength, 
                                    int lengthFieldOffset, 
                                    int lengthFieldLength, 
                                    int lengthAdjustment, 
                                    int initialBytesToStrip, 
                                    boolean failFast) {
   }
   //...
}
````
byteOrder：表示字节流表示的数据是大端还是小端，因为Netty要读取Length字段的值，所以大端小端要设置好，默认Netty是大端序ByteOrder.BIG_ENDIAN。  
maxFrameLength：表示的是包的最大长度，超出包的最大长度netty将会报错；  
lengthFieldOffset：指的是长度域（Length）的偏移量，表示跳过指定长度个字节之后的才是长度域，也就是length前面的字节，也就是头部信息；  
lengthFieldLength：记录该帧数据长度的字段本身的长度；  
lengthAdjustment：该字段加长度字段等于数据帧的长度，包体长度调整的大小，长度域的数值表示的长度加上这个修正值表示的就是带header的包；  
initialBytesToStrip：从数据帧中跳过的字节数，表示获取完一个完整的数据包之后，忽略前面的指定的位数个字节，应用解码器拿到的就是不带长度域的数据包；  
failFast：如果为true，则表示读取到长度域，TA的值的超过maxFrameLength，就抛出一个 TooLongFrameException，而为false表示只有当真正读取完长度域的值表示的字节之后，才会抛出 TooLongFrameException，默认情况下设置为true，建议不要修改，否则可能会造成内存溢出。  







