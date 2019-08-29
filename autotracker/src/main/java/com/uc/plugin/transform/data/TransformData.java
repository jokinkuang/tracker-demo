package com.uc.plugin.transform.data;

import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;

import java.io.File;
import java.util.Collection;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class TransformData {
    private TransformInvocation mTransformInvocation;
    private QualifiedContent mInput;
    private File mOutput;

    public TransformData(TransformInvocation transformInvocation) {
        mTransformInvocation = transformInvocation;
    }

    public TransformData(TransformInvocation invocation, QualifiedContent input) {
        this.mTransformInvocation = invocation;
        this.mInput = input;
    }

    public TransformInvocation getTransformInvocation() {
        return mTransformInvocation;
    }

    public Collection<TransformInput> getInputs() {
        return mTransformInvocation.getInputs();
    }

    public QualifiedContent getInput() {
        return mInput;
    }

    public File getInputDir() {
        return getTransformInvocation().getOutputProvider().getContentLocation(mInput.getName(),
                mInput.getContentTypes(), mInput.getScopes(), Format.DIRECTORY);
    }

    public File getOutput() {
        return mOutput;
    }
}
