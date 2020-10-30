package ywm.library.shared.security;

import com.wolf.lang.helper.Strings;
import com.wolf.security.exception.WolfSecurityException;
import com.wolf.security.service.WolfSecurityOperations;
import com.wolf.security.support.filter.UserAuthenticatedFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import ywm.library.shared.YwmSpecification;
import ywm.library.shared.utils.Cookies;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * SecurityUserFilter
 * </p>
 * Created by snowway on 04/05/2018.
 */
public class SecurityUserFilter extends UserAuthenticatedFilter implements YwmSpecification {

    /**
     * Logger
     */
    private Logger logger = LoggerFactory.getLogger(getClass());


    private SecurityService securityService;
    private WolfSecurityOperations securityOperations;


    public SecurityUserFilter(ApplicationContext applicationContext, String sessionKey) {
        super(applicationContext, sessionKey);
        this.securityService = applicationContext.getBean(SecurityService.class);
        this.securityOperations = applicationContext.getBean(WolfSecurityOperations.class);
    }


    @Override
    protected boolean isAccessAllowed(ServletRequest req, ServletResponse res, Object mappedValue) {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(true);
        /**
         * 保存用户最近一次登录的ticket
         */
        String sessionLastTicket = (String) session.getAttribute(TICKET);
        String cookieTicket = Cookies.resolve(TICKET, request);
        String ticket = Strings.defaultIfBlank(request.getParameter(TICKET), cookieTicket);
        boolean allow = Strings.isNotBlank(ticket) && securityService.validate(ticket);
        /**
         * 如果允许登录并且cookie中的用户和_ticket不一致
         * 重写cookie过期时间
         */
        if (allow) {
            Cookies.clear(TICKET, request, response);
            Cookies.of(TICKET, ticket, 1, TimeUnit.DAYS, response);
        }
        boolean applogined = super.isAccessAllowed(req, res, mappedValue);
        /**
         * 网关授权成功, 但app本身无法登录, 则自动登录
         * 或者最后保存的ticket和本次ticket不一致, 可能切换了用户, 再次重新登录
         */
        if ((allow && !applogined) || !Strings.equals(sessionLastTicket, ticket)) {
            if (Strings.isNotBlank(ticket)) {
                SecurityUser user = securityService.profile(ticket);
                /**
                 * ticket也许在Gateway已经过期了
                 */
                if (user != null) {
                    try {
                        securityOperations.login(user.getUsername(), user.getPassword(), true);//模拟自动登录
                    } catch (WolfSecurityException ex) {
                        return false;   //认证失败, 一般是后台修改了密码,导致无法自动登录
                    }
                    logger.info("应用模拟用户登录成功,当前ticket:[{}]", ticket);
                }
            }
            session.setAttribute(TICKET, ticket);

        }
        return allow && super.isAccessAllowed(req, res, mappedValue);
    }


}
