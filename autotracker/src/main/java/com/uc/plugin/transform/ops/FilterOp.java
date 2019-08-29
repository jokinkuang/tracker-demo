package com.uc.plugin.transform.ops;

import com.android.build.api.transform.QualifiedContent;
import com.uc.plugin.log.Logx;
import com.uc.plugin.transform.base.IResult;
import com.uc.plugin.transform.base.ITransformOp;

import java.io.File;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/7/31
 */
public class FilterOp implements ITransformOp<QualifiedContent, Object> {
    private static final String TAG = "FilterOp";

    @Override
    public void start(QualifiedContent data, IResult<QualifiedContent, Object> cb) {
        Object obj = new Object();

        Logx.d(TAG, "data:" + data.toString() + ", file:" + data.getFile());

        // if (data.getFile().isDirectory()) {
        //     dump(data.getFile(), 1);
        //     Logx.d(TAG, "data:"+data.toString()+", file:"+data.getFile());
        // } else {
        //     Logx.d(TAG, "data:" + data.toString() + ", file:" + data.getFile());
        // }

        cb.onFail(data, obj);
    }

    private void dump(File file, int space) {
        if (file.isDirectory()) {
            Logx.d(TAG, "dir:"+file.getPath());
            space++;
            for (File subFile : file.listFiles()) {
                dump(subFile, space);
            }
        }
        String prefix = "";
        for (int i = 0; i < space; ++i) {
            prefix = prefix + "-";
        }
        Logx.d(TAG, prefix + "file:"+file.getPath());
    }
}
