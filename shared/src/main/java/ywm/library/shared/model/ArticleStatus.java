package ywm.library.shared.model;

import java.util.Arrays;

/**
 * Created by Herbert Yu on 2019-11-12 21:44
 */
public enum ArticleStatus {

    DEPLOY(0, "已发布"),
    UNDEPLOY(1, "未发布");

    private int code;
    private String desc;


    public static ArticleStatus fromCode(int code){
        return Arrays.stream(ArticleStatus.values()).filter(status->status.getCode()==code).findAny().orElse(null);
    }

    ArticleStatus(int code, String desc) {
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
    }
}
