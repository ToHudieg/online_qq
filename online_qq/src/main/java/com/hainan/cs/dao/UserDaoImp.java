package com.hainan.cs.dao;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import com.hainan.cs.bean.User;
@Repository(value="userDao")
public class UserDaoImp extends RedisGeneratorDao<String,String> implements UserDao{
	//添加user Hash hsetNX
	@Override
	public boolean addUser(final User user){
		boolean result=redisTemplate.execute(new RedisCallback<Boolean>(){
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer=getRedisSerializer();
				byte[] key=serializer.serialize(user.getId());
				byte[] name=serializer.serialize("name");
				byte[] namea=serializer.serialize(user.getUsername());
				byte[] passworda=serializer.serialize(user.getPassword());
				byte[] password=serializer.serialize("password");
				byte[] email=serializer.serialize("email");
				byte[] emaila=serializer.serialize(user.getEmial());
				byte[] phone=serializer.serialize("phone");
				byte[] phonea=serializer.serialize(user.getPhone());
				byte[] address=serializer.serialize("address");
				byte[] addressa=serializer.serialize(user.getAdress());
				connection.hSetNX(key, name,namea);//hash类型的提交<key,hash的key,hash的value>
				connection.hSetNX(key, password, passworda);
				connection.hSetNX(key, email, emaila);
				connection.hSetNX(key, phone, phonea);
				connection.hSetNX(key, address, addressa);
				return true;
			}
		});
		return result;
	}
	//获取用户信息
	public Map<String,String> getUser(String userid){
		BoundHashOperations<String,String,String> bhops=redisTemplate.boundHashOps(userid);
		return bhops.entries();
	}
	//添加用户好友
	@Override
	public void addFriends(final User user, final String friendid, final String group){
		Boolean result=redisTemplate.execute(new RedisCallback<Boolean>(){
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer=redisTemplate.getStringSerializer();
				String key="friend"+user.getId();
				byte[] fkey=serializer.serialize(key);
				byte[] ffriendid=serializer.serialize(friendid);
				byte[] fgroup=serializer.serialize(group);
				Boolean r= connection.hSet(fkey, ffriendid, fgroup);
				return r;
			}
			
		});
	}
	//删除用户好友
	@Override
	public void deleteFriend(User user, String friendsid){
		BoundHashOperations<String,String,String> bhops=redisTemplate.boundHashOps("friend"+user.getId());
		bhops.delete(friendsid);
	}
	//添加用户好友组
	@Override
	public void addFriendsGroup(final User user, final String group){
		redisTemplate.execute(new RedisCallback<Boolean>(){

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer=redisTemplate.getStringSerializer();
				byte[] key=serializer.serialize("group"+user.getId());
				byte[] value=serializer.serialize(group);
				connection.lPush(key, value);
				return true;
			}
			
		});
	}
	//添加用户的列表的hashmap
	@Override
	public void addUserToList(final String key,final String userid,final String username){
		redisTemplate.execute(new RedisCallback<Boolean>(){
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer=getRedisSerializer();
				byte[] a=serializer.serialize(key);
				byte[] c=serializer.serialize(userid);
				byte[] b=serializer.serialize(username);
				connection.hSet(a, c, b);
				return true;
			}
			});
	}
	//获取用户列表
	public Map<String,String> getUserList(){
		BoundHashOperations<String,String,String> hlops=redisTemplate.boundHashOps("userlist");
		return hlops.entries();
	}
	//删除对象
	@Override
	public void deleteUser(String userid){
		redisTemplate.delete(userid);
	}
	//删除hash中的一条记录
	@Override
	public void deleteUserFromList(String key,String id){
		BoundHashOperations<String,Object,Object> bhops=redisTemplate.boundHashOps(key);
		bhops.delete(id);
	}
	//获取用户的好友分组
	public List<String> getFriendGroups(String userid){
		BoundListOperations<String,String> blops=redisTemplate.boundListOps("group"+userid);
		System.out.println(userid);
		return blops.range(0, blops.size());
	}
	//获取用户的好友列表
	public Map<String,String> getFriends(String userid){
		BoundHashOperations<String,String,String> bhops=redisTemplate.boundHashOps("friend"+userid);
		return bhops.entries();
	}
	
	//测试
	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring/application-config.xml");
		UserDaoImp udi=context.getBean(UserDaoImp.class);
		User u=new User();
		String id=UUID.randomUUID().toString();//自动生成一个id
		u.setId(id);
		u.setUsername("zhangsan");
		u.setPassword("123456");
		u.setEmial("test@qq.com");
		u.setPhone("234556");
		u.setAdress("china haikou");
		udi.addUser(u);
		udi.addUserToList("userlist", id, "zhangsan");
		context.close();
	}
}
