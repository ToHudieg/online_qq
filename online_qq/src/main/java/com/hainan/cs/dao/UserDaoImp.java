package com.hainan.cs.dao;

import java.util.UUID;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import com.hainan.cs.bean.User;
@Repository(value="userDao")
public class UserDaoImp extends RedisGeneratorDao<String,String>{
	//添加user Hash hsetNX
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
	
	//添加List
	public void addUserToList(final String key,final String value){
		redisTemplate.execute(new RedisCallback<Boolean>(){
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer=getRedisSerializer();
				byte[] a=serializer.serialize(key);
				byte[] b=serializer.serialize(value);
				connection.lPush(a, b);
				return true;
			}
			});
	}
	//删除对象
	public void deleteUser(String userid){
		redisTemplate.delete(userid);
	}
	//删除hash中的一条记录
	public void deleteUserFromList(String key,String id){
		BoundHashOperations<String,Object,Object> bhops=redisTemplate.boundHashOps(key);
		bhops.delete(id);
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
		udi.addUserToList("userlist", id);
	}
}
