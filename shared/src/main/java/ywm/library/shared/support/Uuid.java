package ywm.library.shared.support;


import com.wolf.lang.datetime.Dates;
import com.wolf.lang.helper.Strings;

import java.util.Date;

/**
 * Created by Herbert Yu on 2019-11-14 13:22
 */
public interface Uuid {

    Dates.Formatter DATE_FORMATTER = new Dates.Formatter("yyyyMMdd");

    interface ArticleTag {
        String prefix = "AT";
        String sep = "";

        static String generate() {
            return prefix + sep + DATE_FORMATTER.format(new Date()) + sep
                    + Strings.randomAlphanumeric(6).toUpperCase();
        }
    }

}
