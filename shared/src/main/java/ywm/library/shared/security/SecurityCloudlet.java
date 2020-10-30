package ywm.library.shared.security;

import com.wolf.cloud.WolfCloudlet;
import com.wolf.web.config.WolfWebConfigurerAdapter;
import com.wolf.web.support.filter.WolfFilterRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Herbert Yu on 2020-10-28 15:31
 */
public abstract class SecurityCloudlet extends WolfWebConfigurerAdapter implements WolfCloudlet {

    protected Logger logger = LoggerFactory.getLogger(SecurityCloudlet.class);

    @Autowired
    private SecurityService securityService;


    @Override
    public void addFilters(WolfFilterRegister filterRegister) {
        super.addFilters(filterRegister);

        filterRegister.addFilter(new Filter() {

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
                /**
                 * 将ZUUL发送的_ticket头保存到后续的request.Attribute中
                 */
                HttpServletRequest request = (HttpServletRequest) req;
                String ticket = request.getHeader("_ticket");
                request.setAttribute("_ticket", ticket);

                logger.info("***将ZUUL发送的_ticket头保存到后续的request[{}]", ticket);

                chain.doFilter(req, res);
            }

            @Override
            public void destroy() {

            }
        });
    }
}
