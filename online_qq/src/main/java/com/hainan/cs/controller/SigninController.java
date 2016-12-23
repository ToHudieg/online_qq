package com.hainan.cs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hainan.cs.bean.User;
import com.hainan.cs.dao.UserDaoImp;

@Controller
@RequestMapping(value="/sign")
public class SigninController {
	
	@Resource
	private UserDaoImp userDao;
	
	@RequestMapping()
	public ModelAndView sign(){
		ModelAndView mav=new ModelAndView();
		mav.setViewName("sign");
		return mav;
	}
	@RequestMapping(value="/adduser")
	public ModelAndView signup(String username,String password,String email,String phone,String address){
		User user=new User();
		user.setAdress(address);
		user.setEmial(email);
		user.setPhone(phone);
		user.setUsername(username);
		user.setPassword(password);
		String id=UUID.randomUUID().toString();
		user.setId(id);
		userDao.addUser(user);
		userDao.addFriends(user, user.getId(), "friends");
		userDao.addUserToList("userlist", user.getId(),user.getUsername());
		userDao.addFriendsGroup(user, "friends");
		ModelAndView mav=new ModelAndView();
		mav.setViewName("redirect:/login");
		return mav;
	}
	@RequestMapping(value="/check")
	@ResponseBody
	public Map<String,Object> check(String username){
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String,String> userlist=userDao.getUserList();
		for(Map.Entry<String, String>en:userlist.entrySet()){
			String id=en.getKey();
			String name=en.getValue();
			Map<String,String> usermap=userDao.getUser(id);
			int exist=0;
			for(Map.Entry<String, String>entry:usermap.entrySet()){
				String key=entry.getKey();
				String value=entry.getValue();
				if(key.equals("name")&&value.equals(username)){
					exist=1;
					break;
					}
				}
			if(exist==1){
				map.put("msg", "username exist");
			}else{
				map.put("msg", "user name can be used");
			}
		}
		return map;
	}
}
