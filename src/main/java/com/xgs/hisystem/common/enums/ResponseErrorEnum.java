package com.xgs.hisystem.common.enums;

/**
 * @author XueGuiSheng
 * @date 2020/11/25 11:16
 */
public enum ResponseErrorEnum {

    ;

    private String errCode;
    private String msg;

    ResponseErrorEnum(String errCode, String msg) {
        this.errCode = errCode;
        this.msg = msg;
    }

    public String getErrCode() {
        return errCode;
    }


    public String getMsg() {
        return msg;
    }

}
