package org.lplp.commons.config;

import java.util.ArrayList;
import java.util.List;

import org.lplp.commons.domain.InMessage;
import org.lplp.commons.domain.ResponseToken;
import org.lplp.commons.domain.event.EventInMessage;
import org.lplp.commons.service.JsonRedisSerializer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface EventListenerConfig extends//	
		CommandLineRunner, 
		DisposableBean {

	// 这是一个停止监视器，等待是否停止的通知
	public final Object stopMonitor = new Object();

	@Override
	public default void run(String... args) throws Exception {
		new Thread(() -> {
			synchronized (stopMonitor) {
				try {
					// 等待停止通知
					stopMonitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public default void destroy() throws Exception {
		// 发送停止通知
		synchronized (stopMonitor) {
			stopMonitor.notify();
		}
	}

	// 相当于Spring的XML配置方式中的<bean>元素
	@Bean
	public default RedisTemplate<String, InMessage> inMessageTemplate(//
			@Autowired RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, InMessage> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);

		template.setValueSerializer(new JsonRedisSerializer());

		return template;
	}

	@Bean
	public default RedisTemplate<String, ResponseToken> tokenRedisTemplate(//
			@Autowired RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, ResponseToken> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setValueSerializer(new JsonRedisSerializer());

		return template;
	}

	@Bean
	public default MessageListenerAdapter messageListener(
			@Autowired RedisTemplate<String, InMessage> inMessageTemplate) {
		MessageListenerAdapter adapter = new MessageListenerAdapter();
		// 共用模板里面的序列化程序
		adapter.setSerializer(inMessageTemplate.getValueSerializer());

		// 设置消息处理程序的代理对象
		adapter.setDelegate(this);
		// 设置代理对象里面哪个方法用于处理消息，设置方法名
		adapter.setDefaultListenerMethod("handle");

		return adapter;
	}

	@Bean
	public default RedisMessageListenerContainer messageListenerContainer(//
			@Autowired RedisConnectionFactory redisConnectionFactory, //
			@Autowired MessageListener l) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		// 可以监听多个通道的消息
		List<Topic> topics = new ArrayList<>();

		// 监听具体某个通道
		topics.add(new ChannelTopic("kemao_1_event"));
		container.addMessageListener(l, topics);

		return container;
	}

	public void handle(EventInMessage msg);
}
