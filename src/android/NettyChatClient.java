package com.dyz.android;

import java.util.Date;
import com.alibaba.fastjson.JSON;


import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;

import javax.net.ssl.SSLEngine;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.FutureListener;

public enum  NettyChatClient {
    NETTY_CHAT_CLIENT();
    //    String host = "dyz";
    String host = "119.29.4.88";
    int port = 8000;
    private EventLoopGroup group;
    private Bootstrap b;
    private ChannelFuture cf ;
    private ChatMsgRecord register = null;
    private NettyChatClient(){
        group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new StringDecoder());
//                        ch.pipeline().addLast(new StringEncoder());
//                        ch.pipeline().addLast(new ObjectEncoder());
//                        ch.pipeline().addLast(new ObjectDecoder(new ClassResolver() {
//                            @Override
//                            public Class<?> resolve(String className) throws ClassNotFoundException {
//                                return Class.forName(className);
//                            }
//                        }));
//                        engine.setUseClientMode(true);
//                        ch.pipeline().addLast("ssl", new SslHandler(engine));
                        ChannelPipeline p = ch.pipeline();
                        SslContext sslCtx = SslContextBuilder.forClient().build();
                        p.addLast("ssl", sslCtx.newHandler(ch.alloc(), host, port));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
    }

    public void connect(){
        try {
            this.cf = b.connect(host, port).sync();
            this.cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("连接成功");
                    if (register == null) {
                        register = new ChatMsgRecord();
                        register.setSendname("123");
                        register.setContent("channel注册请求");
                        future.channel().writeAndFlush(JSON.toJSONString(register)).sync();
                    }
                }
            });
//            ChannelFuture future = this.cf.channel().closeFuture().sync();
//            future.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture future) throws Exception {
//                    System.out.println("链接已关闭");
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChannelFuture getChannelFuture() {
        //如果管道没有被开启或者被关闭了，那么重连
        if (this.cf == null) {
            this.connect();
            System.out.println("注册管道");
        }
        if (!this.cf.channel().isActive()) {
            this.connect();
            System.out.println("注册管道");
        }
        return this.cf;
    }

    public ChannelFuture getChannelFuture(ChatMsgRecord chatMsgRecord) {
        register = chatMsgRecord;
        return getChannelFuture();
    }
    // 暂时以String代替消息对象
    public void write(String msg) throws InterruptedException {
        if(msg.equals("close")){
            this.cf.channel().close();
            return;
        }
        ChatMsgRecord chatMsgRecord = new ChatMsgRecord();
        chatMsgRecord.setContent(msg);
        chatMsgRecord.setSendname("123");
        chatMsgRecord.setReceivename(Constant.receiveName);
        chatMsgRecord.setAddtime(new Date());
        chatMsgRecord.setType(1);
        String json = JSON.toJSONString(chatMsgRecord);
        System.out.println(json);
        ChannelFuture channelFuture = getChannelFuture();
        channelFuture.channel().writeAndFlush(json).sync();
    }
    // 写入对象
    public void write(ChatMsgRecord chatMsgRecord) throws InterruptedException {
        chatMsgRecord.setAddtime(new Date());
        chatMsgRecord.setType(1);
        String json = JSON.toJSONString(chatMsgRecord);
        System.out.println(json);
        ChannelFuture channelFuture = getChannelFuture();
        channelFuture.channel().writeAndFlush(json).sync();
    }
    public static NettyChatClient getInstance(){
        return NETTY_CHAT_CLIENT;
    }
}
