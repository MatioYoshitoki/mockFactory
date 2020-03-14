package com.mock.common.pojo;


import com.mock.common.global.CloudCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * json公用类
 * @author matio
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class JsonPublic<T> implements Serializable {
	//返回码
	private int code;
	//返回信息
	private String message;
	//返回的对象
	private T data;

	public JsonPublic(){
		this.code = CloudCode.SUCCESS_CODE;
		this.message = CloudCode.SUCCESS_MESSAGE;
	}

	public JsonPublic(Exception e){
		this(CloudCode.SYSTEM_EXCEPTION_CODE, CloudCode.SYSTEM_EXCEPTION_MESSAGE, null);
	}
}
