package cn.structure.starter.jwt.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Jwt配置
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/10 19:58
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties("structure.jwt")
public class JwtConfig {

    /**
     * 加密串
     */
    private String secret = "JWT";

    /**
     * token 有效时间
     */
    private long jwtTokenValidity = 9 * 60 * 60;

    private String corsFilterClass = "cn.structure.starter.jwt.configuration.CorsFilter";

    /**
     * 访问权限
     */
    private Map<String, List<String>> antMatchers;

    public Map<String, List<String>> getAntMatchers() {
        return antMatchers;
    }

    public void setAntMatchers(Map<String, List<String>> antMatchers) {
        this.antMatchers = antMatchers;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getJwtTokenValidity() {
        return jwtTokenValidity;
    }

    public void setJwtTokenValidity(long jwtTokenValidity) {
        this.jwtTokenValidity = jwtTokenValidity;
    }
}
