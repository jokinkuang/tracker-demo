package com.uc.plugin.transform.data;


import groovy.lang.Closure;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class ClassDesc {
    public String ClassName = "";
    public String InterfaceName = "";
    public String MethodName = "";
    public String MethodDes = "";
    public Closure MethodVisitor;
    public boolean isAnnotation = false;
}
