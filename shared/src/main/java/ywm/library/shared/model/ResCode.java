package ywm.library.shared.model;


public enum ResCode {

    OK(200, "OK"),
    FORBIDDEN(403, "Forbidden"),
    UNAUTHORIZED(401, "Unauthorized"),
    FAILED(500, "Internal Server Error"),
    BAD_GATEWAY(502, "Bad Gateway");

    private int code;

    private String desc;

    ResCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }}
