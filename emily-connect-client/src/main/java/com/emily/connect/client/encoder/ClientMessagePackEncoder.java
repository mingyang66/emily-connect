package com.emily.connect.client.encoder;

import com.emily.connect.core.entity.RequestEntity;
import com.emily.connect.core.entity.RequestPayload;
import com.emily.connect.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: emily-connect
 * @description: Rpc编码器 protobuf[<a href="https://github.com/protocolbuffers/protobuf/releases">...</a>]
 * @author: Emily
 * @create: 2021/09/23
 */
public class ClientMessagePackEncoder extends MessageToByteEncoder<RequestEntity> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestEntity entity, ByteBuf byteBuf) {
        if (entity.getPrefix() == 0) {
            byteBuf.writeByte(entity.getPrefix());
            //请求头必须存在
            byteBuf.writeBytes(entity.getHeaders().toByteArray());
            RequestPayload[] payload = entity.getPayload();
            if (payload != null) {
                byteBuf.writeInt(payload.length);
                for (RequestPayload body : payload) {
                    ByteBufUtils.writeString(byteBuf, body.getValue());
                }
            }
        } else if (entity.getPrefix() == 1) {
            byteBuf.writeByte(entity.getPrefix());
        }
    }
}
