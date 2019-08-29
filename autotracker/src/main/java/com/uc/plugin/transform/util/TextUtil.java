package com.uc.plugin.transform.util;

import java.io.File;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class TextUtil {
    public static boolean isEmpty(String text) {
        return text == null || text.trim().length() == 0;
    }

    public static String path2ClassName(String pathName) {
        return pathName.replace(File.separator, ".").replace(".class", "");
    }

    /**
     * "com.android.view" -> "com/android/view"
     */
    public static String classPath2JvmPath(String classPath) {
        return classPath.replace('.', '/');
    }
}
