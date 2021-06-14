package com.board.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.baord.service.BoardService;
import com.board.domain.BoardAttachVO;
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
	private void deleteFiles(List<BoardAttachVO> attachList) {
		log.info("deleteFiles ����===============================================");
		if(attachList == null|| attachList.size() == 0) {
			log.info("DeleteFiles ���� ���� �� ���� ����");
			return;
		}
		log.info("DeleteFiles ���� ���� �� ���� ����");
		log.info("������ ���� ��� : "+attachList);
		attachList.forEach(attach -> {
			try {
				Path file = Paths.get("C:\\upload\\"+attach.getUploadPath()+"\\"+attach.getUuid()+"_"+attach.getFileName());
				log.info("�� ���� ��� Ȯ�� : " + file);
				Files.deleteIfExists(file);
				if(Files.probeContentType(file).startsWith("image")) {
					log.info("�̹��� ���Ϸ� Ȯ�� ���� �� =======================================");
					Path thumbNail = Paths.get("c:\\upload\\"+attach.getUploadPath()+"\\s_"+attach.getUuid()+"_"+attach.getFileName());
					log.info("������ ���� �̸� : "+ thumbNail);
					Files.delete(thumbNail);
				}
			} catch (Exception e) {
				log.error("���� ���� ����  : "+e.getMessage());
			}
		});
	}
	
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
		if(board.getAttachList() != null) {
			board.getAttachList().forEach(attach -> log.info("���� ��� : "+attach));
		}
		log.info("================================");
		service.register(board);
		rttr.addFlashAttribute("result",board.getBno());
		return "redirect:/board/list";
	}
	@GetMapping({"/get","/modify"})
	public void get(@RequestParam("bno") Long bno,@ModelAttribute("cri")Criteria cri ,Model model) {
		log.info("/get or /modify");
		model.addAttribute("board",service.get(bno));
	}
	//������ ajax�� ���� ����
	@GetMapping(value = "/getAttachList", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
		log.info("�Խñ��� ÷�� ���� ����Ʈ  :"+ bno);
		return new ResponseEntity<>(service.getAttachList(bno),HttpStatus.OK);
	}
	
	@PostMapping("/modify")
	public String modify(BoardVO board,@ModelAttribute("cri") Criteria cri ,RedirectAttributes rttr) {
		log.info("modified : =================" + board);
		if(service.modify(board)) {
			rttr.addFlashAttribute("result","modified"+board.getBno());
		}
//		rttr.addAttribute("pageNum", cri.getPageNum());
//		rttr.addAttribute("amount",cri.getAmount());
//		rttr.addAttribute("type",cri.getType());
//		rttr.addAttribute("keyword",cri.getKeyword());
		return "redirect:/board/list"+cri.getListLink();
	}
	@PostMapping("/remove")
	public String remove(@RequestParam("bno")Long bno,@ModelAttribute("cri") Criteria cri ,RedirectAttributes rttr) {
		log.info("remove : =============="+bno);
		List<BoardAttachVO> attachList = service.getAttachList(bno);
		if(service.remove(bno)) {
			deleteFiles(attachList);
			rttr.addFlashAttribute("result","removed"+bno);
		}
		rttr.addAttribute("pageNum",cri.getPageNum());
		rttr.addAttribute("amount",cri.getAmount());
		return "redirect:/board/list"+cri.getListLink();
	}
}
