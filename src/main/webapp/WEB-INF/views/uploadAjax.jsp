<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ajax를 사용한 파일 업로드</title>
<!-- 첨부파일에 대한 정보를 보여주는 영역에 대한 CSS를 적용 -->
<style>
.uploadResult {
	width: 100%;
	background-color: gray;
}
.uploadResult ul {
	display: flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
}
.uploadResult ul li {
	list-style: none;
	padding: 10px;
}
.uploadResult ul li img {
	width: 20px;
}
.bigPictureWrapper {
  position: absolute;
  display: none;
  justify-content: center;
  align-items: center;
  top:0%;
  width:100%;
  height:100%;
  background-color: gray; 
  z-index: 100;
}
.bigPicture {
  position: relative;
  display:flex;
  justify-content: center;
  align-items: center;
}
.bigPicture img {
  width: 600px;
}
</style>
<!-- CDN을 이용해서 jQuery 라이브러리를 추가 -->
<script src="https://code.jquery.com/jquery-3.3.1.min.js"
	integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
	crossorigin="anonymous">
</script>
<script type="text/javascript">
// 원본 이미지를 보여주는 함수
// 입력 매개변수 : 원본 파일 이름
function showImage(fileCallPath) {
	//alert(fileCallPath);
	$(".bigPictureWrapper").css("display","flex").show();
	  
	$(".bigPicture")
		.html("<img src='/display?fileName="+ encodeURI(fileCallPath)+"'>")
		.animate({width:'100%', height: '100%'}, 1000);
}

$(document).ready(function() {	// HTML code가 로딩된 다음에 동작
	var regex = new RegExp("(.*?\.(exe|sh|zip|alz)$)");	// 확장자 제한
	var maxSize = 5242880;	// 파일 크기를 5MB 크기로 제한
	
	function checkExtension(filename, filesize) {
		if(filesize > maxSize) {			// 파일의 크기 제한
			alert("파일 크기 초과");
			return false;
		}
		if(regex.test(filename)) {		// 확장자 제한
			alert("해당 종류의 파일은 업로드할 수 없습니다.");
			return false;
		}
		return true;
	}

	// input file name:uploadFile 요소를 복사
	var cloneObj = $(".uploadDiv").clone();	// 첨부 파일이 없는 box를 복사
	
	// UploadBtn이 눌리면 동작하도록 이벤트를 등록
	$("#uploadBtn").on("click", function(e) {
		// Ajax를 이용해서 서버로 파일을 전송
		var formData = new FormData();	// 가상의 폼 데이터
		var inputFile = $("input[name='uploadFile']");
		var files = inputFile[0].files;
		console.log(files);
		for(var i = 0;i < files.length;i++) {
			if(!checkExtension(files[i].name, files[i].size)) {	// 유효성 검사
				return;	// 확장자 제한이나 파일 크기 제한에 걸릴 경우 종료
			}
			formData.append("uploadFile", files[i]);
		}
		// Ajax를 이용해서 서버로 전송 : 공식
		$.ajax({
			url: '/uploadAjaxAction',
			processData: false,
			contentType: false,
			data: formData,
			type: 'POST',
			dataType: 'json',	// JSON 객체 형태로 응답을 받겠다.
			success: function(result) {	// result에 JSON 객체 형태로 응답 : List<AttachFileDTO>
				//alert("Uploaded");
				console.log(result);
				// 파일 입력 폼을 다시 reset -> 새로운 첨부 파일을 추가할 수 있다.
				$(".uploadDiv").html(cloneObj.html());	// 기존 첨부파일 정보를 reset
				showUploadedFile(result);
			}
		});
	});
	
	var uploadResult = $(".uploadResult ul");
	// 입력 : List<AttachFileDTO>
	function showUploadedFile(uploadResultArr) {
		var str = "";
		// i : index
		// obj : AttachFileDTO
		$(uploadResultArr).each(function(i, obj) {	// 각각의 첨부 파일에 대하여
			if(!obj.image) {	// 이미지가 아닌 경우 -> default 이미지를 보여준다.
				// 실질적으로 다운로드 할 때는 업로드된 파일의 경로를 지정해야 한다.
				var fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" +
						obj.fileName);
				// default 이미지를 보여줄 때는 상대 경로에 저장된 이미지를 보여주는 경우
				str += "<li><div><a href='/download?fileName=" + fileCallPath + "'>" +
					"<img src='/resources/img/attach.png'>"	+ obj.fileName + "</a>" +
					"<span data-file=\'" + fileCallPath + "\' data-type='file'> x </span>" +
					"</div></li>";	
			} else {	// 이미지인 경우
				// 이미지 파일을 표시하는 부분을 추가
				// 서버에 있는 이미지 파일을 경로를 저장하는 변수
				// 년월일 파일 경로 + 's_' + uuid + '_' + fileName : 인코딩을 해주어야 한다.
				var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" +
						obj.uuid + '_' + obj.fileName);
				var originPath = obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName;
				originPath = originPath.replace(new RegExp(/\\/g), "/");
				// 절대 경로에 저장된 이미지를 보여주는 방버
				// '/display' url을 사용해서 이미지를 보여준다.
				str += "<li><a href=\"javascript:showImage(\'" + originPath +
					"\')\"><img src='/display?fileName=" + fileCallPath + "'></a>" +
					"<span data-file=\'" + fileCallPath + "\' data-type='image'> x </span>"
					"</li>";
			}
		});
		uploadResult.append(str);	// <ul> 태그에 <li> 태그들을 추가
	}
	
	// 원본이미지를 클릭하면 사라지도록 한다.
	$(".bigPictureWrapper").on("click", function(e){
		$(".bigPicture").animate({width:'0%', height: '0%'}, 1000);
		setTimeout(function() {
			$(".bigPictureWrapper").hide();
		}, 1000);
	});

	// 삭제버튼을 누르면 삭제를 하도록 (서버로 삭제 요청, 화면표시부분을 삭제)
	$(".uploadResult").on("click","span", function(e){
		var targetFile = $(this).data("file");	// 삭제하고자 하는 파일 이름
		var type = $(this).data("type");		// 첨부파일의 유형
		console.log(targetFile);
		$.ajax({
		    url: '/deleteFile',
		    data: {fileName: targetFile, type:type},	// 서버에 전달
		    dataType:'text',
		    type: 'POST',
		    success: function(result){
		         alert(result);
		    }
		}); //$.ajax  
	});

});
</script>
</head>
<body>
	<h1>Upload with Ajax</h1>
	<!-- 원본 이미지를 보여주는 영역 -->
	<div class="bigPictureWrapper">
		<div class='bigPicture'>
		</div>
	</div>
	<div class="uploadDiv">
		<input type="file" name="uploadFile" multiple>
	</div>
	<!-- 업로드된 파일의 정보를 출력 -->
	<div class="uploadResult">
		<!-- ul-li 형식으로 첨파일의 정보를 표시 -->
		<ul>
		
		</ul>
	</div>
	<button id="uploadBtn">Upload</button>
</body>
</html>