package ywm.library.shared.security;

import com.wolf.lang.Landholder;
import lombok.Data;
import org.apache.commons.beanutils.PropertyUtils;
import ywm.library.shared.model.UserDTO;

import java.util.Objects;

/**
 * Created by Herbert Yu on 2020-10-26 16:10
 */
@Data
public class SecurityUser extends UserDTO implements Landholder {

    public SecurityUser() {
    }

    public static SecurityUser of(UserDTO delegate) throws Exception {
        if (Objects.isNull(delegate))
            return null;
        SecurityUser user = new SecurityUser();
        PropertyUtils.copyProperties(user, delegate);
        return user;
    }

    /**
     * 登录ticket
     */
    private String ticket;

    /**
     * 拥有的权限对象
     */
    private Security security;

    @Override
    public String getPrincipal() {
        return getUsername();
    }

    @Override
    public String getCredential() {
        return getPassword();
    }

}
