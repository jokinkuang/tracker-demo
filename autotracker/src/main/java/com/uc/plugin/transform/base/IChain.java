package com.uc.plugin.transform.base;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/7/31
 */
public interface IChain<IN, OUT> extends IResult<IN, OUT> {
    boolean add(IOperation<IN, OUT> op);
    boolean run(IN data);
}
