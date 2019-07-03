package org.lplp.weixin.library.comtroller;

import org.lplp.weixin.library.domain.DebitList;
import org.lplp.weixin.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/lplp_1/library/debit")
@SessionAttributes({ "debitList" })
public class DebitController {

	@Autowired
	private LibraryService libraryService;

	@RequestMapping
	public String debit(@RequestParam("id") String id, WebRequest request) {

		DebitList list = (DebitList) request.getAttribute("debitList", WebRequest.SCOPE_SESSION);
		if (list == null) {
			list = new DebitList();
			request.setAttribute("debitList", list, WebRequest.SCOPE_SESSION);
		}

		this.libraryService.add(id, list);
		return "redirect:/lplp_1/library/debit/list";
	}

	@RequestMapping("list")
	public String list() {
		return "/WEB-INF/views/library/debit/list.jsp";
	}

	@RequestMapping("remove/{id}")
	public String remove(@PathVariable("id") String id,
			@SessionAttribute("debitList") DebitList list) {
		this.libraryService.remove(id, list);
		return "redirect:/lplp_1/library/debit/list";
	}
}
