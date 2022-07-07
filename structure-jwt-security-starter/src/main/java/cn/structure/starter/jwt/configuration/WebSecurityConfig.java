package cn.structure.starter.jwt.configuration;

import cn.structure.common.constant.AuthConstant;
import cn.structure.common.constant.SymbolConstant;
import cn.structure.common.enums.NumberEnum;
import cn.structure.starter.jwt.interfaces.ICorsFilter;
import cn.structure.starter.jwt.interfaces.ITokenService;
import cn.structure.starter.jwt.properties.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private AuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ITokenService tokenService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests();
        Map<String, List<String>> antMatchers = jwtConfig.getAntMatchers();
        if (antMatchers != null) {
            Set<String> keys = antMatchers.keySet();
            for (String key : keys) {
                if (key.equals(AuthConstant.UN_AUTHENTICATED)) {
                    List<String> urls = antMatchers.get(key);
                    for (String url : urls) {
                        httpSecurity.csrf().disable().authorizeRequests().antMatchers(url).permitAll();
                    }
                } else {
                    String[] authUrlStr = key.split(SymbolConstant.MINUS);
                    if (authUrlStr.length < NumberEnum.TWO.getValue()) {
                        continue;
                    }
                    String type = authUrlStr[NumberEnum.ZERO.getValue()];
                    String str = authUrlStr[NumberEnum.ONE.getValue()];
                    List<String> urls = antMatchers.get(key);
                    if (type.equals(AuthConstant.ROLE)) {
                        for (String url : urls) {
                            httpSecurity.csrf().disable().authorizeRequests().antMatchers(url).hasRole(str);
                        }
                    }
                    if (type.equals(AuthConstant.AUTH)) {
                        for (String url : urls) {
                            httpSecurity.csrf().disable().authorizeRequests().antMatchers(url).hasAuthority(str);
                        }
                    }
                }
            }
        }
        httpSecurity.authorizeRequests().anyRequest().authenticated().and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(new JwtRequestFilter(tokenService, jwtConfig), UsernamePasswordAuthenticationFilter.class);
        Class<?> aClass = Class.forName(jwtConfig.getCorsFilterClass());
        ICorsFilter iCorsFilter = (ICorsFilter) aClass.newInstance();
        httpSecurity.addFilterBefore(iCorsFilter, JwtRequestFilter.class);

    }
}
