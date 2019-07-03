package org.lplp.weixin.processors.impl;

import org.lplp.commons.domain.User;
import org.lplp.commons.domain.event.EventInMessage;
import org.lplp.commons.processors.EventMessageProcessor;
import org.lplp.commons.repository.UserRepository;
import org.lplp.commons.service.WeixinProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("subscribeMessageProcessor")
public class SubscribeEventMessageProcessor implements EventMessageProcessor {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WeixinProxy weixinProxy;

	@Override
	public void onMessage(EventInMessage msg) {
		if (!msg.getEvent().equals("subscribe")) {
			return;
		}
		String openId = msg.getFromUserName();
		User user = this.userRepository.findByOpenId(openId);
		if (user == null || user.getStatus() != User.Status.IS_SUBSCRIBE) {
			String account = "";
			User wxUser = weixinProxy.getUser(account, openId);
			if (wxUser == null) {
				return;
			}
			if (user != null) {
				wxUser.setId(user.getId());
				wxUser.setSubTime(user.getSubTime());
				wxUser.setUnsubTime(null);
			}
			wxUser.setStatus(User.Status.IS_SUBSCRIBE);

			this.userRepository.save(wxUser);

			weixinProxy.sendText(account, openId, "欢迎关注我的公众号，回复帮助可获得人工智能菜单");
		}
	}
}
