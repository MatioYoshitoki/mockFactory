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


}
