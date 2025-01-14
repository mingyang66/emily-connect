package com.emily.connect.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @program: SkyDb
 * @description:
 * @author: Emily
 * @create: 2021/09/17
 */
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    private final ServerBusinessHandler handler;

    public ServerChannelHandler(ServerBusinessHandler handler) {
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Rpc客户端连接成功：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().close();
        System.out.println("Rpc服务器连接断开：" + ctx.channel().remoteAddress());
    }

    /**
     * 接收客户端传入的值，将值解析为类对象，获取其中的属性，然后反射调用实现类的方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg == null) {
            return;
        }
        try {
            if (msg instanceof byte[] array) {
                // 通过现成的byte数组创建一个ByteBuf对象
                ByteBuf byteBuf = Unpooled.wrappedBuffer(array);
                // 打印ByteBuf中的数据以验证
                if (byteBuf.isReadable()) {
                    byte prefix = byteBuf.readByte();
                    if (prefix == 0) {
                        System.out.println("读取正文消息：" + byteBuf.readInt());
                    } else if (prefix == 1) {
                        System.out.println("读取心跳消息：" + byteBuf.readInt());

                    }
                }
            } else {
                //todo 非可识别数据类型
                System.out.println("----非可识别数据类型----");
            }


            //消息类型
            // byte packageType = requestEntity.packageType;
            //心跳包
            // if (packageType == 1) {
            //   String heartBeat = MessagePackUtils.deSerialize(requestEntity.content, String.class);
            // System.out.println("心跳包是：" + heartBeat);
            //return;
            //}
            //请求消息体
            //TransContent transContent = MessagePackUtils.deSerialize(requestEntity.content, TransContent.class);
            //获取后置处理结果
            //Object value = this.handler.invoke(transContent);
            //发送调用方法调用结果
            //ctx.writeAndFlush(new DataPacket(requestEntity.header, MessagePackUtils.serialize(value)));
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            //手动释放消息，否则会导致内存泄漏
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
