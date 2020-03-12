package com.blog.blogservice.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blogservice.exception.AdminUserException;
import com.blog.blogservice.exception.UserNotFoundException;
import com.blog.blogservice.model.AdminUser;
import com.blog.blogservice.model.User;
import com.blog.blogservice.service.AdminService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@RestController
@RequestMapping("/blog")
public class AdminController {
	
	/**
	 * Member variable logger to hold log info,debug,error.
	 */
	private static Logger logger=LoggerFactory.getLogger(AdminController.class);
	
	private long  EXPIRATION_TIME = 100000000;
	
	@Autowired
	AdminService adminService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<String> getRoot() {
		logger.info("GET Blog Service ROOT Event");
		return new ResponseEntity<String>("You've reached the keepalive file for Blog Service", null, HttpStatus.OK);
	}
   
	/*
	 *  This method used to get  user information for a given userId and his posts.
	 * */

	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUserInfo(@PathVariable(value = "id") String id) {
		User user = null;
		try {
			user = adminService.getUserById(id);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			logger.error(e.getMessage());

			return new ResponseEntity<String>("User is not found", HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/users")
	public ResponseEntity<?> getAllUserInfoWithPosts()
	{
		return new ResponseEntity<List<User>>(adminService.getAllUserinfoWithPost(),HttpStatus.OK) ;
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<?> adminLogin(@RequestBody AdminUser admUser) {

		Map<String, String> map = new HashMap<String, String>();
		logger.info("START adminLogin invoked");
		try {

			if (adminService.isAdminUser(admUser)) {
				String token = getToken(admUser.getUserName(), admUser.getUserPassword());
				logger.info("---->Token is " + token);
				map.put("token", token);
				map.put("message", "Admin logged");
				return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
			} else {
				map.put("message", "Not an Admin");
				return new ResponseEntity<Map<String, String>>(map, HttpStatus.UNAUTHORIZED);
			}

		} catch (AdminUserException e) {
			logger.error(e.getMessage());

			map.put("message", e.getMessage());
			return new ResponseEntity<Map<String, String>>(map, HttpStatus.NOT_FOUND);
		} catch (Exception e) {

			logger.error(e.getMessage());
			map.put("message", e.getMessage());
			return new ResponseEntity<Map<String, String>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
	
	
	// Generate JWT token
		public String getToken(String userId, String password) throws Exception {
			logger.info("getToken method invoked");
			return Jwts.builder().setSubject(userId).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
			.signWith(SignatureAlgorithm.HS256, "secretKey").compact();
		}
}
