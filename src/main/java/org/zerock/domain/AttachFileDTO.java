package org.zerock.domain;

import lombok.Data;

// 첨부파일에 대한 정보를 저장하는 빈
@Data
public class AttachFileDTO {
	private String fileName;	// 업로드한 파일의 이름
	private String uploadPath;	// 저장된 폴더명 : 년월일(2022/10/11)
	private String uuid;
	private boolean image;		// 이미지 여부->
	// 이미지 파일일 경우는 섬네일을 보여준다.
	// 일반 파일인 경우는 default 이미지를 보여주고, 첨부파일의 이름을 출력
}
