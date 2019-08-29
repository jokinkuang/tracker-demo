package com.uc.plugin.transform.ops;

import com.android.build.api.transform.QualifiedContent;
import com.uc.plugin.log.Logx;
import com.uc.plugin.transform.base.IResult;
import com.uc.plugin.transform.base.ITransformOp;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/7/31
 */
public class FilterOp2 implements ITransformOp<QualifiedContent, Object> {
    private static final String TAG = "FilterOp2";
    @Override
    public void start(QualifiedContent data, IResult<QualifiedContent, Object> cb) {
        Object obj = new Object();

        Logx.d(TAG, "file:"+data.getFile().getPath());
        
        cb.onOk(data, obj);
    }
}
