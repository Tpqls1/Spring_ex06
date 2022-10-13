package org.zerock.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class SampleTxServiceTests {
	// SampleTxService를 주입 받는다.
	@Setter(onMethod_=@Autowired)
	private SampleTxService service;
	
	@Test
	public void testLong() {
		String str = "abcdefghijklmnopqrstuvwxyz"
				+ " abcdefghijklmnopqrstuvwxyz";	// 52자
		// 1개의 테이블은 성공, 1개의 테이블은 저장이 안되게
		log.info("문자열의 길이= " + str.getBytes().length);
		service.addData(str);
	}
}
