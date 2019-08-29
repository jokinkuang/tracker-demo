package com.uc.plugin.transform;

import com.android.build.api.transform.QualifiedContent;
import com.uc.plugin.transform.base.BaseTaskChain;
import com.uc.plugin.transform.base.IOperation;
import com.uc.plugin.transform.base.IResult;
import com.uc.plugin.transform.data.TransformData;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/7/31
 */
public class TransformTaskChain extends BaseTaskChain<TransformData, TransformData>
        implements IOperation<TransformData, TransformData> {
}
