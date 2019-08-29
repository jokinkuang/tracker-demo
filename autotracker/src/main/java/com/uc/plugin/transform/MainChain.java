package com.uc.plugin.transform;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.TransformInput;
import com.uc.plugin.transform.base.BaseChain;
import com.uc.plugin.transform.base.BaseTaskChain;
import com.uc.plugin.transform.base.IOperation;
import com.uc.plugin.transform.data.TransformData;
import com.uc.plugin.transform.ops.DirectoryFilterOp;
import com.uc.plugin.transform.ops.DirectoryTask;
import com.uc.plugin.transform.ops.JarTask;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 主链路器
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class MainChain extends BaseChain<TransformData, TransformData> {
    private static final String TAG = "MainChain";

    private DirectoryTask mDirectoryTask = new DirectoryTask();
    private JarTask mJarTask = new JarTask();

    public MainChain() {

    }

    @Override
    public boolean run(TransformData data) {
        for (TransformInput input : data.getInputs()) {
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                mDirectoryTask.run(new TransformData(data.getTransformInvocation(), directoryInput));
            }
            for (JarInput jarInput : input.getJarInputs()) {
                mJarTask.run(new TransformData(data.getTransformInvocation(), jarInput));
            }
        }
        return true;
    }
}
