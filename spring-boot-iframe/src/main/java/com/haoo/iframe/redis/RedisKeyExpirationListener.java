package com.haoo.iframe.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @author Haoo
 * 定义监听器
 * 该接口监听所有db的过期事件keyevent@*:expired
 */

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Autowired
    private CustomConfig customConfig;

    /**
     * 针对redis数据失效事件，进行数据处理
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        String expiredKey = message.toString();
        if (expiredKey.startsWith("redis_")) {
            System.out.println(customConfig + "-" + expiredKey);
            //根据key截取linkCode 拼接完整路径
            //String linkCode = expiredKey.substring(expiredKey.lastIndexOf("_") + 1);
            //String source = customConfig.getDlpServerFileUrl() + linkCode;
            //调用删除文件方法
            //FileUtils.removeDir(source);
        }
    }


}