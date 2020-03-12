package com.blog.blogservice.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.blog.blogservice.controller.AdminController;
import com.blog.blogservice.model.Post;
import com.blog.blogservice.model.User;

@Service
public class BlogRestClient {

	private static Logger logger = LoggerFactory.getLogger(BlogRestClient.class);

	@Autowired
	RestTemplate restTemplate;

	@Value("${blogs.usersURL}")
	private String usersURL;

	@Value("${blogs.postsURL}")
	private String postsURL;

	@Async
	public CompletableFuture<User> getUserById(String userId) {
		return CompletableFuture.completedFuture(restTemplate.getForObject(usersURL + userId, User.class));
	}

	@Async
	public CompletableFuture<List<Post>> getUserPosts(String userId) {
		Post[] post = restTemplate.getForObject(usersURL + userId + "/posts", Post[].class);
		return CompletableFuture.completedFuture(Arrays.asList(post));
	}

	@Async
	public CompletableFuture<List<Post>> getAllPosts() {
		logger.info("getAllPosts invoked");
		Post[] post = restTemplate.getForObject(postsURL, Post[].class);
		logger.info("Template response : " + post);
		return CompletableFuture.completedFuture(Arrays.asList(post));
	}

	public CompletableFuture<List<User>> getAllUsers() {
		logger.info("getAllUsers invoked");
		User[] users = restTemplate.getForObject(usersURL, User[].class);
		logger.info("Template response : " + users);
		return CompletableFuture.completedFuture(Arrays.asList(users));
	}

	@Async
	public CompletableFuture<User> addUser(User user) {
		logger.info("addUser invoked");
		ResponseEntity<User> myuser = restTemplate.postForEntity(postsURL, user, User.class);
		logger.info("Response from template " + myuser.getStatusCode());
		if (myuser.getStatusCode() == HttpStatus.OK)
			return CompletableFuture.completedFuture(myuser.getBody());
		else
			return null;

	}

	@Async
	public void deleteUser(String userId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", userId);
		restTemplate.delete(usersURL + "{id}", params);

	}

	@Bean
	public RestTemplate rest() {
		return new RestTemplate();
	}
}
