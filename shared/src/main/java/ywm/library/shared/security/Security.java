package ywm.library.shared.security;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Herbert Yu on 2020-10-26 16:12
 */
@Data
public class Security implements Serializable {

    /**
     * Feign调用发生错误,HTTP[0,JSON parse error: Can not construct instance of ywm.library.shared.security.Security: no suitable constructor found, can not deserialize from Object value (missing default constructor or creator, or perhaps need to add/enable type information?); nested exception is com.fasterxml.jackson.databind.JsonMappingException: Can not construct instance of ywm.library.shared.security.Security: no suitable constructor found, can not deserialize from Object value (missing default constructor or creator, or perhaps need to add/enable type information?)
     */
    public Security() {
    }

    public Security(String uid, String[] resources) {
        this.uid = uid;
        this.resources = resources;
    }

    //用户id
    private String uid;

    /**
     * 所能管辖的资源权限
     */
    private String[] resources;

}
