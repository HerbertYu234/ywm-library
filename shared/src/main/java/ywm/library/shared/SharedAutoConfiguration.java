package ywm.library.shared;

import com.wolf.lang.helper.Strings;
import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ywm.library.shared.security.SecurityService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Herbert Yu on 2019-11-12 22:35
 */
@Configuration
@EnableFeignClients
public class SharedAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    @ConditionalOnMissingBean(name = {"ywm.gateway.security.manager"})
    public SecurityService securityService() {
        return new SecurityService.Proxy();
    }


    /**
     * Feign Interceptor, 用于发起feign远程调用的时候, 附加权限相关的参数
     */
    @Bean
    @ConditionalOnMissingBean(name = {"ywm.gateway.security.manager"})
    public RequestInterceptor securityFeignInterceptor() {
        return template -> {
            try {
                /**
                 * RequestContextHolder.currentRequestAttributes()可能异常
                 */
                if (RequestContextHolder.getRequestAttributes() != null) {
                    logger.info("*** Shared Feign Interceptor");
                    ServletRequestAttributes attributes =
                            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    HttpServletRequest request = attributes.getRequest();
                    HttpServletResponse response = attributes.getResponse();

//                    /**
//                     * 如果有模拟supervisor的request.attribute
//                     */
//                    if (request.getAttribute("__source__") != null) {
//                        template.header("__source__", String.valueOf(request.getAttribute("__source__")));
//                    }
//
//                    if (request.getAttribute("__from__") != null) {
//                        template.header("__from__", String.valueOf(request.getAttribute("__from__")));
//                    }

                    /**
                     * 查看header中有没有_security请求头
                     */
                    String ticket = Strings.defaultIfBlank((String) request.getAttribute("_ticket"),
                            securityService().getTicket(request, response));


                    ticket = Strings.defaultIfBlank(ticket, request.getHeader("_ticket"));
                    ticket = Strings.defaultIfBlank(ticket, request.getParameter("_ticket"));

                    if (Strings.isNotBlank(ticket)) {
                        template.header("_ticket", ticket);
                    }
                }
            } catch (Exception ex) {
                logger.info("Feign远程调用发生错误,{},忽略异常", ex.getMessage());
            }
        };
    }

}
