package org.zerock.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

// 스프링환경을 흉내주어야 한다.
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardServiceTests {
	// BoardService를 주입 받는다.
	@Setter(onMethod_=@Autowired)
	private BoardService service;
	
	// BoardService 구현객체를 주입받았는지를 테스트
//	@Test
	public void testExists() {
		log.info(service);
		assertNotNull(service);	// service 객체가 주입받지 못하면 fail
		// assertNotNull : service가 not null이면 성공
	}
	
//	@Test
	public void testRegister() {
		// 게시글 생성
		BoardVO board = new BoardVO();
		board.setTitle("서비스에서 게시글 추가 제목");
		board.setContent("서비스에서 게시글 추가 내용");
		board.setWriter("user0");
		service.register(board);
		log.info("생성된 게시글의 번호 : " + board.getBno());
	}
	
//	@Test
	public void testGetList() {
		Criteria cri = new Criteria(1, 10);
		List<BoardVO> list = service.getList(cri);
		list.forEach(board -> log.info(board));	// 람다식을 이용
	}
	
//	@Test
	public void testRead() {
		BoardVO board = service.get(13);	// 있는 게시글 번호를 사용
		log.info(board);
	}
	
	@Test
	public void testModify() {
		// 수정할 게시글 (존재하는 게시글)
		BoardVO board = new BoardVO();
		board.setBno(1);
		board.setTitle("수정된 게시글 제목");
		board.setContent("수정된 게시글 내용");
		log.info("변경 결과 : " + service.modify(board));
	}
	
	@Test
	public void testRemove() {
		// 삭제할 게시글 아이디 (존재하는 게시글)
		Integer bno = 11;
		log.info("삭제 결과 : " + service.remove(bno));
	}
}
