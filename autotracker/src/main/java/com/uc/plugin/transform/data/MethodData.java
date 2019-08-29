package com.uc.plugin.transform.data;

import java.util.List;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class MethodData {
    // 原方法名
    public String name;
    // 原方法描述
    public String desc;
    // 方法所在的接口或类
    public String parent;
    // 采集数据的方法名
    public String agentName;
    // 采集数据的方法描述
    public String agentDesc;
    // 采集数据的方法参数起始索引（ 0：this，1+：普通参数 ）
    public int paramsStart;
    // 采集数据的方法参数个数
    public int paramsCount;
    // 参数类型对应的ASM指令，加载不同类型的参数需要不同的指令
    public int[] opcodes;

    public MethodData(String name, String desc, String parent, String agentName, String agentDesc, int paramsStart, int paramsCount, int[] opcodes) {
        this.name = name;
        this.desc = desc;
        this.parent = parent;
        this.agentName = agentName;
        this.agentDesc = agentDesc;
        this.paramsStart = paramsStart;
        this.paramsCount = paramsCount;
        this.opcodes = opcodes;
    }
}
