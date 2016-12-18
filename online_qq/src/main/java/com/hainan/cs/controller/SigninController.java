package com.hainan.cs.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hainan.cs.bean.User;
import com.hainan.cs.dao.UserDaoImp;

@Controller
@RequestMapping(value="/sign")
public class SigninController {
	
	@Resource
	private UserDaoImp userDao;
	
	@RequestMapping
	public ModelAndView sign(){
		ModelAndView mav=new ModelAndView();
		mav.setViewName("sign");
		return mav;
	}
	@RequestMapping(value="adduser")
	public ModelAndView signup(User user){
		userDao.addUser(user);
		userDao.addFriends(user, user.getId(), "friends");
		userDao.addUserToList("userlist", user.getId());
		userDao.addFriendsGroup(user, "friends");
		ModelAndView mav=new ModelAndView();
		mav.setViewName("login");
		return mav;
	}
}
