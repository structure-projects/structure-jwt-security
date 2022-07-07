package cn.structure.starter.jwt.configuration;

import cn.structure.common.constant.AuthConstant;
import cn.structure.starter.jwt.entity.AuthUser;
import cn.structure.starter.jwt.interfaces.ITokenService;
import cn.structure.starter.jwt.properties.JwtConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter{

    private ITokenService iTokenService;

    private JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 放行资源和登录
        String requestUri = request.getRequestURI();
        Map<String, List<String>> antMatchers = jwtConfig.getAntMatchers();

        List<String> unAuthenticated = antMatchers.get(AuthConstant.UN_AUTHENTICATED);
        AntPathMatcher matcher = new AntPathMatcher();
        for (String uri : unAuthenticated) {
            if (matcher.match(uri, requestUri)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String token = iTokenService.getToken(request);
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        AuthUser user = iTokenService.getUserInfoFromToken(token);
        if (user == null) {
            chain.doFilter(request, response);
            return;
        }

        if (iTokenService.isTokenExpired(token)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // 认证成功，下个过滤器就会放行了。
        // 每个请求有效，下个请求authentication就变了
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}