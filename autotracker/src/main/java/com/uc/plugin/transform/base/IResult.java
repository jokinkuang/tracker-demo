package com.uc.plugin.transform.base;

import com.sun.istack.Nullable;

public interface IResult<IN, OUT> {
    /**
     * 回溯
     */
    void onRecall(@Nullable IN data, OUT obj);

    /**
     * 成功
     */
    void onOk(@Nullable IN data, OUT obj);

    /**
     * 失败
     */
    void onFail(@Nullable IN data, OUT obj);

}