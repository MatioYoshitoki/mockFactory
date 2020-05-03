package com.mock.message.service;

import com.mock.common.exception.ExceptionPlus;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;

/**
 * Created by matioyoshitoki on 2020/2/9.
 */
public interface MessageService {

    String sendMsg(String phoneNo) throws HTTPException, IOException, ExceptionPlus;

}
