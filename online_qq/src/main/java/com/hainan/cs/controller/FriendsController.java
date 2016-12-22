package com.hainan.cs.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hainan.cs.dao.UserDaoImp;
import com.hainan.cs.singleton.UserSingleton;

@Controller
@RequestMapping(value="/friends")
public class FriendsController {
	@Resource
	private UserDaoImp userDao;
	
	@RequestMapping()
	@ResponseBody
	public Map<String,Object> showFriends(){
		Map<String,Object> map=new HashMap<String,Object>();
		UserSingleton user=UserSingleton.getInstance();
		
		return map;
	}

}
