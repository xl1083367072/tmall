package com.xl.tmall.utils;

//restful风格返回的json信息
public class Result {

    private static final int SUCCESS =  0;
    private static final int FAIL = 1;

    private int code;
    private String message;
    private Object data;

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

//    返回成功信息
    public static Result success(){
        return new Result(SUCCESS,null,null);
    }

//    成功并返回数据
    public static Result success(Object data){
        return new Result(SUCCESS,null,data);
    }

//    失败并返回错误信息
    public static Result fail(String message){
        return new Result(FAIL,message,null);
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static int getSUCCESS() {
        return SUCCESS;
    }

    public static int getFAIL() {
        return FAIL;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
