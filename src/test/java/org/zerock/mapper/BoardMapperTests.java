package org.zerock.mapper;

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

// junut를 사용해서 테스트 : Mapper를 테스트
// Connection Pool
// DataSource -> Connection
// Spring에 설정한 환경이 필요 -> tomcat이 구동되고, 스프링 라이브러리가
// 동작하도록 환경을 인위적으로 꾸며져야 한다.
// 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardMapperTests {
	// 스프링이 구현 객체를 자동으로 생성시켜주고 주입을 해준다.
	@Setter(onMethod_=@Autowired)	// 다형성 : 인터페이스 = 구현 객체를 대입
	private BoardMapper mapper;	// 인터페이스를 정의 -> 구현 객체를 주입받는다.
	
//	@Test
	public void testGetList() {
		mapper.getList().forEach(board -> log.info(board));
	}
	
//	@Test
	public void testInsert() {
		// 게시글을 추가
		// 테스트를 위한 준비
		BoardVO board = new BoardVO();
		board.setTitle("새로 작성하는 글");
		board.setContent("새로 작성하는 내용");
		board.setWriter("newbie");
		mapper.insert(board);	// 테스트를 하는 코드
		log.info(board);
	}
	
//	@Test
	public void testInsertSelectKey() {
		// 게시글을 추가
		BoardVO board = new BoardVO();
		board.setTitle("새로 작성하는 글");
		board.setContent("새로 작성하는 내용");
		board.setWriter("newbie");
		mapper.insertSelectKey(board);
		// 첨부 파일을 추가할 때 bno를 사용
		log.info(board);
	}
	
//	@Test
	public void testRead() {
		// 게시글 상세 보기
		Integer bno = 12;	// 있는 게시글 번호를 사용
		BoardVO board = mapper.read(bno);
		log.info(board);
	}
	
//	@Test
	public void testUpdate() {
		// 수정된 게시글 정보
		BoardVO board = new BoardVO();
		board.setBno(12);
		board.setTitle("수정된 게시글 제목");
		board.setContent("수정된 게시글 내용");
		int result = mapper.update(board);
		log.info("변경된 게시글의 수 : " + result);	// 수정된 열의 수가 반환된다.
	}
	
//	@Test
	public void testDelete() {
		// 삭제할 게시글의 아이디
		Integer bno = 12;
		int result = mapper.delete(bno);
		log.info("삭제된 게시글의 수 : " + result);
	}
	
//	@Test
	public void testGetListWithPaging() {
		Criteria cri = new Criteria(2, 10);	// 2페이지의 10개의 게시글 가져오도록
		mapper.getListWithPaging(cri).forEach(board -> log.info(board));
	}
	
	// 검색 기능을 테스트
	@Test
	public void testSearch() {
		Criteria cri = new Criteria();	// 1, 10
		cri.setType("T");	// 제목, 내용, 작성자
		cri.setKeyword("수정된");	// 검색 가능한 키워드를 사용
		
		List<BoardVO> list = mapper.getListWithPaging(cri);
		list.forEach(board -> log.info(board));
	}
}
