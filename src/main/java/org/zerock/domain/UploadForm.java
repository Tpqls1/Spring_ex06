package org.zerock.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

// 파일 업로드가 포함된 정보를 저장하는 빈
@Data
public class UploadForm {
	private String desc;				// 
	private MultipartFile[] uploadFile;	// 파일에 대한 정보
}
