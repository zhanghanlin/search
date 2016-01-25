package com.search.web.response;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class ResponseContent<T> implements Serializable {

    private static final long serialVersionUID = -7967643212994055628L;

    private int code;

    private String msg;

    private T data;

    @JSONField(serialize = false)
    private String callback;

    public ResponseContent() {
        super();
    }

    public ResponseContent(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ResponseContent(int code, String msg, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseContent(int code, String msg, String callback) {
        super();
        this.code = code;
        this.msg = msg;
        this.callback = callback;
    }

    public ResponseContent(int code, String msg, T data, String callback) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.callback = callback;
    }

    public ResponseContent(ResponseEnum responseEnum) {
        super();
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getMsg();
    }

    public ResponseContent(ResponseEnum responseEnum, T data) {
        super();
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getMsg();
        this.data = data;
    }

    public ResponseContent(ResponseEnum responseEnum, T data, String callback) {
        super();
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getMsg();
        this.data = data;
        this.callback = callback;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
