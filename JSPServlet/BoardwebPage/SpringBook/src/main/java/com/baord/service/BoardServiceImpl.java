package com.baord.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.domain.BoardVO;
import com.board.domain.Criteria;
import com.board.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@AllArgsConstructor
public class BoardServiceImpl implements BoardService{

	@Setter(onMethod_=@Autowired)//4���� ���ķ� �ڵ�ó�� �ȴٰ�� ������ �������
	private BoardMapper mapper;
	
	@Override
	public void register(BoardVO board) {
		log.info("==============register �� �ֱ� : "+board);
		mapper.insertSelectKey(board);
		
	}

	@Override
	public BoardVO get(Long bno) {
		log.info("============get �о�� �Խñ� ��ȣ : "+bno);
		return mapper.read(bno);
	}

	@Override
	public boolean modify(BoardVO board) {
		log.info("===========modify �Խñ� ���� : "+board);
		return mapper.update(board) == 1;
	}

	@Override
	public boolean remove(Long bno) {
		log.info("===========remove �Խñ� ����  : "+bno);
		return mapper.delete(bno) == 1;
	}

//	@Override //����¡ ���� ��
//	public List<BoardVO> getList() {
//		log.info("================getList�Խñ� ��ü ��� �о����  ");
//		return mapper.getList();
//		
//	}

	@Override
	public List<BoardVO> getList(Criteria cri) { //����¡ �ִ� ��
		log.info("===========getList����¡ �Խñ� ���: "+cri.getPageNum()+"�������� : "+cri.getAmount());
		
		return mapper.getListWithPaging(cri);
	}

	@Override
	public int getTotal(Criteria cri) {
		log.info("get Total Count ==========================================");
		return mapper.getTotalCount(cri);
	}

}
