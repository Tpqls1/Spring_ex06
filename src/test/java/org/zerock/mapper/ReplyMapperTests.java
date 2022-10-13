package org.zerock.mapper;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

// junut를 사용해서 테스트 : Mapper를 테스트
// Connection Pool
// DataSource -> Connection
// Spring에 설정한 환경이 필요 -> tomcat이 구동되고, 스프링 라이브러리가
// 동작하도록 환경을 인위적으로 꾸며져야 한다.
// 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyMapperTests {
	// 스프링이 구현 객체를 자동으로 생성시켜주고 주입을 해준다.
	@Setter(onMethod_=@Autowired)	// 다형성 : 인터페이스 = 구현 객체를 대입
	private ReplyMapper mapper;	// 인터페이스를 정의 -> 구현 객체를 주입받는다.
	
	// 게시글 번호 : 1952~1948 (여러분의 게시글 번호를 사용)
	private Integer[] bnoArr = { 1652, 1651, 1650, 1649, 1648 };
	
	// ReplyMapper 구현객체가 제대로 주입이 되는지 테스트 : AOP로 동작하는 proxy 객체
//	@Test
	public void testMapper() {
		log.info(mapper);
	}
	
//	@Test
	public void testCreate() {
		IntStream.rangeClosed(1, 10).forEach(i -> {
			ReplyVO vo = new ReplyVO();
			vo.setBno(bnoArr[i % 5]);
			vo.setReply("댓글 테스트 " + i);
			vo.setReplyer("replyer" + i);
			mapper.insert(vo);
		}); 
	}

//	@Test
	public void testRead() {
		Integer rno = 2;	// 데이터베이스에 존재하는 rno를 사용한다.
		ReplyVO vo = mapper.read(rno);
		log.info(vo);
	}
	
//	@Test
	public void testUpdate() {
		ReplyVO reply = new ReplyVO();
		reply.setRno(2);
		reply.setReply("수정 댓글");
		
		int result = mapper.update(reply);
		log.info("수정 결과 : " + result);
	}
	
//	@Test
	public void testDelete() {
		Integer rno = 2;
		int result = mapper.delete(rno);
		log.info("삭제 결과 : " + result);
	}
	
//	@Test
	public void testGetListWithPaging() {
		// bno
		Criteria cri = new Criteria();	// 1페이지, 10개
		List<ReplyVO> list = mapper.getListWithPaging(cri, bnoArr[0]);
		list.forEach(reply -> log.info(reply));
	}
	
	@Test
	public void testList2() {
		Criteria cri = new Criteria(1, 10);		// 충분한 수의 댓글을 추가하고 시험한다.
		List<ReplyVO> replies = mapper.getListWithPaging(cri, 1652);	// bno = 30
		replies.forEach(reply -> log.info(reply));
	}

}
