package cn.structure.example.jwt.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.exception.CommonException;
import cn.structure.starter.jwt.dto.LoginRequestDTO;
import cn.structure.starter.jwt.entity.AuthUser;
import cn.structure.starter.jwt.interfaces.ITokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 登录逻辑
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/17 10:53
 */
@RestController
@Api(tags = "登录模块")
@RequestMapping(value = "")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ITokenService tokenService;

    @PostMapping(value = "/login")
    @ApiOperation(value = "登录请求")
    public ResResultVO<String> login(@Validated @RequestBody LoginRequestDTO loginDto){
        UsernamePasswordAuthenticationToken params = null;
        try {
            params = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            authenticationManager.authenticate(params);
        } catch (CommonException e) {
            return ResResultVO.fail(e.getCode(),e.getMessage());
        }catch (Exception e) {
            return ResResultVO.exception("登录错误！");
        }
        AuthUser authUser = (AuthUser) params.getPrincipal();
        String token = tokenService.generateToken(authUser);
        return ResResultVO.success(token);
    }

}
