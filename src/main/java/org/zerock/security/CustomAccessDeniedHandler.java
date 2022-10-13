package org.zerock.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.log4j.Log4j;

@Log4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	//	상태 코드 403에 대하여 처리하는 핸들러를 정의 
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
			log.error("Access Denied Handler");
			log.error("Redirect...");
			
			//	필요한 정보를 더 처리를 해야 한다. 
			//	해커가 접근한다. -> 조치를 취해야 한다.... 
			response.sendRedirect("/accessError");
	}

}
