package ywm.library.shared.model;

import com.wolf.lang.helper.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Herbert Yu on 2019-11-12 21:44
 */
public enum ArticleType {

    T1("心中江上如画"),
    COMPUTER("计算机/程序"),
    EMOTION("情感类"),
    SCIENCES("社科类"),
    OTHER("其他");

    private String desc;

    ArticleType(String desc) {
        this.desc = desc;
    }

    public static List<Map<String, String>> toListMap() {
        return Arrays.stream(ArticleType.values())
                .map(type -> Maps.of("name", type.name(), "desc", type.getDesc()))
                .collect(Collectors.toList());
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
