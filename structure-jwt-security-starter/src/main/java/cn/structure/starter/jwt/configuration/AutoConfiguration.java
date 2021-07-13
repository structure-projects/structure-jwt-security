package cn.structure.starter.jwt.configuration;

import cn.structure.starter.jwt.interfaces.ITokenService;
import cn.structure.starter.jwt.properties.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * <p>
 * 自动装配类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/10 20:06
 */
@Configuration
@EnableConfigurationProperties({JwtConfig.class})
@ComponentScan(value = {"cn.structure.starter.jwt.configuration","cn.structure.starter.jwt.filter"})
public class AutoConfiguration {

    @Autowired
    private JwtConfig jwtConfig;

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(ITokenService.class)
    public ITokenService tokenService(){
        return new JwtDefaultServiceImpl(jwtConfig);
    }

//    @Bean
//    @ConditionalOnMissingBean(CorsFilter.class)
//    public CorsFilter corsFilter (){
//        return new CorsFilter();
//    }
//
//
//    @Bean
//    @ConditionalOnMissingBean(org.springframework.web.filter.CorsFilter.class)
//    public org.springframework.web.filter.CorsFilter buildConfigCorsFilter() {
//
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.setMaxAge(18000L);
//        config.addAllowedMethod("OPTIONS");
//        config.addAllowedMethod("HEAD");
//        config.addAllowedMethod("GET");
//        config.addAllowedMethod("PUT");
//        config.addAllowedMethod("POST");
//        config.addAllowedMethod("DELETE");
//        config.addAllowedMethod("PATCH");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        // 对接口配置跨域设置
//        source.registerCorsConfiguration("/**", config);
//        return new org.springframework.web.filter.CorsFilter(source);
//    }
}
