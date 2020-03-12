package com.blog.blogservice.exception;

public class UserNotFoundException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Method to log Exception with String
	 * @param String
	 */
	public UserNotFoundException(String message) {
		super(message);
	}
	
	/**
	 * Method to log Exception with String
	 * @param String
	 * @param Throwable
	 */
	public UserNotFoundException(String message, Throwable t)
	{
		super(message,t);
	}
	

}
