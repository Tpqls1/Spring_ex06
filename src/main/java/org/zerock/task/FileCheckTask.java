package org.zerock.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.domain.BoardAttachVO;
import org.zerock.mapper.BoardAttachMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

//	주기적으로 불필요하게 존재하는 첨부파일을 삭제하는 Task
//	cron 기능을 이용해서 주기적으로 동작한다.
@Component
@Log4j
public class FileCheckTask {
	private static final String UPLOAD_PATH = "/Users/tpqls/upload";
	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;
	// @Scheduled : 주기적인 작업을 수행
	// 0 : 초
	// * : 분
	// * : 시간
	// * : 월
	// * : 요일
	// * : 년도

	private String getFolderYesterDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String str = sdf.format(cal.getTime());
		return str.replace("-", File.separator);
	}

	//	한가한 시간에 작업 : 새벽 2시 
	//	전날에 있는 데이터에 대하여 작업한다.
	//	데이터베이스에 존재하지 않지만 파일로 존재하는 파일을 삭제 : 기준 -> 데이터베이스 
	//	데이터베이스에 존재하는 파일은 유지를 하고 존재하지 않으면 삭제 
	@Scheduled(cron = "0 50 11 * * *") // 오전 11시 50분 
	public void checkFiles() {
		log.warn("File check task run.........");
		log.warn(new Date());
		// file list in datebase
		List<BoardAttachVO> fileList = attachMapper.getOldFiles(); // 데이터베이스 테이블

		// ready for check file in directory with database file list
		List<Path> fileListPaths = fileList.stream()
				.map(vo -> Paths.get(UPLOAD_PATH, vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
				.collect(Collectors.toList()); // 파일에 대한 목록(존재하는) - 원본파일

		// image file has thumbnail file
		fileList.stream().filter(vo -> vo.isFileType() == true).map(
				vo -> Paths.get(UPLOAD_PATH, vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName())) // 섬네일
				.forEach(p -> fileListPaths.add(p));
		log.warn("=======================================");
		fileListPaths.forEach(p -> log.warn(p)); // 데이터베이스에 저장된 파일의 정보를 로깅

		// file in yesterday directory
		File targetDir = Paths.get(UPLOAD_PATH, getFolderYesterDay()).toFile();
		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);

		if (removeFiles == null) // null pointer exception 처리
			return;

		log.warn("---------------------------------------");
		for (File file : removeFiles) {
			log.warn(file.getAbsolutePath());
			file.delete();

		}
	}
}
