package com.uc.plugin.transform.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class AsmClassModifier {
    private static final String TAG = "AsmClassModifier";
    public static byte[] modifyClasses(String className, byte[] srcByteCode) {
        byte[] classBytesCode = null;
        try {
            classBytesCode = modifyClass(srcByteCode);
            //调试模式下再遍历一遍看修改的方法情况
//            if (Logger.isDebug()) {
//                seeModifyMethod(classBytesCode)
//            }
            return classBytesCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (classBytesCode == null) {
            classBytesCode = srcByteCode;
        }
        return classBytesCode;
    }
    /**
     * 真正修改类中方法字节码
     */
    private static byte[] modifyClass(byte[] srcClass) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor adapter = new AsmClassVisitor(classWriter);
        ClassReader cr = new ClassReader(srcClass);
        cr.accept(adapter, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
}
