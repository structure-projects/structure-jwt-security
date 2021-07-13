package cn.structure.starter.jwt.filter;

import cn.structure.starter.jwt.entity.AuthUser;
import cn.structure.starter.jwt.interfaces.ITokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 登录处理
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/11 9:31
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private ITokenService tokenService;

    public LoginFilter(ITokenService tokenService){
        super(new AntPathRequestMatcher("/login","POST"));
        this.tokenService = tokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken params = new UsernamePasswordAuthenticationToken(username,password);
        AuthenticationManager authenticationManager = this.getAuthenticationManager();
        Authentication authenticate = authenticationManager.authenticate(params);

        // 认证不通过，下面的代码不会执行
        AuthUser details = (AuthUser) authenticate.getPrincipal();
        final String token = tokenService.generateToken(details);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(token);
        return authenticate;
    }
}
