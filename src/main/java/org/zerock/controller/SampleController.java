package org.zerock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.SampleVO;
import org.zerock.domain.Ticket;

import lombok.extern.log4j.Log4j;

@RestController		// Rest 방식의 controller : Spring 4에서 추가
@RequestMapping("/sample")
@Log4j
public class SampleController {
	// value : url
	// produces : response에 대한 content-type을 지정해 준다.
	// text/plain : 텍스트 문자열
	@GetMapping(value="/getText", produces="text/plain; charset=UTF-8")
	public String getText() {
		log.info("MIME TYPE: " + MediaType.TEXT_PLAIN_VALUE);
		return "안녕하세요";
	}
	
	// produces : content-type = application/json, application/xml 
	@GetMapping(value="/getSample", produces= {MediaType.APPLICATION_JSON_UTF8_VALUE,
			MediaType.APPLICATION_XML_VALUE})
	public SampleVO getSample() {
		return new SampleVO(112, "스타", "로드");
	}
	
	// List<SampleVO> : JSON, XML
	@GetMapping("/getList")	// default : JSON
	public List<SampleVO> getList() {
		return IntStream.range(1, 10).mapToObj(i -> new SampleVO(i,
			i + "First", i + "Last")).collect(Collectors.toList());
	}
	
	// Map 형태로 응답
	// 어떤 객체 형태던지 응답할 수 있다.
	@GetMapping("/getMap")
	public Map<String, SampleVO> getMap() {
		Map<String, SampleVO> map = new HashMap<String, SampleVO>();
		map.put("First", new SampleVO(111, "길동", "홍"));
		map.put("Second", new SampleVO(222, "수환", "홍"));
		return map;
	}
	
	// ResponseEntity : 데이터 + 상태(코드)를 응답
	// params : 기존에 Controller에서 사용하는 URL 체계
	// 브라우저에서 전송되는 URL /sample/check?height=168&weight=68을 parsing
	// height, weight -> 매개변수로 읽어 온다.
	// 기존 URL에 대한 호환성을 제공
	@GetMapping(value="/check", params= {"height", "weight"})
	public ResponseEntity<SampleVO> check(Double height, Double weight) {
		SampleVO vo = new SampleVO(000, "" + height, "" + weight);
		// 비만도 측정 : height(키) > 150
		// 데이터 : SampleVO
		// 상태코드 : HttpStatus.BAD_GATEWAY(400), HttpStatus.OK(200)
		ResponseEntity<SampleVO> result = null;
		if(height < 150) {	// 비만도를 측정할 수 키를 가진 사람일 경우
			result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
		} else {
			result = ResponseEntity.status(HttpStatus.OK).body(vo);
		}
		return result;
	}
	
	// URL 체계에서 '{var}' : 파라미터로 입력값이 올라오는 변수
	// {}없이 var : 상수 -> 기능(카테고리)
	@GetMapping("/product/{cat}/{pid}")
	public String[] getPath(@PathVariable("cat") String cat, @PathVariable("pid") Integer pid) {
		return new String[] { "category:" + cat, "productId:" + pid };
	}
	
	// @RequestBody : client로부터 JSON 형태로 데이터를 수신
	@PostMapping("/ticket")
	public Ticket convert(@RequestBody Ticket ticket) {
		log.info("convert... ticket: " + ticket);
		return ticket;	// JSON 형태로 응답
	}
}
