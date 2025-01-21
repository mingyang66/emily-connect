package com.emily.connect.client.handler;

import com.emily.connect.core.entity.RequestEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description :  心跳处理器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/25 2:16 PM
 */
public class HeartBeatChannelHandler extends ChannelInboundHandlerAdapter {
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
}
