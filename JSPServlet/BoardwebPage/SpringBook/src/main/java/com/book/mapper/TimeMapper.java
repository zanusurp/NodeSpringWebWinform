package com.book.mapper;

import org.apache.ibatis.annotations.Select;

public interface TimeMapper {
	@Select("select sysdate from dual")
	public String getTime();
	
	public String getTime2(); //��� xml�� ���� Ȯ�ε� ���� 
}
