package com.blog.blogservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.blog.blogservice.exception.AdminUserException;
import com.blog.blogservice.exception.UserNotFoundException;
import com.blog.blogservice.model.AdminUser;
import com.blog.blogservice.model.Post;
import com.blog.blogservice.model.User;
import com.blog.blogservice.util.BlogRestClient;

@Service
@Component
public class AdminServiceImpl implements AdminService {

	private static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	BlogRestClient restClient;

	@Override
	public User getUserById(String id) throws UserNotFoundException {

		logger.info("START getUserById");

		CompletableFuture<User> user = restClient.getUserById(id);
		CompletableFuture<List<Post>> posts = restClient.getAllPosts();
		
		User resp = null;
		try {
			if (null == user.get())
				throw new UserNotFoundException("User not found");
			else {
				resp = user.get();
				resp.setUserPosts(getUserPost(posts.get(), id));
			}
		} catch (InterruptedException  e) {
			logger.error(e.getMessage());
		}catch(ExecutionException exc){
			throw new UserNotFoundException("User not found");
		}catch(Exception ex)
		{
			logger.error(ex.getMessage());
			throw new UserNotFoundException("Service not found 503");
		}
		return resp;
	}

	@Override
	public User addUser(User user) {

		CompletableFuture<User> savedUser = restClient.addUser(user);
		try {
			return savedUser.get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public boolean isAdminUser(AdminUser admUser) throws AdminUserException {

		/*
		 * Here we mock the validation by checking username password and role.Ideally it
		 * should check with Database.
		 */
		if (null != admUser.getUserName() && !admUser.getUserName().equals("") && null != admUser.getUserPassword()
				&& !admUser.getUserPassword().equals("")) {
			if (admUser.getRole().trim().equals("admin"))
				return true;
			else
				throw new AdminUserException("User is not an Admin");

		} else
			throw new AdminUserException("Missing required fields");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blog.blogservice.service.AdminService#deleteUser(java.lang.String)
	 */
	@Override
	public void deleteUser(String userId) {
		restClient.deleteUser(userId);
	}

	
	/* (non-Javadoc)
	 * @see com.blog.blogservice.service.AdminService#getAllUserinfoWithPost()
	 */
	@Override
	public List<User> getAllUserinfoWithPost() {
		logger.info("START getAllUserinfoWithPost method");
		CompletableFuture<List<User>> user = restClient.getAllUsers();
		CompletableFuture<List<Post>> posts = restClient.getAllPosts();
		List<User> usersList = null;
		List<Post> allPosts;
		try {

			usersList = user.get();
			allPosts = posts.get();
			usersList.stream().forEach(item -> item.setUserPosts(getUserPost(allPosts, item.getId())));

		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
		logger.info("END getAllUserinfoWithPost method");
		return usersList;
	}

	/**
	 * @param postList
	 * @param userId
	 * @return
	 */
	private List<Post> getUserPost(List<Post> postList, String userId) {
		logger.info("START getUserPost");
		return postList.stream().filter(item -> userId.equals(item.getUserId())).collect(Collectors.toList());
	}

}
