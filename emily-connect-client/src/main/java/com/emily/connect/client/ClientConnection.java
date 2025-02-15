package com.emily.connect.client;

import com.emily.connect.client.handler.ClientChannelHandler;
import com.emily.connect.client.handler.PoolClientChannelHandler;
import com.emily.connect.core.entity.RequestEntity;
import com.emily.connect.core.entity.RequestHeader;
import com.emily.connect.core.entity.RequestPayload;
import com.emily.connect.core.entity.ResponseEntity;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import org.apache.commons.lang3.math.NumberUtils;

import java.net.InetSocketAddress;
import java.util.Objects;

import static com.emily.connect.client.handler.PoolClientChannelHandler.POOL_CHANNEL_HANDLER;

/**
 * @program: emily-connect
 * @description: 创建Netty客户端及自定义处理器
 * @author: Emily
 * @create: 2021/09/17
 */
public class ClientConnection {
    /**
     * 线程工作组
     */
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();
    /**
     * 允许将特定的key映射到ChannelPool，可以获取匹配的ChannelPool,如果不存在则会创建一个新的对象
     * 即：根据不同的服务器地址初始化ChannelPoolMap
     */
    private final ChannelPoolMap<InetSocketAddress, ChannelPool> poolMap;

    private final ClientProperties properties;

    public ClientConnection(ClientProperties properties) {
        this.properties = properties;
        this.poolMap = this.createChannelPoolMap(properties);
    }

    public Bootstrap createBootstrap() {
        return new Bootstrap()
                //设置线程组
                .group(workerGroup)
                //初始化通道
                .channel(NioSocketChannel.class)
                /**
                 * 是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，
                 * 这套机制才会被激活
                 */
                .option(ChannelOption.SO_KEEPALIVE, true)
                /**
                 * 1.在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，同时，对方接收到数据，也需要发送ACK表示确认。
                 * 为了尽可能的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。这里就涉及到一个名为Nagle的算法，该算法的目的就是为了尽可能发送大块数据，
                 * 避免网络中充斥着许多小数据块。
                 * 2.TCP_NODELAY就是用于启用或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；
                 * 如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
                 */
                .option(ChannelOption.TCP_NODELAY, true)
                /**
                 * Channel连接超时时间，默认：5s
                 * The timeout period of the connection.
                 * If this time is exceeded or the connection cannot be established, the connection fails.
                 */
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NumberUtils.toInt(String.valueOf(properties.getConnectTimeOut().toMillis())));
    }

    public AbstractChannelPoolMap<InetSocketAddress, ChannelPool> createChannelPoolMap(ClientProperties properties) {
        //ChannelPool存储、创建、删除管理Map类
        return new AbstractChannelPoolMap<>() {
            //如果ChannelPool不存在，则会创建一个新的对象
            @Override
            protected ChannelPool newPool(InetSocketAddress inetSocketAddress) {
                return new FixedChannelPool(createBootstrap().remoteAddress(inetSocketAddress), new PoolClientChannelHandler(), properties.getMaxConnections());
            }
        };
    }

    /**
     * 获取ChannelPool连接池
     */
    public ChannelPool getChannelPool(InetSocketAddress inetSocketAddress) {
        return poolMap.get(inetSocketAddress);
    }

    /**
     * 发送请求
     *
     * @param requestHeader 请求头
     * @param payload       请求体
     */
    public ResponseEntity getForEntity(String tag, RequestHeader requestHeader, RequestPayload... payload) {
        Objects.requireNonNull(tag, "tag can not be null");
        Objects.requireNonNull(tag, "requestHeader can not be null");
        ChannelPool pool = getChannelPool(this.getSocketAddress(tag));
        RequestEntity entity = new RequestEntity()
                .prefix((byte) 0)
                .headers(requestHeader)
                .payload(payload);
        return getForEntity(pool, entity);
    }

    /**
     * 发送请求
     */
    public ResponseEntity getForEntity(ChannelPool pool, RequestEntity entity) {
        Objects.requireNonNull(pool, "ChannelPool can not be null");
        Objects.requireNonNull(entity, "RequestEntity can not be null");
        ResponseEntity response;
        Channel channel = null;
        ClientChannelHandler channelHandler = null;
        try {
            //从ChannelPool中获取一个Channel
            Future<Channel> future = pool.acquire();
            //等待future完成
            future.await();
            //判定I/O操作是否成功完成
            if (future.isSuccess()) {
                //无阻塞获取Channel对象
                channel = future.getNow();
                if (channel != null && channel.isActive() && channel.isWritable()) {
                    //获取信道对应的handler对象
                    channelHandler = POOL_CHANNEL_HANDLER.get(channel.id());
                    if (channelHandler != null) {
                        synchronized (channelHandler.object) {
                            //发送TCP请求
                            channel.writeAndFlush(entity);
                            //等待请求返回结果
                            channelHandler.object.wait(this.properties.getReadTimeOut().toMillis());
                        }
                        //根据返回结果做后续处理
                        response = channelHandler.result;
                    } else {
                        throw new IllegalAccessException("The Channel processor handler is empty");
                    }
                } else {
                    throw new IllegalAccessException("Channel unavailable");
                }
            } else {
                throw new IllegalAccessException("Get Channel is Fail");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (Objects.nonNull(channel)) {
                pool.release(channel);
            }
            if (Objects.nonNull(channelHandler)) {
                channelHandler.result = null;
            }
        }
        return response;
    }

    public ClientProperties getProperties() {
        return properties;
    }

    public InetSocketAddress getSocketAddress(String tag) {
        String address = this.getProperties().getConfig().get(tag);
        if (address == null) {
            throw new IllegalStateException("Server address can not be config");
        }
        String host = address.split(":")[0];
        String port = address.split(":")[1];
        return new InetSocketAddress(host, Integer.parseInt(port));
    }
}
