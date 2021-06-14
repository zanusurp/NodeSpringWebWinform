package com.board.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller
public class CommonController {
	@GetMapping("/accessError")
	public void accessError(Authentication auth, Model model) {
		log.info("============== ���� ���� : "+auth);
		model.addAttribute("msg","���� ����  : ���� �� �� �����ϴ�.");

	}
	@GetMapping("/customLogin")
	public void loginInput(String error, String logout, Model model) {
		log.info("�α� â ==========================");
		log.info("error ���� ========: "+error);
		log.info("�α� �ƿ� =================="+logout);
		
		if(error!=null) {
			model.addAttribute("error","���� Ȯ�� �ϼ��� �α��� ���� ");
		}
		if(logout != null) {
			model.addAttribute("logout","�α׾ƿ�!");
		}
	}
	@GetMapping("/customLogout")
	public void logoutGet() {
		log.info("�ڸ� ��Ʈ�ѷ� = ===============================custom logout");
	}
}