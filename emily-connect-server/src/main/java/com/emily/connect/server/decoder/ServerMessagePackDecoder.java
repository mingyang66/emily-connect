package com.emily.connect.server.decoder;

import com.emily.connect.core.entity.RequestEntity;
import com.emily.connect.core.entity.RequestHeader;
import com.emily.connect.core.entity.RequestPayload;
import com.emily.connect.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Netty 提供了一些内置的解码器（如 ByteToMessageDecoder），这些解码器会自动管理 ByteBuf 的引用计数。当解码完成后，它们会自动释放 ByteBuf，因此你无需手动释放
 *
 * @program: emily-connect
 * @description:
 * @author: Emily
 * @create: 2021/09/23
 */
public class ServerMessagePackDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) {
        RequestEntity entity = new RequestEntity();
        byte prefix = byteBuf.readByte();
        if (prefix == 0) {
            entity.setPrefix(prefix);
            entity.setHeaders(new RequestHeader()
                    .systemNumber(ByteBufUtils.readString(byteBuf))
                    .traceId(ByteBufUtils.readString(byteBuf))
                    .appType(ByteBufUtils.readString(byteBuf))
                    .appVersion(ByteBufUtils.readString(byteBuf))
                    .contentType(byteBuf.readByte())
                    .url(ByteBufUtils.readString(byteBuf))
                    .method(ByteBufUtils.readString(byteBuf))
                    .timeout(byteBuf.readInt()));
            int count = byteBuf.readInt();
            if (count > 0) {
                List<RequestPayload> payload = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    String value = ByteBufUtils.readString(byteBuf);
                    payload.add(new RequestPayload(value));
                }
                entity.setPayload(payload.toArray(new RequestPayload[0]));
            }
        } else if (prefix == 1) {
            entity.setPrefix(prefix);
        }
        list.add(entity);
    }
}
