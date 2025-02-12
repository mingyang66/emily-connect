package com.emily.connect.server.handler;

import com.emily.connect.core.constant.PrefixType;
import com.emily.connect.core.entity.RequestEntity;
import com.emily.connect.core.entity.ResponseEntity;
import com.emily.connect.server.TcpServerProperties;
import com.emily.connect.server.plugin.Plugin;
import com.emily.connect.server.plugin.PluginRegistry;
import com.emily.connect.server.plugin.PluginType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @program: emily-connect
 * @description:
 * @author: Emily
 * @create: 2021/09/17
 */
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(ServerChannelHandler.class);
    /**
     * 未响应心跳次数
     */
    private int unResponseHeartbeats = 0;
    /**
     * 客户端属性配置文件
     */
    private final TcpServerProperties properties;

    public ServerChannelHandler(TcpServerProperties properties) {
        this.properties = properties;
    }

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
            if (Objects.nonNull(msg) && msg instanceof RequestEntity entity) {
                prefix = entity.getPrefix();
                // 打印ByteBuf中的数据以验证
                if (prefix == PrefixType.HEARTBEAT) {
                    unResponseHeartbeats = 0;
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
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        LOG.warn("{} 通道已经超过20秒未与服务端进行读写操作，发送心跳包...", ctx.channel().remoteAddress());
        if (evt instanceof IdleStateEvent e) {
            switch (e.state()) {
                case READER_IDLE:
                case WRITER_IDLE:
                case ALL_IDLE:
                    //发送心跳包
                    ctx.channel().writeAndFlush(new RequestEntity().prefix(PrefixType.HEARTBEAT));
                    unResponseHeartbeats++;
                    if (unResponseHeartbeats > properties.getMaxUnResponseHeartbeats()) {
                        LOG.warn("{} No response for {}  heartbeats, closing connection.", ctx.channel().remoteAddress(), properties.getMaxUnResponseHeartbeats());
                        ctx.close();
                    }
                    break;
                default:
                    break;
            }
        } else {
            //继续传播事件
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("{} Tcp服务器端异常exceptionCaught {}", ctx.channel().remoteAddress(), cause.getMessage());
        ctx.close();
    }
}
