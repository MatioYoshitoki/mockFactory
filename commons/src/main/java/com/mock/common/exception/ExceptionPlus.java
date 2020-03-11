package com.mock.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionPlus extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int code;
	
	private String message;

	/**
	 * 继承异常类，进行自己必要的格式整合
	 * @return
	 */
	public String getMessagePlus (){
		//to do
		return this.getMessage();
	}

}
