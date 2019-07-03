package org.lplp.weixin.unsubscribe;

import java.util.Date;

import javax.transaction.Transactional;

import org.lplp.commons.domain.User;
import org.lplp.commons.domain.event.EventInMessage;
import org.lplp.commons.processors.EventMessageProcessor;
import org.lplp.commons.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("unsubscribeMessageProcessor")
public class UnsubscribeEventMessageProcessor implements EventMessageProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(UnsubscribeEventMessageProcessor.class);
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public void onMessage(EventInMessage msg) {
		if (!msg.getEvent().equals("unsubscribe")) {
			return;
		}

		LOG.trace("处理取消关注的消息：" + msg);

		User user = this.userRepository.findByOpenId(msg.getFromUserName());
		if (user != null) {
			user.setStatus(User.Status.IS_UNSUBSCRIBE);
			user.setUnsubTime(new Date());
		}
	}
}
