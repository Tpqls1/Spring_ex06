package org.zerock.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

// 마치 스프링이 동작을 하면서 브라우저에서 request 요청이 오는 것을 흉내내주어야 한다.
// Junt에서
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration // controller를 테스트할 때 사용 (브라우저환경을 제공)
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@Log4j
public class BoardControllerTests {
	// WebApplicationContext 객체를 주입받는다.
	@Setter(onMethod_ = @Autowired)
	private WebApplicationContext ctx;

	private MockMvc mockMvc; // 브라우저 역할을 하는 클래스

	// Test가 실행되기 전에 수행된다. -> 초기화
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	// BoardService를 주입 받는다.

//	@Test
	public void testList() throws Exception {
		log.info(mockMvc.perform(MockMvcRequestBuilders.get("/board/list")).andReturn().getModelAndView() // List<Board>,
																											// /board/list.jsp
				.getModelMap() // List<Board>
		);
	}

//	@Test
	public void testRegister() throws Exception {
		// 리다리렉트가 되는지 확인
		String resultPage = mockMvc
				.perform(MockMvcRequestBuilders.post("/board/register").param("title", "테스트 새글 제목")
						.param("content", "테스트 새글 내용").param("writer", "user1"))
				.andReturn().getModelAndView().getViewName();
		log.info(resultPage);
	}

//	@Test
	public void testGet() throws Exception {
		log.info(mockMvc.perform(MockMvcRequestBuilders.get("/board/get").param("bno", "3")).andReturn()
				.getModelAndView() // Board, /board/get.jsp
				.getModelMap() // Board
		);
	}

//	@Test
	public void testModify() throws Exception {
		// 리다리렉트가 되는지 확인
		// 수정할 때 : 특정 게시글 bno, 수정 내용 : title, content
		String resultPage = mockMvc
				.perform(MockMvcRequestBuilders.post("/board/modify").param("title", "수정된 테스트 새글 제목")
						.param("content", "수정된 테스트 새글 내용").param("bno", "3"))
				.andReturn().getModelAndView().getViewName();
		log.info(resultPage);
	}

//	@Test
	public void testRemove() throws Exception {
		// 리다리렉트가 되는지 확인
		// 수정할 때 : 특정 게시글 bno, 수정 내용 : title, content
		String resultPage = mockMvc
				.perform(MockMvcRequestBuilders.post("/board/remove").param("bno", "4"))
				.andReturn().getModelAndView().getViewName();
		log.info(resultPage);
	}
	
	// 페이징 처리 : pageNum, amount
	@Test
	public void testListWithPaging() throws Exception {
		log.info(mockMvc.perform(MockMvcRequestBuilders.get("/board/list")
				.param("pageNum", "1")
				.param("amount", "10")
				).andReturn().getModelAndView() // List<Board>,																						// /board/list.jsp
				.getModelMap() // List<Board>
		);
	}
}
