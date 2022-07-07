package cn.structure.starter.jwt.configuration;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -7858869558253243875L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		String json = "{\"code\": -2, \"msg\": \"无效token\", \"data\": null}";

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}
}