package com.uc.autotracker.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/15
 */
public class TData implements Cloneable {
    private String mName = "";
    private Map<String, String> mDatas = new HashMap<>();

    public TData(String name) {
        mName = name;
    }

    public void addProperty(String key, String val) {
        mDatas.put(key, val);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Entry{" +
                "mName='" + mName + '\'' +
                ", mDatas=" + mDatas +
                '}';
    }
}
