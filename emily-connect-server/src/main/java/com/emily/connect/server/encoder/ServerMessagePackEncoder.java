package com.emily.connect.server.encoder;

import com.emily.connect.core.entity.ResponseEntity;
import com.emily.connect.core.utils.ByteBufUtils;
import com.emily.infrastructure.json.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: SkyDb
 * @description: Rpc编码器 protobuf[<a href="https://github.com/protocolbuffers/protobuf/releases">...</a>]
 * @author: Emily
 * @create: 2021/09/23
 */
public class ServerMessagePackEncoder extends MessageToByteEncoder<ResponseEntity> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseEntity entity, ByteBuf byteBuf) {
        if (entity.getPrefix() == 0) {
            byteBuf.writeByte(entity.getPrefix());
            byteBuf.writeInt(entity.getStatus());
            ByteBufUtils.writeString(byteBuf, entity.getMessage());
            Object data = entity.getData();
            if (data == null) {
                byteBuf.writeInt(0);
            } else if (data instanceof byte[] bytes) {
                byteBuf.writeInt(bytes.length);
                byteBuf.writeBytes(bytes);
            } else if (data instanceof String str) {
                ByteBufUtils.writeString(byteBuf, str);
            } else {
                ByteBufUtils.writeString(byteBuf, JsonUtils.toJSONString(data));
            }
        } else if (entity.getPrefix() == 1) {
            byteBuf.writeByte(entity.getPrefix());
        }
    }
}
