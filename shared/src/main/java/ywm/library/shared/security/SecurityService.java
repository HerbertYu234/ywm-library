package ywm.library.shared.security;

import com.wolf.lang.Landholder;
import com.wolf.lang.LandholderProvider;
import com.wolf.lang.helper.Strings;
import com.wolf.security.service.WolfSecurityOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ywm.library.shared.YwmSpecification;
import ywm.library.shared.remote.SharedUserService;
import ywm.library.shared.utils.Cookies;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ywm.library.shared.YwmSpecification.TICKET;

/**
 * Created by Herbert Yu on 2020-10-26 23:20
 */
public interface SecurityService extends LandholderProvider {

    /**
     * 验证ticket是否有效
     */
    boolean validate(String ticket);

    /**
     * 通过ticket拿到用户数据
     */
    SecurityUser profile(String ticket);

    /**
     * 登出
     */
    void logout(HttpServletRequest request, HttpServletResponse res);


    /**
     * 从request中获取ticket
     */
    default String getTicket(HttpServletRequest request, HttpServletResponse response) {
        return Cookies.resolve(TICKET, request);
    }


    @FeignClient(value = "gateway")
    interface Feign {
        /**
         * 验证ticket
         */
        @RequestMapping("/validate/{ticket}")
        boolean validate(@PathVariable("ticket") String ticket);


        /**
         * 获取profile
         */
        @RequestMapping("/profile/{ticket}")
        SecurityUser profile(@PathVariable("ticket") String ticket);


        /**
         * 获取profile
         */
        @RequestMapping("/logout/{ticket}")
        void logout(@PathVariable("ticket") String ticket);
    }

    @Component
    class Proxy implements SecurityService, YwmSpecification {

        @Autowired
        private SharedUserService sharedUserService;

        @Autowired
        private ApplicationContext applicationContext;

        private Feign getFeign() {
            return applicationContext.getBean(Feign.class);
        }

        @Override
        public boolean validate(String ticket) {
            if (Strings.isBlank(ticket))
                return false;
            return getFeign().validate(ticket);
        }

        @Override
        public SecurityUser profile(String ticket) {
            return Strings.isBlank(ticket) ? null : getFeign().profile(ticket);
        }


        @Override
        public void logout(HttpServletRequest request, HttpServletResponse response) {
            getFeign().logout(Cookies.resolve(TICKET, request));
            /**
             * 删除cookie
             */
            Cookies.clear(TICKET, request, response);
            if (applicationContext.getBean(WolfSecurityOperations.class) != null) {
                //shiro logout
                applicationContext.getBean(WolfSecurityOperations.class).logout();
            }
        }

        @Override
        public Landholder resolveByPrincipal(String principal) {
            SecurityUser securityUser = null;
            try {
                securityUser = SecurityUser.of(sharedUserService.findByUsername(principal));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return securityUser;
        }

        @Override
        public Landholder resolveById(String id) {
            SecurityUser securityUser = null;
            try {
                securityUser = SecurityUser.of(sharedUserService.findById(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return securityUser;
        }

    }

}
