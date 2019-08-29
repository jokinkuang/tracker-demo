package com.uc.plugin.transform.base;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/7/31
 */
public abstract class BaseFilterChain<IN, OUT> implements IChain<IN, OUT>, IOperation<IN, OUT> {
    private List<IOperation<IN, OUT>> mOps = new LinkedList<>();
    private Integer mIndex;

    public BaseFilterChain() {
        // nop
    }

    @Override
    public boolean add(IOperation<IN, OUT> op) {
        return mOps.add(op);
    }

    @Override
    public boolean run(IN data) {
        mIndex = null;
        startNextOp(data);
        return true;
    }

    private void startOp(IOperation<IN, OUT> op, IN data) {
        if (op != null) {
            op.start(data, this);
        }
    }

    private IOperation<IN, OUT> getNextOp() {
        if (mIndex == null) {
            mIndex = 0;
        } else {
            mIndex++;
        }
        if (mIndex >= 0 && mIndex < mOps.size()) {
            return mOps.get(mIndex);
        }
        return null;
    }

    private void startNextOp(IN data) {
        startOp(getNextOp(), data);
    }


    @Override
    public void onRecall(IN data, OUT out) {
        // nop. can not recall
    }

    @Override
    public void onOk(IN data, OUT out) {
        startNextOp(data);
    }

    @Override
    public void onFail(IN data, OUT out) {
        // nop. break on failed
    }

    @Override
    public void start(IN data, IResult<IN, OUT> cb) {
        run(data);
    }
}
