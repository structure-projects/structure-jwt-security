package cn.structure.starter.jwt.configuration;

import cn.structure.starter.jwt.entity.AuthUser;
import cn.structure.starter.jwt.properties.JwtConfig;
import cn.structure.starter.jwt.interfaces.ITokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class JwtDefaultServiceImpl implements ITokenService {

	private JwtConfig jwtConfig;

	@Override
	public AuthUser getUserInfoFromToken(String token) {
		Claims claims = getAllClaimsFromToken(token);
		return new AuthUser((String)claims.get("id"), (String)claims.get("username"));
	}

	@Override
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token).getBody();
	}

	@Override
	public Boolean isTokenExpired(String token) {
		Claims claims = getAllClaimsFromToken(token);
		Date expiration = claims.getExpiration();
		return expiration.before(new Date());
	}

	@Override
	public String generateToken(AuthUser userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", userDetails.getId());
		claims.put("username", userDetails.getUsername());
		return doGenerateToken(claims, userDetails.getUsername());
	}

	@Override
	public String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getJwtTokenValidity() * 1000))
				.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret()).compact();
	}
}
