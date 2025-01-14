package com.emily.connect.client.handler;

import com.emily.connect.core.protocol.TransHeader;
import com.emily.connect.core.utils.MessagePackUtils;
import com.emily.connect.core.utils.UUIDUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

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
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                switch (e.state()) {
                    case READER_IDLE:
                    case WRITER_IDLE:
                        TransHeader transHeader = new TransHeader(UUIDUtils.randomSimpleUUID());
                        //序列化请求头
                        byte[] header = MessagePackUtils.serialize(transHeader);
                        // 创建一个ByteBuf实例
                        ByteBuf byteBuf = Unpooled.buffer();

                        // 向ByteBuf中写入数据
                        byteBuf.writeByte(1);          // 写入一个整数
                        byteBuf.writeInt(43);
                        // 获取ByteBuf中可读字节的数量
                        int readableBytes = byteBuf.readableBytes();

                        // 创建一个字节数组来存储ByteBuf中的数据
                        byte[] array = new byte[readableBytes];

                        // 将ByteBuf中的数据读到字节数组中
                        byteBuf.readBytes(array);
                        //发送心跳包
                        ctx.channel().writeAndFlush(array);
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
