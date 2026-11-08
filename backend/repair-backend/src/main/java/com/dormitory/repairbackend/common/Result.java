package com.dormitory.repairbackend.common;

public class Result<T> {
    private Integer code; // 200表示成功，500表示失败
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> res = new Result<>();
        res.code = 200;
        res.message = "操作成功";
        res.data = data;
        return res;
    }

    public static <T> Result<T> error(String message) {
        Result<T> res = new Result<>();
        res.code = 500;
        res.message = message;
        return res;
    }

    // Getters and Setters
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}