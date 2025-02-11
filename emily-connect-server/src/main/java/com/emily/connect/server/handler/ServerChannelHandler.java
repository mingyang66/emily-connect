package com.emily.connect.server.handler;

import com.emily.connect.core.constant.PrefixType;
import com.emily.connect.core.entity.RequestEntity;
import com.emily.connect.core.entity.ResponseEntity;
import com.emily.connect.server.plugin.Plugin;
import com.emily.connect.server.plugin.PluginRegistry;
import com.emily.connect.server.plugin.PluginType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: emily-connect
 * @description:
 * @author: Emily
 * @create: 2021/09/17
 */
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Tpc客户端连接成功channelActive：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Tpc服务器连接断开channelInactive：" + ctx.channel().remoteAddress());
        ctx.close();
    }

    /**
     * 接收客户端传入的值，将值解析为类对象，获取其中的属性，然后反射调用实现类的方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Tcp服务器读取到客户端数据channelRead：" + ctx.channel().remoteAddress());
        byte prefix = PrefixType.TCP;
        try {
            if (msg == null) {
                ctx.writeAndFlush(new ResponseEntity().prefix(prefix).status(10000).message("无有效请求消息"));
            } else if (msg instanceof RequestEntity entity) {
                prefix = entity.getPrefix();
                // 打印ByteBuf中的数据以验证
                if (prefix == PrefixType.HEARTBEAT) {
                    ctx.writeAndFlush(new ResponseEntity().prefix(PrefixType.HEARTBEAT));
                } else {
                    Plugin<?> plugin = PluginRegistry.getPlugin(PluginType.getPluginTypeByCode(prefix));
                    Object response = plugin.invoke(entity.getHeaders(), entity.getPayload());
                    ctx.writeAndFlush(response);
                }
            } else {
                ctx.writeAndFlush(new ResponseEntity().prefix(prefix).status(10000).message("无有效请求消息"));
            }
        } catch (Throwable exception) {
            //全局异常处理 todo
            ctx.writeAndFlush(new ResponseEntity().prefix(prefix).status(10000).message(exception.getMessage()));
        } finally {
            //手动释放消息，否则会导致内存泄漏
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Tcp服务器端异常exceptionCaught：" + ctx.channel().remoteAddress() + ": " + cause.getMessage());
        ctx.close();
    }
}
