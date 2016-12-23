package com.hainan.cs.dao;

import com.hainan.cs.bean.User;

public interface UserDao {

	boolean addUser(User user);

	void addFriends(User user, String friendid, String group);

	void deleteFriend(User user, String friendsid);

	void addFriendsGroup(User user, String group);

	void deleteUser(String userid);

	void deleteUserFromList(String key, String id);

	void addUserToList(String key, String userid, String username);

}
