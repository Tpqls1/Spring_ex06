package org.zerock.mapper;

import java.util.List;
import org.zerock.domain.BoardAttachVO;

public interface BoardAttachMapper {
	public void insert(BoardAttachVO vo);
	public int delete(String uuid);
	public List<BoardAttachVO> findByBno(Integer bno);
	// 특정 게시글의 첨부파일을 삭제
	public void deleteAll(Integer bno);
	// 전일의 첨부파일 목록을 가져온다.
	public List<BoardAttachVO> getOldFiles();
}	// 파일에서는 update = delete -> insert