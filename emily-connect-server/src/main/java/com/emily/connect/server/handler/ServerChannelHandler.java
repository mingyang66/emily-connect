package com.emily.connect.server.handler;

import com.emily.connect.core.entity.RequestEntity;
import com.emily.connect.server.plugin.Plugin;
import com.emily.connect.server.plugin.PluginRegistry;
import com.emily.connect.server.plugin.PluginType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;

/**
 * @program: SkyDb
 * @description:
 * @author: Emily
 * @create: 2021/09/17
 */
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

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
        System.out.println("----------------------channelRead----------------------" + ctx.channel().id());
        if (msg == null) {
            return;
        }
        try {
            if (msg instanceof RequestEntity entity) {
                byte prefix = entity.getPrefix();
                // 打印ByteBuf中的数据以验证
                if (prefix == 0) {
                    Plugin<?> plugin = switch (entity.getHeaders().getContentType()) {
                        case 0 -> PluginRegistry.getPlugin(PluginType.BEAN);
                        case 1 -> PluginRegistry.getPlugin(PluginType.STRING);
                        default -> null;
                    };
                    Object response = plugin.invoke(entity.getHeaders(), entity.getBody());
                    //发送调用方法调用结果
                    ctx.writeAndFlush(response);
                } else if (prefix == 1) {
                    System.out.println("读取心跳消息：");
                   /* int bodyLength = byteBuf.readInt();
                    byte[] body = new byte[bodyLength];
                    byteBuf.readBytes(body);*/
                    System.out.println("心跳请求体：" + new String(entity.getBody(), StandardCharsets.UTF_8));
                }
            } else {
                //todo 非可识别数据类型
                System.out.println("----非可识别数据类型----");
            }
        } catch (Throwable exception) {
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
