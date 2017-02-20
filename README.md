# zzby-netty-qq
用netty做的一个简单聊天程序，控制台进行输入输出

计算机5层模型：物理层，数据链路层，网络层，传输层，应用层

两个计算机进行信息交互时，通过IP(网络层)和端口号(传输层)找到运行在计算机中的应用程序进程，
通过约定协议(应用层)来交流信息。

传输层分为tcp和udp，此聊天程序使用的是tcp，面向连接，流式传输，
若启用nagle算法或接收端读取数据不及时，可能会出现粘包现象

定义了一个简单的聊天协议，包括消息头和消息体，消息头有6个字节，
第1字节为版本号，第2字节为消息类型，剩下的4个字节为消息体的长度，
消息体内容以utf-8编码



netty是个nio框架，客户端和服务端交互的过程大致为：

客户端业务逻辑 -->  编码发送  -->  服务端接收解码  -->  服务端业务逻辑


服务端接收数据又可分解为：

io就绪  -->   io读写   -->   处理业务逻辑

io的处理是个不断演化的过程：

单线程阻塞方式：
由单独的线程阻塞监听io是否就绪并读写io，
缺点不能支持高并发

select/poll方式：
1、将文件描述符集合从用户空间拷贝到内核空间，挂起当前进程
2、当有io就绪时，唤醒进程，轮循描述符集合，找到已就绪的io
3、将文件描述符集合从内核空间拷贝到用户空间

缺点：
1、内存开销大，需多次拷贝文件描述符集合
2、需要遍历描述符集合来获取就绪io，时间复杂度是O(n)

epoll方式：
1、使用内存映射减少大量的内存拷贝
2、会将就绪的io放到一个就绪链表中，当回调唤醒进程后，只需遍历就绪链表，就可获得所有的就绪io

以上3种方式对io的读写都是同步的，所有io读写可能成为性能瓶颈

常用的线程模型：
1、1个boss线程来获取就绪的io
2、使用io线程池来读写io（如果业务逻辑只是简单的内存操作，也可在io线程中完成，减少线程调度）
3、使用work线程池来处理业务逻辑
