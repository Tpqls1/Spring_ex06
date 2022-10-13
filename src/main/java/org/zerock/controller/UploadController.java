package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.net.URLEncoder;
import java.net.URLDecoder;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;
import org.zerock.domain.UploadForm;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

// 파일 업로드 기능을 처리하는 컨트롤러
@Controller
@Log4j
public class UploadController {
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload form");
	}
	
	@PostMapping("/uploadFormAction")
	public void uploadFormAction(UploadForm form, Model model) {
		log.info("desc = " + form.getDesc());
		String uploadFolder = "/Users/tpqls/upload";	// 절대경로를 사용해서 저장
		// 파일의 정보만 출력 -> 저장은 아직 하지 않음
		for(MultipartFile multipartFile : form.getUploadFile()) {
			log.info("Upload file name: " + multipartFile.getOriginalFilename());
			log.info("upload file size: " + multipartFile.getSize());
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			try {
				multipartFile.transferTo(saveFile);	// 파일 저장
			} catch(Exception e) {
				log.error(e.getMessage());
			}
		}
	}
	
	// Ajax를 사용해서 파일 업로드
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("uploadAjax...");
	}
	
	// 반환형 : 파일정보를 가지는 AttachFileDTO
	@PostMapping(value="/uploadAjaxAction", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile, Model model ) {
		log.info("upload ajax post.....");
		List<AttachFileDTO> list = new ArrayList<AttachFileDTO>();
		String uploadFolder = "/Users/tpqls/upload";
		
		// 업로드 폴더 생성
		String uploadFolderPath = getFolder();	// 저장된 폴더 경로
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("upload path: " + uploadPath);
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();	// 전체 경로까지의 폴더를 생성
		}

		for(MultipartFile multipartFile : uploadFile) {
			AttachFileDTO attachDTO = new AttachFileDTO();	// 첨부파일 정보를 저장하는 객체
			log.info("------------------------------");
			log.info("upload file name: " + multipartFile.getOriginalFilename());
			log.info("upload File Size: " + multipartFile.getSize());
			
			String uploadFileName = multipartFile.getOriginalFilename();
			// Chrome 브라우저에서 올라올 때 : 파일 이름만 전송된다.(a.jpg)
			// IE일 때는 전체 경로가 올라온다. (C:\Temp\a.jpg)
			// IE has file path -> 경로 자르기
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			log.info("only file name: " + uploadFileName);
			attachDTO.setFileName(uploadFileName);	// 원래 파일 이름을 저장
			
			// 파일의 중복 방지 : UUID -> 고유한 값을 발생
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;

			File saveFile = new File(uploadPath, uploadFileName);
			try {
				multipartFile.transferTo(saveFile);
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				// 첨부파일이 이미지 형태인지 검사
				if(checkImageType(saveFile)) {	// 이미지 파일일 경우 섬네일 생성
					attachDTO.setImage(true);
					FileOutputStream thumbnail = new FileOutputStream(
						new File(uploadPath, "s_" + uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(),
							thumbnail, 100, 100);	// 이미지 크기 : 100px * 100px
					thumbnail.close();
				}
				list.add(attachDTO);
			} catch(Exception e) {
				log.error(e.getMessage());
			}
		}	// 뷰 페이지 이름 : /uploadAjaxAction.jsp 파일
		// 첨부파일에 대한 정보를 응답
		return new ResponseEntity<List<AttachFileDTO>>(list, HttpStatus.OK);
	}
	
	// 첨부파일이 이미지인지 검사하는 메소드
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			if(contentType != null)	// modified by ksseo
				return contentType.startsWith("image"); // image/jpg, image/png, image/gif
			else
				return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


	// 년월일 형태로 파일의 경로를 반환(2022/10/11)
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		// OS에 따라서 디렉토리의 파일 구분자 다르므로 무관하게 쓰도록 사용
		// Windows OS : '\'
		// Linux OS : '/'
		return str.replace("-", File.separator);	// '-'->'\' 또는 '/'
	}

	// 브라우저에서 파일을 표시하는 기능을 추가 : /display
	// 입력 : 보여줄 파일 이름
	// 응답 : 보여줄 파일의 바이트 배열
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {
		log.info("fileName: " + fileName);
		File file = new File("/Users/tpqls/upload/" + fileName);
		log.info("file: " + file);
		ResponseEntity<byte[]> result = null;
		try {
			HttpHeaders header = new HttpHeaders();
			// MIME type을 응답 : image/jpg, image/png, image/gif
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(
					FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// 파일 다운로드 : 서버에 저장된 파일을 다운로드
	// 입력 : 다운로드할 파일 이름
	// 	헤더 정보에서 User-Agent -> 브라우저 종류를 판단
	// 출력 : 파일 형태를 출력 -> octet_stream -> ISO-8859-1 인코딩
	@GetMapping(value="/download", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent,String fileName) {
		log.info("download file: " + fileName);	// 년월일 형태의 파일형태를 사용한다.
		// Tomcat이 프로젝트에 대한 자원(Resource)
		Resource resource = new FileSystemResource("/Users/tpqls/upload/" + fileName);
		log.info("resource: " + resource);
		if(resource.exists() == false) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		String resourceName = resource.getFilename();
		
		// 다운로드할 때 원본 파일이름으로 다운로드를 해준다. : uuid_파일이름 -> 파일이름
		// remove UUID
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1); 
		
		HttpHeaders headers = new HttpHeaders();
		try {
			String downloadName = null;
			if(userAgent.contains("Trident")) {	// I.E
				log.info("IE browser");
				downloadName = URLEncoder.encode(resourceOriginalName,
						"UTF-8").replaceAll("\\+", " ");
			} else if(userAgent.contains("Edge")) {
				log.info("Edge browser");
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
				log.info("Edge name: " + downloadName);
			} else {
				log.info("Chrome browser");	// 인코딩
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), 
						"ISO-8859-1");
			}
			log.info("downloadName: " + downloadName);
			// 다운로드할 파일의 이름을 헤더에 지정한다.
			headers.add("Content-Disposition", "attachment; filename=" + downloadName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	// 파일을 삭제하는 기능을 추가
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type) {
		log.info("deleteFile: " + fileName);
		File file;
		try {
			file = new File("/Users/tpqls/upload/" + URLDecoder.decode(fileName,
	"UTF-8"));
			file.delete();
			if (type.equals("image")) {	// 이미지인 경우 섬네일을 삭제
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				log.info("largeFileName: " + largeFileName);
				file = new File(largeFileName);
				file.delete();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}

}
