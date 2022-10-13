package org.zerock.service;

import org.springframework.stereotype.Service;

@Service
public class SampleServiceImpl implements SampleService {
	// str1, str2가 숫자 문자열
	// str1, str2가 숫자가 아닌 문자열일 경우 예외가 발생할 수 있음
	@Override
	public Integer doAdd(String str1, String str2) throws Exception {
		return Integer.parseInt(str1) + Integer.parseInt(str2);
	}

}
