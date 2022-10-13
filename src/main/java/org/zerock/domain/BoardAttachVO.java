package org.zerock.domain;

import lombok.Data;

// 첨부 파일에 대한 정보를 저장하는 빈
@Data
public class BoardAttachVO {
	private String uuid;
	private String uploadPath;
	private String fileName;
	private boolean fileType;
	private Integer bno;
}
