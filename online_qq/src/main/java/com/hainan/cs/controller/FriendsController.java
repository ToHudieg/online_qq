package com.hainan.cs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hainan.cs.dao.UserDaoImp;
import com.hainan.cs.singleton.UserSingleton;

@Controller
@RequestMapping(value="/home")
public class FriendsController {
	@Resource
	private UserDaoImp userDao;
	
	@RequestMapping
	public ModelAndView home(){
		ModelAndView mav=new ModelAndView();
		mav.setViewName("home");
		UserSingleton user=UserSingleton.getInstance();
		mav.addObject("username", user.getName() );
		return mav;
		
	}
	
	@RequestMapping(value="/friends")
	@ResponseBody
	public Map<String,Object> showFriends(){
		System.out.println("got it");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("reply", "got yourmessage");
		UserSingleton user=UserSingleton.getInstance();
		//获取用户的所以好友分组，及各组的好友
		List<String> friendgroup=userDao.getFriendGroups(user.getUserid());
		System.out.println(friendgroup.size());
		Map<String,String> friend=userDao.getFriends(user.getUserid());
		for(Map.Entry<String, String>entry:friend.entrySet()){
			String key=entry.getKey();
			String value=entry.getValue();
			map.put(key, value);
			}
		return map;
	}

}
