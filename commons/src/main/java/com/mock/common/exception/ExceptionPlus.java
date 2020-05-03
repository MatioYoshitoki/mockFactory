package com.mock.common.exception;

import com.mock.common.global.CloudCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionPlus extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int code;
	
	private String message;

	public ExceptionPlus(){
		this.code = CloudCode.SYSTEM_EXCEPTION_CODE;
		this.message = CloudCode.SYSTEM_EXCEPTION_MESSAGE;
	}


}
