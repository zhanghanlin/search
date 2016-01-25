package com.search.web.response;

public enum ResponseEnum {

    OK(200, "成功"),
    NOT_FOUND(404, "未找到"),
    EXCEPTION(500, "内部错误");

    private int code;
    private String msg;

    ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
