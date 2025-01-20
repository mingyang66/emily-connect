package com.emily.connect.client.handler;

import com.emily.connect.core.entity.RequestEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.nio.charset.StandardCharsets;

/**
 * @Description :  心跳处理器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/25 2:16 PM
 */
public class HeartBeatChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        {
            System.out.println("通道已经超过20秒未与服务端进行读写操作，发送心跳包..." + ctx.channel().remoteAddress());
            if (evt instanceof IdleStateEvent e) {
                switch (e.state()) {
                    case READER_IDLE:
                    case WRITER_IDLE:
                        byte[] hearBeat = "hearBeat...".getBytes(StandardCharsets.UTF_8);
                        RequestEntity entity = new RequestEntity();
                        entity.setPrefix((byte) 1);
                        // entity.setBody(hearBeat);
                        // 创建一个ByteBuf实例
//                        ByteBuf byteBuf = Unpooled.buffer();
//                        // 向ByteBuf中写入数据
//                        byteBuf.writeByte(1);
//                        byteBuf.writeInt(hearBeat.length);
//                        byteBuf.writeBytes(hearBeat);
//                        // 创建一个字节数组来存储ByteBuf中的数据
//                        byte[] array = new byte[byteBuf.readableBytes()];
//                        // 将ByteBuf中的数据读到字节数组中
//                        byteBuf.readBytes(array);
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
}
