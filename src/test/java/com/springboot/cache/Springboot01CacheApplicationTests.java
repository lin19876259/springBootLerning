package com.springboot.cache;

import com.springboot.cache.bean.Book;
import com.springboot.cache.bean.Employee;
import com.springboot.cache.mapper.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot01CacheApplicationTests {

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    StringRedisTemplate  stringRedisTemplate; //操作k-v都是字符串的

    @Autowired
    RedisTemplate redisTemplate;  //k-v都是对象的

    @Autowired
    RedisTemplate<Object, Employee> empRedisTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;
    //AmqpAdmin amqpAdmin;

    /**
     * Redis常见的五大数据类型
     *  String（字符串）、List（列表）、Set（集合）、Hash（散列）、ZSet（有序集合）
     *  stringRedisTemplate.opsForValue()[String（字符串）]
     *  stringRedisTemplate.opsForList()[List（列表）]
     *  stringRedisTemplate.opsForSet()[Set（集合）]
     *  stringRedisTemplate.opsForHash()[Hash（散列）]
     *  stringRedisTemplate.opsForZSet()[ZSet（有序集合）]
     */
    @Test
    public void test01(){
        //给redis中保存数据
        //stringRedisTemplate.opsForValue().append("msg","hello");
//		String msg = stringRedisTemplate.opsForValue().get("msg");
//		System.out.println(msg);

//		stringRedisTemplate.opsForList().leftPush("mylist","1");
//		stringRedisTemplate.opsForList().leftPush("mylist","2");
    }

    //测试保存对象
    @Test
    public void test02(){
        Employee empById = employeeMapper.getEmpById(1);
        //默认如果保存对象，使用jdk序列化机制，序列化后的数据保存到redis中
        //redisTemplate.opsForValue().set("emp-01",empById);
        //1、将数据以json的方式保存
        //(1)自己将对象转为json
        //(2)redisTemplate默认的序列化规则；改变默认的序列化规则；
        empRedisTemplate.opsForValue().set("emp-01",empById);
    }



    @Test
    public void contextLoads() {

        Employee empById = employeeMapper.getEmpById(1);
        System.out.println(empById);

    }



    @Test
    public void createExchange(){

//		amqpAdmin.declareExchange(new DirectExchange("amqpadmin.exchange"));
//		System.out.println("创建完成");

//		amqpAdmin.declareQueue(new Queue("amqpadmin.queue",true));
        //创建绑定规则

//		amqpAdmin.declareBinding(new Binding("amqpadmin.queue", Binding.DestinationType.QUEUE,"amqpadmin.exchange","amqp.haha",null));

        //amqpAdmin.de
    }

    /**
     * 1、单播（点对点）
     */
    @Test
    public void contextLoadsrabbit() {
        //Message需要自己构造一个;定义消息体内容和消息头
        //rabbitTemplate.send(exchage,routeKey,message);

        //object默认当成消息体，只需要传入要发送的对象，自动序列化发送给rabbitmq；
        //rabbitTemplate.convertAndSend(exchage,routeKey,object);
        Map<String,Object> map = new HashMap<>();
        map.put("msg","这是第一个消息");
        map.put("data", Arrays.asList("helloworld",123,true));
        //对象被默认序列化以后发送出去
        rabbitTemplate.convertAndSend("exchange.direct","atspringboot.news",new Book("西游记","吴承恩"));

    }

    //接受数据,如何将数据自动的转为json发送出去
    @Test
    public void receive(){
        Object o = rabbitTemplate.receiveAndConvert("atspringboot.news");
        System.out.println(o.getClass());
        System.out.println(o);
    }

    /**
     * 广播
     */
    @Test
    public void sendMsg(){
        rabbitTemplate.convertAndSend("exchange.fanout","",new Book("红楼梦","曹雪芹"));
    }

}
