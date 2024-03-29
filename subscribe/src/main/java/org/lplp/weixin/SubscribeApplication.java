package org.lplp.weixin;

import org.lplp.commons.config.EventListenerConfig;
import org.lplp.commons.domain.event.EventInMessage;
import org.lplp.commons.processors.EventMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@ComponentScan("org.lplp")
@EnableJpaRepositories("org.lplp")
@EntityScan("org.lplp")
public class SubscribeApplication implements 
		EventListenerConfig, 
		ApplicationContextAware {
	private ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}

	private static final Logger LOG = LoggerFactory.getLogger(SubscribeApplication.class);

	public void handle(EventInMessage msg) {
		String id = msg.getEvent().toLowerCase() + "MessageProcessor";
		try {
			EventMessageProcessor mp = (EventMessageProcessor) ctx.getBean(id);
			if (mp != null) {
				mp.onMessage(msg);
			} else {
				LOG.warn("Bean的ID {} 无法调用对应的消息处理器: {} 对应的Bean不存在", id, id);
			}
		} catch (Exception e) {
			LOG.warn("Bean的ID {} 无法调用对应的消息处理器: {}", id, e.getMessage());
//			LOG.trace(e.getMessage(), e);
		}
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(SubscribeApplication.class, args);
//		System.out.println("Spring Boot应用启动成功");
//		CountDownLatch countDownLatch = new CountDownLatch(1);
//		countDownLatch.await();
	}

}
