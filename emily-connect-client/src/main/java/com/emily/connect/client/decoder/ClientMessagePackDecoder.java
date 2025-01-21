package com.emily.connect.client.decoder;

import com.emily.connect.core.entity.ResponseEntity;
import com.emily.connect.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @program: SkyDb
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
