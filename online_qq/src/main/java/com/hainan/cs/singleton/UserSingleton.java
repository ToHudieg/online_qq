package com.hainan.cs.singleton;

public class UserSingleton {
	private static UserSingleton instance;
	private String name;
	private String password;
	private String userid;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	private UserSingleton(){}
	public static UserSingleton getInstance() {
		if(instance==null){
			instance=new UserSingleton();
			return instance;
		}else return instance;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
