# structure-jwt-security

集成security认证，对生成JWT的封装

## 功能介绍
1. 集成security认证
2. 封装JWT的生成
## 如何使用

### 引用POM ###
```xml
     <dependency>
        <groupId>cn.structured</groupId>
        <artifactId>structure-jwt-security-starter</artifactId>
        <version>${last.version}</version>
    </dependency>
```

### yaml 配置 ### 
配置对jwt加密解密的密钥
```yaml
structure:
  jwt:
    secret: 12345646
```

### 完成一个基础的登录接口并返回一个JWT token ### 
1. 第一步完成基于security认证的UserService并实现loadUserByUsername
```java
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = new AuthUser();
        authUser.setId("1");
        authUser.setUsername("admin");
        authUser.setPassword(passwordEncoder.encode("123456"));
        return authUser;
    }
}

```

2. 编写一个登录认证接口
```java
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
        //生成token
        String token = tokenService.generateToken(authUser);
        return ResResultVO.success(token);
    }
}
```

### token 放置位置 ### 

jwt 请放置在请求头Authorization中.例如 Authorization:Bearer jwt 。注意Bearer和jwt中间有一个空格 

注意重写登录接口必须为/login开头的接口因为默认拦截器中对该路径的接口进行了放行
