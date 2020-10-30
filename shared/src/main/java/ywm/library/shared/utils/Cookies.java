package ywm.library.shared.utils;

import com.wolf.lang.helper.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * Created by Herbert Yu on 2020-10-26 23:43
 */
public class Cookies {

    private static Logger logger = LoggerFactory.getLogger(Cookies.class);

    public static String resolve(String name, HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, name);
        return cookie == null ? Strings.EMPTY : cookie.getValue();
    }

    public static void clear(String name, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            logger.debug("清除cookie:[{}]信息", name);
        }
    }

    public static void of(String name, String value, int maxAge, TimeUnit timeUnit, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) timeUnit.toSeconds((long) maxAge));
        cookie.setPath("/");
        response.addCookie(cookie);
        logger.debug("设置cookie:[{}]信息", name);
    }

}
