package cn.structure.starter.jwt.dto;

import io.swagger.annotations.ApiModelProperty;


public class LoginRequestDTO {

	@ApiModelProperty(value = "用户名",example = "tom")
	private String username;
	@ApiModelProperty(value = "密码",example = "123456")
	private String password;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
