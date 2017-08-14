package cn.deepclue.demo.app.common;

import cn.deepclue.demo.app.common.exception.ErrorCode;

/**
 * Created by luoyong on 17-7-3.
 */
public class Response<T> {
    private int errno = 0;
    private String errmsg = "success";
    private T data;

    public Response() {
    }

    public Response(T data) {
        this.data = data;
    }

    public Response(int errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }

    public Response(int errno, String errmsg, T data) {
        this.errno = errno;
        this.errmsg = errmsg;
        this.data = data;
    }

    public Response(ErrorCode errorCode) {
        this.errno = errorCode.getCode();
        this.errmsg = errorCode.getMessage();
    }

    public int getErrno() {
        return errno;
    }


    public String getErrmsg() {
        return errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
