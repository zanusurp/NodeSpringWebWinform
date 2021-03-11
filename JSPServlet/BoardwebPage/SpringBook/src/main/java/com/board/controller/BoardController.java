package com.board.controller;

import javax.xml.ws.soap.Addressing;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.baord.service.BoardService;
import com.board.domain.BoardVO;
import com.board.domain.Criteria;
import com.board.domain.PageDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {
	private BoardService service;
	
//	@GetMapping("/list")
//	public void list(Model model) {
//		log.info("Controller list=======================");
//		
//		model.addAttribute("list", service.getList());
//		
//	}
	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		log.info("Control page GetList================================================");
		log.info("list :"+cri);
		model.addAttribute("list",service.getList(cri));
		//model.addAttribute("pageMaker",new PageDTO(cri,123));
		int total = service.getTotal(cri);
		log.info("total : ====="+total);
		model.addAttribute("pageMaker",new PageDTO(cri,total));
		
		
	}
	@GetMapping("/register")
	public void register() {
		
	}
	@PostMapping("/register")
	public String register(BoardVO board, RedirectAttributes rttr) {
		log.info("register =====================" + board);
		service.register(board);
		rttr.addFlashAttribute("result",board.getBno());
		return "redirect:/board/list";
	}
	@GetMapping({"/get","/modify"})
	public void get(@RequestParam("bno") Long bno,@ModelAttribute("cri")Criteria cri ,Model model) {
		log.info("/get or /modify");
		model.addAttribute("board",service.get(bno));
	}
	@PostMapping("/modify")
	public String modify(BoardVO board,@ModelAttribute("cri") Criteria cri ,RedirectAttributes rttr) {
		log.info("modified : =================" + board);
		if(service.modify(board)) {
			rttr.addFlashAttribute("result","modified"+board.getBno());
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount",cri.getAmount());
		return "redirect:/board/list";
	}
	@PostMapping("/remove")
	public String remove(@RequestParam("bno")Long bno,@ModelAttribute("cri") Criteria cri ,RedirectAttributes rttr) {
		log.info("remove : =============="+bno);
		if(service.remove(bno)) {
			rttr.addFlashAttribute("result","removed"+bno);
		}
		rttr.addAttribute("pageNum",cri.getPageNum());
		rttr.addAttribute("amount",cri.getAmount());
		return "redirect:/board/list";
	}
}
