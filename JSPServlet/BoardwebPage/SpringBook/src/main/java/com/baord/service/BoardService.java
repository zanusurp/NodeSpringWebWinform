package com.baord.service;

import java.util.List;

import com.board.domain.BoardAttachVO;
import com.board.domain.BoardVO;
import com.board.domain.Criteria;

public interface BoardService {
	public void register(BoardVO board);
	
	public BoardVO get(Long bno);
	
	public boolean modify(BoardVO board);
	
	public boolean remove(Long bno);
	
	//public List<BoardVO> getList();
	public List<BoardVO> getList(Criteria cri);//����¡ ÷��
	
	public List<BoardAttachVO> getAttachList(Long bno);
	
	public int getTotal(Criteria cri);
}
