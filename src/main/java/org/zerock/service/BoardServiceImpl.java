package org.zerock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service	// Controller에 주입을 해주어야 하므로 주입 대상이 된다고 알려주어야 한다.
@Log4j
@AllArgsConstructor	// 필드에 대한 생성자를 자동으로 추가한다.
public class BoardServiceImpl implements BoardService {
	// BoardMapper를 사용해서 데이터베이스 연동을 해야 한다.
	// BoardMapper를 주입을 받아야 한다. -> 생성자에 의하여 주입
	private BoardMapper mapper;
	private BoardAttachMapper attachMapper;
	
	@Override
	public List<BoardVO> getList(Criteria cri) {	// 페이징 처리
		log.info("getListWithPaging..." + cri);
		return mapper.getListWithPaging(cri);
	}

	@Transactional
	@Override
	public void register(BoardVO board) {
		log.info("register..." + board);	// 로그를 남김
		mapper.insertSelectKey(board);	// 확장성을 위해 : 댓글, 첨부 파일
		// 첨부파일에 대한 정보를 데이터베이스에 추가
		if(board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}
		// 첨부 파일 정보를 데이터베이스에 저장
		board.getAttachList().forEach(attach -> {
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
	}

	@Override
	public BoardVO get(Integer bno) {
		log.info("get... " + bno);
		return mapper.read(bno);
	}
	//	첨부파일에 대하여 삭제하지 않음 -> 배치작업으로 진행 
	//  삭제도 가능 -> 검토가 필요
	//	기존 데이터베이스의 첨부파일 정보에서 올라온 첨부파일 정보를 빼면 삭제된 첨부파일 정보가 되고
	//	차집합을 삭제하면 될 것 같다.(검토필요)
	@Transactional	//	트랜잭션 처리 
	@Override
	public boolean modify(BoardVO board) {
		log.info("modify... " + board);
		// 첨부파일 정보를 변경 : 데이터베이스, 파일 
		// 데이터베이스 : 기존 데이터를 모두 삭제하고 브라우저에서 올라온 데이터를 추가 
		// int -> boolean으로 변환이 필요
		attachMapper.deleteAll(board.getBno()); // 게시글의 모든 첨부파일 정보 삭제
		boolean modifyResult = mapper.update(board) == 1;
		if(modifyResult && board.getAttachList() != null && board.getAttachList().size() > 0) {
			board.getAttachList().forEach(attach -> { 
				attach.setBno(board.getBno()); 
				attachMapper.insert(attach);
			}); 
		}
			return modifyResult;
	}

	@Transactional
	@Override
	public boolean remove(Integer bno) {
		log.info("remove... " + bno);
		// 첨부 파일을 삭제 : 데이터베이스
		attachMapper.deleteAll(bno);
		return mapper.delete(bno) > 0;		// 삭제 여부를 반환
	}

	@Override
	public int getTotalCount(Criteria cri) {
		log.info("getTotalCount..." + cri);
		return mapper.getTotalCount(cri);
	}

	@Override
	public List<BoardAttachVO> getAttachList(Integer bno) {
		log.info("get Attach list by bno " + bno);
		return attachMapper.findByBno(bno);
	}

}
