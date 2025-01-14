package com.emily.connect.core.utils;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author :  姚明洋
 * @since :  2025/1/14 下午1:50
 */
public class ByteBufUtils {
    public static String readString(ByteBuf buf) {
        return readString(buf, StandardCharsets.UTF_8);
    }

    public static String readString(ByteBuf buf, Charset charset) {
        int len = buf.readInt();
        byte[] b = new byte[len];
        buf.readBytes(b, 0, len);
        return new String(b, charset);
    }

    /**
     * 将字符串写入ByteBuf中
     */
    public static void writeString(ByteBuf byteBuf, String str) {
        Objects.requireNonNull(byteBuf, "byteBuf is null");
        Objects.requireNonNull(str, "str is null");
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeInt(bytes.length); // 先写入字符串的长度
        byteBuf.writeBytes(bytes);
    }
}
