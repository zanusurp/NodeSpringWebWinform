package com.board.mapper;

import org.apache.ibatis.annotations.Insert;

public interface SampleMapper2 {

	@Insert("insert into tbl_sample2 (col2) values(#{data})")
	public int insertCol1(String data);
}
