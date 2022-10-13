package org.zerock.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller		// Controller로서 동작을 한다고 스프링에게 알린다.
@RequestMapping("/board/*")		// /board 라는 url을 사용한다고 설정
@Log4j
@AllArgsConstructor
public class BoardController {
	// BoardService를 주입 받는다.(생성자로 주입)
	private BoardService service;
	
	// 게시글 목록 보기
//	@GetMapping("/list")	// /board/list로 동작을 하겠다.
//	public void list(Model model) {
//		// Model에 게시글 목록 정보를 실어서 /board/list.jsp에게 전달
//		List<BoardVO> list = service.getList();
//		model.addAttribute("list", list);
//	}	// 뷰페이지 이름 : /board/list.jsp
	
	// 게시글 목록 보기 : 페이징 처리
	// 사용자로부터 몇 페이지를 보여줄지를 입력 받아야 한다.
	/* 페이징 처리를 위하여 고려해야 할 사항
	 * 1. 데이터베이스에서 해당 페이지의 게시글 목록을 가져오기
	 * 2. 뷰 페이지에서 페이징 처리를 위한 화면을 적용
	 * 	- 시작 페이지, 마지막 페이지 : 1~10페이지
	 *  - 이전 버튼, 다음 버튼
	 * 3. 전체 게시글의 수 -> 페이징 처리를 위한 뷰를 보여주는 데이터를 만든다.
	 */
	
	@GetMapping("/list")	// /board/list로 동작을 하겠다.
	public void list(Criteria cri, Model model) {
		log.info("list... " + cri);
		// Model에 게시글 목록 정보를 실어서 /board/list.jsp에게 전달
		List<BoardVO> list = service.getList(cri);
		model.addAttribute("list", list);
		// 페이징 처리를 위한 객체 pageDTO 정보를 전달해 주어야 한다.
		// 데이터베이스에서 전체 데이터의 수를 검색해서 가져와야 한다.
		int total = service.getTotalCount(cri);
		model.addAttribute("pageMaker", new PageDTO(cri, total)); // 뷰 페이지로 전달
	}	// 뷰페이지 이름 : /board/list.jsp
	
	// 게시글 쓰기
	// 게시글 폼 요청에 대하여 처리 -> JSP 작성 시 코딩
	@GetMapping("/register")
	public void register() {
		log.info("register...");
	}	// board/register.jsp
	
	// 게시글 폼에서 올라오는 게시글 쓰기에 대하여 데이터베이스에 저장
	@PostMapping("/register")
	public String register(BoardVO board, RedirectAttributes rttr) {
		log.info("================================");
		log.info("register: " + board);	// 디버깅 목적으로 출력
		if(board.getAttachList() != null) {
			board.getAttachList().forEach(attach -> log.info(attach));
		}
		log.info("================================");
		// 운영을 할 때면 출력문을 제거해야 한다. -> 출력문은 I/O operation을 하므로
		// 성능에 많은 부하를 주게된다.
		// 브라우저로부터 수신한 게시글 정보를 이용해서 데이터베이스에 저장
		service.register(board);
		// 게시글 목록 보기 리다이렉트 -> 게시글 번호를 RedirectAttribute에 실어
		rttr.addFlashAttribute("result", board.getBno());
		// /board/list.jsp에서 게시글 번호를 사용자에게 모달창을 통해 알린다.
		// 한번만 파라미터를 전달하기 위해 addFlashAttribute를 사용
		return "redirect:/board/list";	// 리다이렉트시에는 url 반환
//		return "/board/success";		// /board/success.jsp
	}
	
	// 게시글 상세보기 : /board/get : bno
	// 브라우저로 올라온 파라미터를 다시 브라우저로 전달하기 위해 @RequestParam를 사용
	// 뷰페이지로 데이터를 전달하기 위해 Model에 실어준다.
	// Criteria를 수신하고 뷰 페이지로 전달을 하도록 추가 -> @ModelAttribute 어노테이션을 사용
	@GetMapping({"/get", "/modify"})
	public void get(@RequestParam("bno") Integer bno, @ModelAttribute("cri") Criteria cri, Model model) {
		log.info("get..." + bno);
		BoardVO board = service.get(bno);
		model.addAttribute("board", board);
	}	// 뷰 페이지 이름 : get->/board/get.jsp, modify->/board/modify.jsp
	
	// 게시글 수정 : /board/modify : BoardVO
	// Criteria를 브라우저로부터 수신하고 뷰페이지로 전달을 해주도록 추가 -> @ModelAttribute를 사용
	@PostMapping("/modify")
	public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {		
		log.info("modify..." + board);
		// 브라우저로부터 수정된 게시글 정보가 올라온다.
		// 게시글 정보를 업데이트(데이터베이스) -> 반환값 true:성공, false:실패
		if(service.modify(board)) {
			// 게시글 수정이 성공되었는지 결과를 실어준다. -> 모달창을 띄워준다.
			// /board/list에서 모달창을 띄운다.
			rttr.addFlashAttribute("result", "success");
		}
		// /board /list로 redirect 하므로 pageNum, amount를 가지고 다니도록 추가
		
//		rttr.addAttribute("pageNum", cri.getPageNum());
//		rttr.addAttribute("amount", cri.getAmount());
//		rttr.addAttribute("type", cri.getType());
//		rttr.addAttribute("keyword", cri.getKeyword());
		// board/list로 리다이렉트
		return "redirect:/board/list" + cri.getListLink();
	}
	
	// 게시글 삭제 : /board/remove : bno
	// 리다이렉트 /board/list
	// RedirectAttribute(모달창)에 성공여부를 전송
	// 브라우저로부터 전달된 Criteria를 수신하고
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Integer bno, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		log.info("remove... " + bno);
		List<BoardAttachVO> attachList = service.getAttachList(bno);
		if(service.remove(bno)) {
			// 첨부파일을 삭제
			deleteFiles(attachList);
			rttr.addFlashAttribute("result", "success");
		}
		// /board/list로 redirect 하므로 pageNum, amount를 가지고 다니도록 추가
//		rttr.addAttribute("pageNum", cri.getPageNum());
//		rttr.addAttribute("amount", cri.getAmount());
//		rttr.addAttribute("type", cri.getType());
//		rttr.addAttribute("keyword", cri.getKeyword());
		return "redirect:/board/list" + cri.getListLink();
	}
	
	// 삭제 작업을 controller에서 해야 하는가? -> 여기서는 controller에서 수행
	// 아니면 service layer에서 해야 하는가? -> service layer에서 하는것이 어떤가?
	private void deleteFiles(List<BoardAttachVO> attachList) {
		if(attachList == null || attachList.size() == 0) {
		      return;
		}
		    
		log.info("delete attach files...................");
		log.info(attachList);
		    
		attachList.forEach(attach -> {
		try {
			Path file  = Paths.get("/Users/tpqls/upload/"+attach.getUploadPath()+"/" + attach.getUuid()+"_"+ attach.getFileName());
	        Files.deleteIfExists(file);
	        if(Files.probeContentType(file).startsWith("image")) {
	          Path thumbNail = Paths.get("/Users/tpqls/upload/"+attach.getUploadPath()+"/s_" + attach.getUuid()+"_"+ attach.getFileName());
	          Files.delete(thumbNail);
	        }
	      }catch(Exception e) {
	        log.error("delete file error" + e.getMessage());
	      }//end catch
	    });//end foreachd
	}

	// 첨부파일목록에 대한 정보를 Ajax로 요청할 때 처리
	@GetMapping(value="/getAttachList", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Integer bno) {
		log.info("getAttachList " + bno);
		return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);
	}
}
