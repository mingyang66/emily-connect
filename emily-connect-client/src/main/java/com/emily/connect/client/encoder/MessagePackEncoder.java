package com.emily.connect.client.encoder;

import com.emily.connect.core.entity.RequestEntity;
import com.emily.connect.core.entity.RequestPayload;
import com.emily.connect.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: SkyDb
 * @description: Rpc编码器 protobuf[<a href="https://github.com/protocolbuffers/protobuf/releases">...</a>]
 * @author: Emily
 * @create: 2021/09/23
 */
public class MessagePackEncoder extends MessageToByteEncoder<RequestEntity> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestEntity entity, ByteBuf byteBuf) {
        byteBuf.writeByte(entity.getPrefix());
        if (entity.getPrefix() == 0) {
            byteBuf.writeBytes(entity.getHeaders().toByteArray());
        }
        RequestPayload[] payload = entity.getPayload();
        if (payload != null) {
            byteBuf.writeInt(payload.length);
            for (RequestPayload body : payload) {
                ByteBufUtils.writeString(byteBuf, body.getValue());
            }
        }
    }
}
