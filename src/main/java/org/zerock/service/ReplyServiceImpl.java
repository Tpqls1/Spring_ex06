package org.zerock.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.ReplyMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReplyServiceImpl implements ReplyService {
	// RePlyMapper를 주입 받는다. -> Setter, 생성자
	@Setter(onMethod_=@Autowired)
	private ReplyMapper mapper;

	// BoardMapper -> 댓글 추가/삭제 -> 게시글의 댓글 수를 수정
	@Setter(onMethod_=@Autowired)
	private BoardMapper boardMapper;

	// tbl_reply과 tbl_board에 대하여 데이터 일치를 위해 트랜잭션 처리를 한다.
	@Transactional
	@Override
	public int register(ReplyVO vo) {
		log.info("register..." + vo);
		// 게시글의 댓글 수를 increment해준다. -> Transaction
		boardMapper.updateReplyCnt(vo.getBno(), 1);	// 댓글의 수를 증가
		return mapper.insert(vo);
	}

	@Override
	public ReplyVO get(Integer rno) {
		log.info("get..." + rno);
		return mapper.read(rno);
	}

	@Override
	public int modify(ReplyVO vo) {
		log.info("modify... " + vo);
		return mapper.update(vo);
	}

	@Transactional	// 트랜잭션 처리
	@Override
	public int remove(Integer rno) {
		log.info("remove..." + rno);
		// 댓글의 아이디를 사용해서 게시글 정보를 가져온다.
		ReplyVO vo = mapper.read(rno);	// 댓글에 게시글 아이디가 있음
		// 게시글의 댓글 수를 하나 감소시켜준다.
		boardMapper.updateReplyCnt(vo.getBno(), -1);	// 댓글의 수를 감소
		int result = mapper.delete(rno);
		return result;
	}

	// 반환형 : 페이징 처리를 위한 정보를 반환
	// 댓글 목록 + 댓글의 수 -> 새로운 클래스를 추가 ReplyPageDTO
	@Override
	public ReplyPageDTO getList(Criteria cri, Integer bno) {
		log.info("getList..." + cri + ", bno=" + bno);
		// 브라우저의 javascript와 동작상의 어떤 이유에 의하여 수정
		if(cri.getPageNum() == -1) {	// pageNum=-1 : 마지막 페이지
			// 데이터베이스에서 댓글의 수만 반환하고, 댓글의 목록은 검색하지 않는다.
			return new ReplyPageDTO(
					mapper.getCountByBno(bno),
					new ArrayList<ReplyVO>());	// 빈 목록을 반환
		} else {	// 일반 페이지를 요청할 때
			return new ReplyPageDTO(
					mapper.getCountByBno(bno),
					mapper.getListWithPaging(cri, bno)
			);
		}
	}
}