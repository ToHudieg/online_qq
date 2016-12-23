package com.hainan.cs.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hainan.cs.dao.UserDaoImp;
import com.hainan.cs.singleton.UserSingleton;

@Controller
@RequestMapping(value="/login")
public class LoginController {
	
	@Resource
	private UserDaoImp userDao;
	
	@RequestMapping
	public ModelAndView login(){
		ModelAndView mav=new ModelAndView();
		mav.addObject("tag", 0);
		mav.setViewName("login");
		return mav;
	}
	@RequestMapping(value="/userlogin")
	public ModelAndView userLogin(String username,String password){
		ModelAndView mav=new ModelAndView();
		Map<String,String> userlist=userDao.getUserList();
		int nameright=0;
		int passright=0;
		for(Map.Entry<String, String>en:userlist.entrySet()){
			String id=en.getKey();
			String name=en.getValue();
			Map<String,String> usermap=userDao.getUser(id);
			for(Map.Entry<String, String>entry:usermap.entrySet()){
				String key=entry.getKey();
				String value=entry.getValue();
				if(key.equals("name")){
					if(value.equals(username)){
						nameright=1;
					}
				}
				if(key.equals("password")){
					if(value.equals(password)){
						passright=1;
					}
				}
				//用户名和密码都正确
				if(nameright==1&&passright==1){
					UserSingleton user=UserSingleton.getInstance();
					user.setPassword(password);
					user.setName(username);
					System.out.println(id);
					user.setUserid(id);
					mav.setViewName("redirect:/home");
					mav.addObject("username", username);
					break;
				}
			}
			if(nameright==1&&passright==1){
				break;
			}
		}
		if(nameright==0||passright==0){
			mav.addObject("tag", 1);
			mav.setViewName("redirect:/login");
		}
		return mav;
	}
}
