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
public class Instance {
    private String mName = "";
    private Map<String, Object> mDatas = new HashMap<>();

    public Instance(String name) {
        mName = name;
    }

    public Instance(String name, Map<String, Object> map) {
        mName = name;
        mDatas.putAll(map);
    }

    public void addProperty(String key, Object val) {
        mDatas.put(key, val);
    }

    @Override
    public String toString() {
        return "Instance{" +
                "mName='" + mName + '\'' +
                ", mDatas=" + mDatas +
                '}';
    }
}
