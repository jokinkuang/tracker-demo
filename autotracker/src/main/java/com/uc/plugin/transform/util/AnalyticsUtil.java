package com.uc.plugin.transform.util;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashSet;
import java.util.List;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class AnalyticsUtil implements Opcodes {
    private static final HashSet<String> targetFragmentClass = new HashSet<String>();
    private static final HashSet<String> targetMenuMethodDesc = new HashSet<String>();

    static {
        /**
         * Menu
         */
        targetMenuMethodDesc.add("onContextItemSelected(Landroid/view/MenuItem;)Z");
        targetMenuMethodDesc.add("onOptionsItemSelected(Landroid/view/MenuItem;)Z");
        targetMenuMethodDesc.add("onNavigationItemSelected(Landroid/view/MenuItem;)Z");

        /**
         * Fragment
         */
        targetFragmentClass.add("android/support/v4/app/Fragment");
        targetFragmentClass.add("android/support/v4/app/ListFragment");
        targetFragmentClass.add("android/support/v4/app/DialogFragment");

        /**
         * For AndroidX Fragment
         */
        targetFragmentClass.add("androidx/fragment/app/Fragment");
        targetFragmentClass.add("androidx/fragment/app/ListFragment");
        targetFragmentClass.add("androidx/fragment/app/DialogFragment");
    }

    public static boolean isSynthetic(int access) {
        return (access & ACC_SYNTHETIC) != 0;
    }

    public static boolean isPrivate(int access) {
        return (access & ACC_PRIVATE) != 0;
    }

    public static boolean isPublic(int access) {
        return (access & ACC_PUBLIC) != 0;
    }

    public static boolean isStatic(int access) {
        return (access & ACC_STATIC) != 0;
    }

    public static boolean isTargetMenuMethodDesc(String nameDesc) {
        return targetMenuMethodDesc.contains(nameDesc);
    }

    public static boolean isTargetFragmentClass(String className) {
        return targetFragmentClass.contains(className);
    }

    public static boolean isInstanceOfFragment(String superName) {
        return targetFragmentClass.contains(superName);
    }

    public static void visitMethodWithLoadedParams(MethodVisitor methodVisitor, int opcode, String owner, String methodName, String methodDesc, int start, int count, int[] paramOpcodes) {
        for (int i = start; i < start + count; i++) {
            methodVisitor.visitVarInsn(paramOpcodes[i - start], i);
        }
        methodVisitor.visitMethodInsn(opcode, owner, methodName, methodDesc, false);
    }
}
