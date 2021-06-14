package com.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/sample/*")
@Controller
public class SampleSecurityController {
	@GetMapping("/allSecurity")
	public String allSecurity() {
		log.info("�ƹ��� ���� �� �� �ֽ��ϴ�.");
		return "sample/allSecurity";
		
	}
	@GetMapping("/memberSecurity")
	public String memberSecurity() {
		log.info("�α��� ����ڸ� ���� �� �ֽ��ϴ�.");
		return "sample/memberSecurity";
	}
	
	@GetMapping("/adminSecurity")
	public void adminSecurity() {
		log.info("�����ڸ� ���� �� �ֽ��ϴ�.");
	}
	
}
