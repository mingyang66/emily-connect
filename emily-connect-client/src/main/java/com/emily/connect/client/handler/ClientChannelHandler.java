package com.emily.connect.client.handler;

import com.emily.connect.core.constant.PrefixType;
import com.emily.connect.core.entity.RequestEntity;
import com.emily.connect.core.entity.ResponseEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.emily.connect.client.handler.PoolClientChannelHandler.POOL_CHANNEL_HANDLER;

/**
 * @program: emily-connect
 * @description: 由于需要在 handler 中发送消息给服务端，并且将服务端返回的消息读取后返回给消费者,所以实现了 Callable 接口，这样可以运行有返回值的线程
 * @author: Emily
 * @create: 2021/09/17
 */
public class ClientChannelHandler extends SimpleChannelInboundHandler<ResponseEntity> {
    private static final int MAX_UNRESPONSE_HEARTBEATS = 3;
    private int unResponseHeartbeats = 0;
    /**
     * 锁对象
     */
    public final Object object = new Object();

    public ResponseEntity result;

    public ClientChannelHandler() {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " 新建ClientChannelHandler");
    }

    /**
     * 当通道变为活动状态时（即连接已建立并且准备好通信），这个方法会被调用
     * 通常用于发送初始化消息或进行一些连接建立后的设置。
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " channelActive：" + ctx.channel().remoteAddress());
    }

    /**
     * 当通道变为不活动状态时（即连接已断开），这个方法会被调用。
     * 通常用于清理资源或进行一些连接关闭后的操作。
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " channelInactive：" + ctx.channel().remoteAddress());
        POOL_CHANNEL_HANDLER.remove(ctx.channel().id());
        ctx.close();
    }

    /**
     * 用于处理每个入站的消息，当有新的消息到达时，这个方法会被调用
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseEntity response) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " 接收到响应数据channelRead0：" + response.getMessage());
        if (response.getPrefix() == PrefixType.SERVLET || response.getPrefix() == PrefixType.TCP) {
            synchronized (this.object) {
                result = response;
                this.object.notify();
            }
        } else if (response.getPrefix() == PrefixType.HEARTBEAT) {
            unResponseHeartbeats = 0;
        }
    }

    /**
     * 当通道读取完成时，这个方法会被调用。
     * 可以用于触发一些读取完成后的操作。
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " 消息读取完成channelReadComplete：" + ctx.channel().remoteAddress());
    }

    /**
     * 当用户事件被触发时，这个方法会被调用。
     * 可以用于处理用户自定义事件。
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " 通道已经超过20秒未与服务端进行读写操作，发送心跳包..." + ctx.channel().remoteAddress());
        if (evt instanceof IdleStateEvent e) {
            switch (e.state()) {
                case READER_IDLE:
                case WRITER_IDLE:
                    //发送心跳包
                    ctx.channel().writeAndFlush(new RequestEntity().prefix(PrefixType.HEARTBEAT));
                    unResponseHeartbeats++;
                    if (unResponseHeartbeats > MAX_UNRESPONSE_HEARTBEATS) {
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " No response for " + MAX_UNRESPONSE_HEARTBEATS + " heartbeats, closing connection.");
                        POOL_CHANNEL_HANDLER.remove(ctx.channel().id());
                        ctx.close();
                    }
                    break;
                case ALL_IDLE:
                default:
                    break;
            }
        } else {
            //继续传播事件
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 当处理过程中出现异常时，这个方法会被调用。
     * 通常用于记录错误日志或关闭连接。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + cause.getMessage());
        POOL_CHANNEL_HANDLER.remove(ctx.channel().id());
        ctx.close();
    }

}
