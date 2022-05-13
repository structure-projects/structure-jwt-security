package cn.structure.starter.jwt.interfaces;

import cn.structure.starter.jwt.entity.AuthUser;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * tokenService
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/10 20:07
 */
public interface ITokenService {
    /**
     * 通过token转换为用户
     * @param token
     * @return
     */
    AuthUser getUserInfoFromToken(String token);

    /**
     * 通过token转换为Claims
     * @param token
     * @return
     */
    Claims getAllClaimsFromToken(String token);

    /**
     * 校验token是否失效
     * @param token
     * @return
     */
    Boolean isTokenExpired(String token);

    /**
     * 通过用户信息生成token
     * @param userDetails
     * @return
     */
    String generateToken(AuthUser userDetails);

    /**
     * 通过claims生成token
     * @param claims
     * @param subject
     * @return
     */
    String doGenerateToken(Map<String, Object> claims, String subject);

    /**
     * 通过请求头中获取token
     * @param request
     * @return
     */
    String getToken(HttpServletRequest request);
}
