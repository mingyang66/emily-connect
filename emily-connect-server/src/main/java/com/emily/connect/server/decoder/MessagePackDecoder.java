package com.emily.connect.server.decoder;

import com.emily.connect.core.entity.RequestEntity;
import com.emily.connect.core.entity.RequestHeader;
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
public class MessagePackDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) {
        RequestEntity entity = new RequestEntity();
        entity.setPrefix(byteBuf.readByte());
        if (entity.getPrefix() == 0) {
            entity.setHeaders(new RequestHeader()
                    .systemNumber(ByteBufUtils.readString(byteBuf))
                    .traceId(ByteBufUtils.readString(byteBuf))
                    .appType(ByteBufUtils.readString(byteBuf))
                    .appVersion(ByteBufUtils.readString(byteBuf))
                    .contentType(byteBuf.readByte())
                    .action(ByteBufUtils.readString(byteBuf)));
        }
        entity.setBody(ByteBufUtils.readBytes(byteBuf));
        list.add(entity);
    }
}
