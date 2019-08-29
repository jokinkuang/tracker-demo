package com.uc.plugin.transform.base;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public abstract class BaseChain<IN, OUT> implements IChain<IN, OUT>, IOperation<IN, OUT> {
    @Override
    public boolean add(IOperation<IN, OUT> op) {
        return false;
    }

    @Override
    public boolean run(IN data) {
        return false;
    }

    @Override
    public void start(IN data, IResult<IN, OUT> cb) {

    }

    @Override
    public void onRecall(IN data, OUT obj) {

    }

    @Override
    public void onOk(IN data, OUT obj) {

    }

    @Override
    public void onFail(IN data, OUT obj) {

    }
}
