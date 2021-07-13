package cn.structure.starter.jwt.configuration;

import cn.structure.starter.jwt.entity.AuthUser;
import cn.structure.starter.jwt.interfaces.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private ITokenService iTokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// 放行资源和登录
        String requestUri = request.getRequestURI();
        if(requestUri.startsWith("/login")) {
            chain.doFilter(request, response);
            return;
        }

		String token = getToken(request);
		if(token == null) {
			chain.doFilter(request, response);
			return;
		}

		AuthUser user = iTokenService.getUserInfoFromToken(token);
		if (user == null) {
			chain.doFilter(request, response);
			return;
		}
		
		if(iTokenService.isTokenExpired(token)) {
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

		// 认证成功，下个过滤器就会放行了。
		// 每个请求有效，下个请求authentication就变了
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}
	
	private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
          
        if (header != null && header.startsWith("Bearer ")) {
          return header.replace("Bearer ","");
        }
        return null;
    }

}