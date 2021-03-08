package com.board.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.board.domain.BoardVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardMapperTests {
	@Setter(onMethod_= @Autowired)
	private BoardMapper mapper;
	
	@Test
	public void testGetList() {
		mapper.getList().forEach(board -> log.info(board));
	}
	@Test
	public void testInsert() {
		log.info("insert====================================================================");
		BoardVO board = new BoardVO();
		board.setTitle("���� ���� �׽�Ʈ�μ�Ʈ �Դϴ�.");
		board.setContent("���� ���� �׽�Ʈ �μ�Ʈ ���빮�Դϴ�.");
		board.setWriter("insertTester");
		
		mapper.insert(board);
		log.info(board);
		log.info("insert====================================================================");
	}
	@Test
	public void testInsertSelectKey() {
		log.info("selctkey====================================================================");
		BoardVO board = new BoardVO();
		board.setTitle("���� �ۼ��ϴ� �� selectkey");
		board.setContent("���� �ۼ��ϴ� �� ���� �Դϴ� selectKey");
		board.setWriter("newbie");
		mapper.insertSelectKey(board);
		log.info(board);
		log.info("selctkey====================================================================");
	}
	@Test
	public void testRead() {
		log.info("read====================================================================");
		BoardVO board = mapper.read(2L);
		
		log.info(board);
		log.info("read====================================================================");
	}
//	@Test //�̹� ������ �Խ��ǹ�ȣ 
//	public void testDelete() {
//		log.info("delete====================================================================");
//		log.info("delete Board : "+mapper.delete(2L));
//		
//		log.info("delete====================================================================");
//	}
	@Test
	public void testUpdate() {
		log.info("update====================================================================");
		BoardVO board = new BoardVO();
		board.setBno(3L);
		board.setTitle("��ȣ 3�� �Խñ��� ������ �����մϴ�");
		board.setContent("��ȣ 3�� �Խñ��� ������ �����մϴ�");
		board.setWriter("�Խñ� tk");
		
		int count = mapper.update(board);
		log.info("update Confirm : "+ count);
		
		log.info("update====================================================================");
		
	}
}
