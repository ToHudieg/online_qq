package com.hainan.cs.controller;

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
		map.put("group", friendgroup);
		Map<String,String> friend=userDao.getFriends(user.getUserid());
		Map<String,String> userlist=userDao.getUserList();
		//用来存好友列表只是key是用户名
		Map<String,String> fmap=new HashMap<String,String>();
		for(Map.Entry<String, String>entry:friend.entrySet()){
			String key=entry.getKey();
			String value=entry.getValue();
			for(Map.Entry<String, String>entry1:userlist.entrySet()){
				String idInUserList=entry1.getKey();
				String nameInUserList=entry1.getValue();
				if(key.equals(idInUserList)){
					fmap.put(nameInUserList, value);
					break;
				}
				}
			}
		map.put("friends", fmap);
		return map;
	}
	
	@RequestMapping(value="/search")
	@ResponseBody
	public Map<String,String> searchFriend(String friendname){
		System.out.println("搜索的好友姓名："+friendname);
		Map<String,String> searchResult=new HashMap<String,String>();
		Map<String,String> userlist=userDao.getUserList();
		int tag=0;
		for(Map.Entry<String, String>entry:userlist.entrySet()){
			String key=entry.getKey();
			String value=entry.getValue();
			if(value.equals(friendname)){
				Map<String,String> usermap=userDao.getUser(key);
				String useremail=usermap.get("email");
				String userphone=usermap.get("phone");
				String useraddress=usermap.get("address");
				searchResult.put("result", "exist");
				searchResult.put("name", friendname);
				searchResult.put("email", useremail);
				searchResult.put("phone", userphone);
				searchResult.put("address", useraddress);
				
				tag=1;
				break;
			}
		}
		if(tag==0){
			searchResult.put("result", "notexist");
		}
		return searchResult;
	}
}
