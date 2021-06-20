package com.xgs.hisystem.common.exception;


import com.xgs.hisystem.common.enums.ResponseErrorEnum;

public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private ResponseErrorEnum errorEnum;

    public BusinessException(String msg) {

        super(msg);
    }

    public BusinessException(ResponseErrorEnum respHeaderErrorEnum){
        super(respHeaderErrorEnum.getMsg());
        errorEnum = respHeaderErrorEnum;
    }

    public BusinessException(ResponseErrorEnum respHeaderErrorEnum, String message){
        super(message);
        errorEnum = respHeaderErrorEnum;
    }

    public ResponseErrorEnum getErrorEnum() {

        return errorEnum;
    }

    public void setErrorEnum(ResponseErrorEnum errorEnum) {

        this.errorEnum = errorEnum;
    }





}
