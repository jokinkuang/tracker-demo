package com.uc.autotracker.model.sem;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/15
 */
public class Module {
    private String mName = "";
    private Map<String, Object> mDatas = new HashMap<>();

    public Module(String name) {
        mName = name;
    }

    public void addProperty(String key, Object val) {
        mDatas.put(key, val);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Module) {
            return ((Module) obj).mName.equals(mName);
        }
        return false;
    }

    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 == null || obj2 == null) {
            return false;
        }
        return obj1.equals(obj2);
    }

    @Override
    public String toString() {
        return "Module{" +
                "mName='" + mName + '\'' +
                ", mDatas=" + mDatas +
                '}';
    }
}
