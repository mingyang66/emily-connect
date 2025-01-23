package com.emily.connect.client.decoder;

import com.emily.connect.core.entity.ResponseEntity;
import com.emily.connect.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Netty 提供了一些内置的解码器（如 ByteToMessageDecoder），这些解码器会自动管理 ByteBuf 的引用计数。当解码完成后，它们会自动释放 ByteBuf，因此你无需手动释放
 *
 * @program: emily-connect
 * @description:
 * @author: Emily
 * @create: 2021/09/23
 */
public class ClientMessagePackDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) {
        ResponseEntity entity = new ResponseEntity();
        byte prefix = byteBuf.readByte();
        if (prefix == 0) {
            entity.prefix(prefix)
                    .status(byteBuf.readInt())
                    .message(ByteBufUtils.readString(byteBuf))
                    .data(ByteBufUtils.readString(byteBuf));
        } else if (prefix == 1) {
            entity.prefix(prefix);
        }
        list.add(entity);
    }
}
