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
    public static String readString(ByteBuf byteBuf) {
        return readString(byteBuf, StandardCharsets.UTF_8);
    }

    public static String readString(ByteBuf byteBuf, Charset charset) {
        int len = byteBuf.readInt();
        byte[] b = new byte[len];
        byteBuf.readBytes(b, 0, len);
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

    public static byte[] readBytesByLen(ByteBuf byteBuf) {
        int len = byteBuf.readInt();
        byte[] array = new byte[len];
        byteBuf.readBytes(array, 0, len);
        return array;
    }

    public static byte[] readBytes(ByteBuf byteBuf) {
        // 获取ByteBuf中可读字节的数量
        int readableBytes = byteBuf.readableBytes();
        // 创建一个字节数组来存储ByteBuf中的数据
        byte[] array = new byte[readableBytes];
        // 将ByteBuf中的数据读到字节数组中
        byteBuf.readBytes(array);
        return array;
    }
}
