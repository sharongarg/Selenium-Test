package com.SeleniumFramework.test; 
@SuppressWarnings("serial")
public class ExcelUtilException extends Exception{
	
	public ExcelUtilException(String message){
		super("Error *****" + message + "*****");
	}
	
	public ExcelUtilException(String message, Throwable  cause){
		super(message, cause);
	}

}