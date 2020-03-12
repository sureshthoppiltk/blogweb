package com.blog.blogservice.exception;

public class AdminUserException extends Exception{

	/**
	 * Method to log Exception with String
	 * @param String
	 */
	public AdminUserException(String message) {
		super(message);
	}
	
	/**
	 * Method to log Exception with String
	 * @param String
	 * @param Throwable
	 */
	public AdminUserException(String message, Throwable t)
	{
		super(message,t);
	}
}
