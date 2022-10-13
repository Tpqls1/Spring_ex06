package org.zerock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mapper.SampleMapper1;
import org.zerock.mapper.SampleMapper2;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class SampleTxServiceImpl implements SampleTxService {
	// 두 개의 Mapper를 주입을 받는다.
	@Setter(onMethod_=@Autowired)
	private SampleMapper1 mapper1;
	
	@Setter(onMethod_=@Autowired)
	private SampleMapper2 mapper2;
	
	// 현재 Transaction으로 묶지 않음
	@Transactional	// 트랜잭션 처리를 하도록 어노테이션을 적용
	@Override
	public void addData(String value) {
		log.info("mapper1...");
		mapper1.insertCol1(value);
		log.info("mapper2...");
		mapper2.insertCol2(value);
		log.info("end...");
	}

}
