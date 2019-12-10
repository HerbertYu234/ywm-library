package ywm.library.shared.model;


import com.wolf.lang.helper.Strings;

import java.io.Serializable;


public class ResEntity implements Serializable {

    private ResEntity() {
    }

    private static ResEntity of(int status, String msg, Object result) {
        ResEntity response = new ResEntity();
        response.setStatus(status);
        response.setMsg(msg);
        response.setResult(result);
        return response;
    }

    private static ResEntity of(int status, String msg) {
        return ResEntity.of(status, msg, null);
    }

    public static ResEntity of(ResCode statusCode, String msg) {
        return ResEntity.of(statusCode.getCode(), msg);
    }

    public static ResEntity of(ResCode code, String msg, Object result) {
        return of(code.getCode(), msg, result);
    }

    public static ResEntity success() {
        return success(null);
    }

    public static ResEntity success(String msg) {
        return ResEntity.of(ResCode.OK, msg);
    }

    public static ResEntity fail(String msg) {
        return ResEntity.of(ResCode.FAILED, msg);
    }

    public static ResEntity fail() {
        return fail(null);
    }

    public static ResEntity result(Object result) {
        return result(result, null);
    }

    public static ResEntity result(Object result, String msg) {
        return ResEntity.of(ResCode.OK.getCode(), msg, result);
    }

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 状态码
     */
    private int status;

    /**
     * 数据结果
     */
    private Object result;

    private boolean getSuccessByStatus(int status) {
        return status < 300;
    }

    public boolean isSuccess() {
        return success;
    }

    public ResEntity setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResEntity setMsg(String msg) {
        if (Strings.isBlank(msg)) {
            msg = getSuccessByStatus(this.status) ? "success" : "fail";
        }
        this.msg = msg;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public ResEntity setStatus(int status) {
        this.status = status;
        this.setSuccess(getSuccessByStatus(status));
        return this;
    }

    public Object getResult() {
        return result;
    }

    public ResEntity setResult(Object result) {
        this.result = result;
        return this;
    }

}
