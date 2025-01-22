package com.emily.connect.client.handler;

import com.emily.connect.core.entity.RequestEntity;
import com.emily.connect.core.entity.ResponseEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.emily.connect.client.handler.PoolClientChannelHandler.POOL_CHANNEL_HANDLER;

/**
 * @program: SkyDb
 * @description: 由于需要在 handler 中发送消息给服务端，并且将服务端返回的消息读取后返回给消费者,所以实现了 Callable 接口，这样可以运行有返回值的线程
 * @author: Emily
 * @create: 2021/09/17
 */
public class ClientChannelHandler extends SimpleChannelInboundHandler<ResponseEntity> {
    /**
     * 锁对象
     */
    public final Object object = new Object();

    public ResponseEntity result;

    public ClientChannelHandler() {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " 新建ClientChannelHandler");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseEntity response) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " 接收到响应数据channelRead0：" + response.getMessage());
        if (response.getPrefix() == 0) {
            synchronized (this.object) {
                result = response;
                this.object.notify();
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "通道已经超过20秒未与服务端进行读写操作，发送心跳包..." + ctx.channel().remoteAddress());
        if (evt instanceof IdleStateEvent e) {
            switch (e.state()) {
                case READER_IDLE:
                case WRITER_IDLE:
                    RequestEntity entity = new RequestEntity().prefix((byte) 1);
                    //发送心跳包
                    ctx.channel().writeAndFlush(entity);
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
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + cause.getMessage());
        if (ctx.channel().id() != null) {
            POOL_CHANNEL_HANDLER.remove(ctx.channel().id());
        }
        ctx.close();
    }

}
