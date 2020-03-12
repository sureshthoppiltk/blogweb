package com.blog.blogservice.service;

import java.util.List;

import com.blog.blogservice.exception.AdminUserException;
import com.blog.blogservice.exception.UserNotFoundException;
import com.blog.blogservice.model.AdminUser;
import com.blog.blogservice.model.User;

public interface AdminService {
	
	User getUserById(String userId) throws UserNotFoundException;
	User addUser(User user); 
	void deleteUser(String userId);
	boolean isAdminUser(AdminUser admUser) throws AdminUserException;
	List<User> getAllUserinfoWithPost();

}
