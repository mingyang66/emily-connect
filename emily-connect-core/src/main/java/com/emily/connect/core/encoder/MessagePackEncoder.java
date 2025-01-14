package com.emily.connect.core.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: SkyDb
 * @description: Rpc编码器 protobuf[https://github.com/protocolbuffers/protobuf/releases]
 * @author: Emily
 * @create: 2021/09/23
 */
public class MessagePackEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] bytes, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(bytes);
    }
}
