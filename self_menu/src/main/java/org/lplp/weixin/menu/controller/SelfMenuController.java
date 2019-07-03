package org.lplp.weixin.menu.controller;

import org.lplp.weixin.menu.domain.SelfMenu;
import org.lplp.weixin.menu.service.SelfMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/lplp_1/menu")
public class SelfMenuController {

	@Autowired
	private SelfMenuService selfMenuService;

	@GetMapping
	public ModelAndView index() {

		return new ModelAndView("/WEB-INF/views/menu/index.jsp");
	}

	@GetMapping(produces = "application/json") // 返回JSON数据
	@ResponseBody // 返回的对象就是响应体
	public SelfMenu data() {
		return selfMenuService.getMenu();
	}

	@PostMapping
	@ResponseBody
	// @RequestBody表示把整个请求体，自动转换为Java对象
	public String save(@RequestBody SelfMenu selfMenu) {
		this.selfMenuService.saveMenu(selfMenu);
		return "保存成功";
	}
}
