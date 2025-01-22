package com.emily.connect.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.util.internal.PlatformDependent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @Description :  为ChannelPool处理各种操作调用的处理程序，如：创建、释放、获取处理程序
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/15 10:31 AM
 */
public class PoolClientChannelHandler extends AbstractChannelPoolHandler {
    /**
     * 缓存Channel与handler的映射关系
     */
    public static final Map<ChannelId, ClientChannelHandler> POOL_CHANNEL_HANDLER = PlatformDependent.newConcurrentHashMap();

    /**
     * 当通道从连接池中获取时调用
     */
    @Override
    public void channelAcquired(Channel ch) throws Exception {
        super.channelAcquired(ch);
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "  Pool channelAcquired：" + ch.id());
    }

    /**
     * 当通道被释放回连接池时调用
     */
    @Override
    public void channelReleased(Channel ch) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Pool channelReleased：" + ch.id());
    }

    /**
     * 当创建一个新的通道时调用
     */
    @Override
    public void channelCreated(Channel ch) throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Pool channelCreated：" + ch.id());
        //自定义handler
        ClientChannelHandler channelHandler = new ClientChannelHandler();
        //将通道和自定义的 ClientChannelHandler 关联并存储到 POOL_CHANNEL_HANDLER
        POOL_CHANNEL_HANDLER.putIfAbsent(ch.id(), channelHandler);
        //Channel初始化实例对象
        ClientChannelInitializer initializer = new ClientChannelInitializer(channelHandler);
        //初始化Channel
        initializer.initChannel(ch);
    }
}
