package org.zerock.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

// 로깅을 하는 클래스
@Aspect		// Advice 임을 스프링에게 알림
@Log4j
@Component	// 주입받는 빈이라는 것을 알림
public class LogAdvice {
	// pointcut을 정의 : SampleService로 시작하는 모든 클래스의 모든 메소드에 적용하겠다.
	// 첫번째 * : 접근 제한자
	// 두번째 * : wildcard(SampleService로 시작하는 모든 클래스)
	// 세번째 * : 모든 메소드
	// Before Advice를 적용 -> target의 메소드가 실행되기 전에 수행된다.
	@Before("execution(* org.zerock.service.SampleService*.*(..))")
	public void logBefore() {
		log.info("=================================");
	}
	
	// args : target의 파라미터에 접근하기 위해서 사용 -> 매개변수의 유형을 알고 있을 때 적용
	@Before("execution(* org.zerock.service.SampleService*.doAdd(String,String)) && args(str1,str2)")
	public void logBeforeWithParam(String str1, String str2) {	// target 매개변수를 지칭
		log.info("str1: " + str1);
		log.info("str2: " + str2);
	}
	
	// 예외가 발생되면 실행되는 Advice
	@AfterThrowing(pointcut="execution(* org.zerock.service.SampleService*.*(..))", throwing="exception")
	public void logException(Exception exception) {	// 예외정보가 실린다.
		log.info("Exception...!!!");
		log.info("exception: " + exception);
	}
	
	@Around("execution(* org.zerock.service.SampleService*.*(..))")
	public Object logTime(ProceedingJoinPoint pjp) {	// target joint point 정보
		long start = System.currentTimeMillis();	// 시작시간
		log.info("Target: " + pjp.getTarget());
		log.info("Param: " + Arrays.toString(pjp.getArgs()));
		Object result = null;
		try {
			result = pjp.proceed();	// target 메소드를 호출
		} catch(Throwable e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		log.info("경과시간: " + (end - start));
		return result;
	}
}
