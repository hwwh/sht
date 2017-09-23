﻿package com.sht.users.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sht.mapper.UsersMapper;
import com.sht.users.mapper.CustomUsersMapper;
import com.sht.users.po.CustomUsers;
import com.sht.users.service.UsersServiceI;
/**
 * Title:UsersService
 * <p>
 * Description:用户业务接口实现
 * <p>
 * @author Kor_Zhang
 * @date 2017年9月12日 上午11:24:50
 * @version 1.0
 */
@Service
public class UsersService extends UBaseService implements UsersServiceI {
	@Autowired
	private CustomUsersMapper customUsersMapper;

	@Autowired
	private UsersMapper usersMapper;
	
	@Override
	public CustomUsers login(CustomUsers po) throws Exception {
		
		
		CustomUsers dbUser = customUsersMapper.selectUserByUsername((String) po.getUsername());
		logger.info("UserService");
		//判断用户是否存在
		if(dbUser==null){
			dbUser = customUsersMapper.selectUserByEmail((String) po.getUsername());
		}
		
		eject(dbUser == null, "用户不存在");
		
		/**下面的代码必须不满足上面的条件**/
		eject(!dbUser.getPassword().equals(md5(po.getPassword() + dbUser.getSalt())), "密码错误");
		
		return dbUser;
		
	}

	@Override
	public void regist(CustomUsers po) throws Exception {
		
		CustomUsers dbUser = customUsersMapper.selectUserByUsername_reg((String) po.getUsername());
		
		if(dbUser==null){
			dbUser = customUsersMapper.selectUserByEmail_reg((String) po.getEmail());
		}
		//用户名相同
		eject(null!=dbUser && dbUser.getUsername().equals(po.getUsername()), "用户已存在");
		
		//邮箱相同
		eject(null!=dbUser && dbUser.getEmail().equals(po.getEmail()), "邮箱已存在");
		
		po.setId(UUID.randomUUID().toString());
		
		po.setEmail(po.getEmail());
		
		po.setSalt(po.getEmail());
		
		po.setBirthday(new Date());
		
		po.setDescription("good");

		po.setPassword(md5(po.getPassword() + po.getEmail()));
		
		po.setRegisttime(timestamp());
		
		po.setSex(Short.valueOf("1"));
		
		po.setStatus(Short.valueOf("-2"));
		
		po.setHeadimg("");
		
		po.setScore(1d);
		
		po.setMoney(Double.valueOf("0"));
		
		usersMapper.insert(po);
		
	}

	@Override
	public void verifyEmail(String email)  throws Exception{

		customUsersMapper.updateStatusByEmail(email);
		
	}

	@Override
	public void getGoodsById(String id) {
		// TODO Auto-generated method stub
		customUsersMapper.getGoodsById(id);
	}

	
	
	
}
