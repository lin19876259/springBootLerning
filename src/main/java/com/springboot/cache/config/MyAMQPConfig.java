package com.springboot.cache.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAMQPConfig {

    /*
       该配置bean的目的是把消息序列化成json数据
       MessageConverter是一个接口，下面有很多转换测实现类。用ctrl+h查找下面的实现类。
       Jackson2JsonMessageConverter()方法把数据转换成json
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
