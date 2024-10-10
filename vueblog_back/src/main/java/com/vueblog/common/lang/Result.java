package com.vueblog.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;
    public static Result succ(Object data) {

        return succ(200,"操作成功",data);
    }

    public static Result succ(int code,String mess, Object data) {
        Result m = new Result();
        m.setCode(code);
        m.setData(data);
        m.setMsg(mess);

        return m;
    }
    public static Result fail(String mess) {
        Result m = new Result();
        m.setCode(400);
        m.setData(null);
        m.setMsg(mess);
        return m;
    }

    public static Result fail(Object data){
        return fail(400,"操作失败",data);
    }
    public static Result fail(int code,String mess, Object data) {
        Result m = new Result();
        m.setCode(code);
        m.setData(data);
        m.setMsg(mess);
        return m;
    }
}