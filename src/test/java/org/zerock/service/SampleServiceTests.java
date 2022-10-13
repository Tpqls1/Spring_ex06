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
public class SampleServiceTests {
	@Setter(onMethod_=@Autowired)
	private SampleService service;
	
//	@Test
	public void testClass() {
		// 주입을 성공적으로 받았는지 확인
		log.info(service);
		// AOP가 적용되었으므로 Proxy 객체가 생성이 되어 동작을 하게 되므로
		// 그 객체 이름을 출력
		log.info(service.getClass().getName());	// Proxy 객체를 출력
	}
	
	// 두수의 합을 출력
	@Test
	public void testAdd() throws Exception {
		log.info(service.doAdd("20", "30"));
	}
	
	// 예외를 발생
//	@Test
	public void testAddError() throws Exception {
		log.info(service.doAdd("20", "AB"));	// NumberFormatException
	}
}
