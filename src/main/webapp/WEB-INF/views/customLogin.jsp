<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 폼</title>
</head>
<body>
	<h1>Custom Login Page</h1>
	<h2><c:out value="${error}"/></h2>
	<h2><c:out value="${logout}"/></h2>
	
	<!-- action="/login" : 약속(가정 : security가 제공하는 기능을 그대로 사용) -->
	<form action="/login" method="post">
		<div>
			<!--security 모듈에서는 username/password를 사용해야 한다. 
			id, pw (X) -> 서버단에서 security가 제공하는 기능을 그대로 사용하기 때문이다. -->
			<input type="text" name="username" value="admin"/>
		</div>
		<div>
			<input type="password" name="password" value="admin"/>
		</div>
		<div>
			<input type="submit"/>
		</div>
		<!-- 해킹을 방지하기 위해서 CSRF 기법을 적용 -->
		<!-- 규칙 : 스프링 시큐리티에서는 post로 전송할 때 아래와 같이 입력해야 함. -->
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>
</body>
</html>