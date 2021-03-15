package com.board.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baord.service.SampleService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@Log4j
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class SampleServiceTests {
	@Setter(onMethod_=@Autowired)
	private SampleService service;
	
	@Test
	public void testClass() {
		log.info("���񽺸�"+service);
		log.info("���� ���Ӹ�:"+service.getClass().getName());
		
	}
	@Test
	public void testAdd() throws Exception{
		log.info("�ο��� : "+service.doAdd("11", "22"));
	}
//	@Test //�Ⱑ���� ����������
//	public void testAddError() throws Exception{
//		log.info("���� ���Ĺ����� �õ� : "+service.doAdd("123", "ADDD"));
//	}
}
