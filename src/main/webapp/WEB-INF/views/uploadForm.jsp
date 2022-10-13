<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 업로드 폼</title>
</head>
<body>
<form action="uploadFormAction" method="post" encType="multipart/form-data">
	<label for="desc">설명</label><input id="desc" type="text" name="desc"><br>
	<input type="file" name="uploadFile" multiple><br>
	<button>Submit</button>
</form>
</body>
</html>