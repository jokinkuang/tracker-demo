package com.uc.plugin.transform.asm;

import com.uc.plugin.PluginSettings;
import com.uc.plugin.log.Logx;
import com.uc.plugin.transform.data.ClassDesc;
import com.uc.plugin.transform.data.HookConfig;
import com.uc.plugin.transform.data.MethodData;
import com.uc.plugin.transform.util.AnalyticsUtil;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import groovy.lang.Closure;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class AsmClassVisitor extends ClassVisitor {
    private static final String TAG = "AsmClassVisitor";
    private HashSet<String> visitedFragMethods = new HashSet<>();


    private String mClassName;
    private String mSuperName;
    private String[] mInterfaces;
    private ClassVisitor classVisitor;

    public AsmClassVisitor(final ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor);
        this.classVisitor = classVisitor;
    }

    /**
     * 该方法是当扫描类时第一个拜访的方法，主要用于类声明使用
     * @param version 表示类版本：51，表示 “.class” 文件的版本是 JDK 1.7
     * @param access 类的修饰符：修饰符在 ASM 中是以 “ACC_” 开头的常量进行定义。
     *                          可以作用到类级别上的修饰符有：ACC_PUBLIC（public）、ACC_PRIVATE（private）、ACC_PROTECTED（protected）、
     *                          ACC_FINAL（final）、ACC_SUPER（extends）、ACC_INTERFACE（接口）、ACC_ABSTRACT（抽象类）、
     *                          ACC_ANNOTATION（注解类型）、ACC_ENUM（枚举类型）、ACC_DEPRECATED（标记了@Deprecated注解的类）、ACC_SYNTHETIC
     * @param name 类的名称：通常我们的类完整类名使用 “org.test.mypackage.MyClass” 来表示，但是到了字节码中会以路径形式表示它们 “org/test/mypackage/MyClass” 。
     *                      值得注意的是虽然是路径表示法但是不需要写明类的 “.class” 扩展名。
     * @param signature 表示泛型信息，如果类并未定义任何泛型该参数为空
     * @param superName 表示所继承的父类：由于 Java 的类是单根结构，即所有类都继承自 java.lang.Object。 因此可以简单的理解为任何类都会具有一个父类。
     *                  虽然在编写 Java 程序时我们没有去写 extends 关键字去明确继承的父类，但是 JDK在编译时 总会为我们加上 “ extends Object”。
     * @param interfaces 表示类实现的接口，在 Java 中类是可以实现多个不同的接口因此此处是一个数组。
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        mClassName = name;
        mInterfaces = interfaces;
        mSuperName = superName;
        // 打印调试信息
        Logx.i(TAG, "\n||---开始扫描类：${mClassName}"+mClassName);
        Logx.i(TAG,"||---类详情：version=${version};\taccess=${Logger.accCode2String(access)};\tname=${name};\tsignature=${signature};" +
                "\tsuperName=${superName};\tinterfaces=${interfaces.toArrayString()}");
        Logx.i(TAG, "visit() called with: version = [" + version + "], access = [" + access + "], name = [" + name + "], signature = [" + signature + "], superName = [" + superName + "]");

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(name, outerName, innerName, access);
    }

    /**
     *  该方法是当扫描器扫描到类的方法时进行调用
     * @param access 表示方法的修饰符
     * @param name 表示方法名，在 ASM 中 “visitMethod” 方法会处理（构造方法、静态代码块、私有方法、受保护的方法、共有方法、native类型方法）。
     *                  在这些范畴中构造方法的方法名为 “<init>”，静态代码块的方法名为 “<clinit>”。
     * @param desc 表示方法签名，方法签名的格式如下：“(参数列表)返回值类型”
     * @param signature 凡是具有泛型信息的方法，该参数都会有值。并且该值的内容信息基本等于第三个参数的拷贝，只不过不同的是泛型参数被特殊标记出来
     * @param exceptions 用来表示将会抛出的异常，如果方法不会抛出异常，则该参数为空
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        MethodVisitor adapter = null;

        // 1、采集日志SDK埋点检测
        if (PluginSettings.get().isOpenLogTrack) {
            MethodVisitor asmMethodVisitor = new AsmMethodVisitor(methodVisitor, access, name, desc, mSuperName, mClassName, mInterfaces, visitedFragMethods);
            adapter = asmMethodVisitor;
        }

        // 2、用户在build.gradle自定义的MethodVisitor
        // List<ClassDesc> autoClassFilter = GlobalConfig.getAutoClassFilter();
        // autoClassFilter.each {
        //     AutoClassFilter filter ->
        //     if (AutoMatchUtil.isShouldModifyCustomMethod(filter, mClassName, name, desc, mInterfaces)) {
        //         MethodVisitor userMethodVisitor
        //         if (adapter == null) {
        //             userMethodVisitor = getSettingMethodVisitor(filter, methodVisitor, access, name, desc)
        //         } else {
        //             userMethodVisitor = getSettingMethodVisitor(filter, adapter, access, name, desc)
        //         }
        //         adapter = userMethodVisitor
        //     }
        // }

        if (adapter != null) {
            return adapter;
        }
        return methodVisitor;
    }

    /**
     * 该方法是当扫描器完成类扫描时才会调用，如果想在类中追加某些方法，可以在该方法中实现。
     */
    @Override
    public void visitEnd() {
        if (PluginSettings.get().isOpenLogTrack && AnalyticsUtil.isInstanceOfFragment(mSuperName)) {
            MethodVisitor mv;
            // 添加剩下的方法，确保super.onHiddenChanged(hidden);等先被调用
            Iterator<Map.Entry<String, MethodData>> iterator = HookConfig.sFragmentMethods.entrySet().iterator();
//            Logger.info("visitedFragMethods:" + visitedFragMethods)
            while (iterator.hasNext()) {
                Map.Entry<String, MethodData> entry = iterator.next();
                String key = entry.getKey();
                MethodData methodCell = entry.getValue();
                if (visitedFragMethods.contains(key)) {
                    continue;
                }
                Logx.i(TAG, "||Hooked class:injected method:" + methodCell.agentName);
                mv = classVisitor.visitMethod(Opcodes.ACC_PUBLIC, methodCell.name, methodCell.desc, null, null);
                mv.visitCode();
                // call super
                AnalyticsUtil.visitMethodWithLoadedParams(mv, Opcodes.INVOKESPECIAL, mSuperName, methodCell.name, methodCell.desc, methodCell.paramsStart, methodCell.paramsCount, methodCell.opcodes);
                // call injected method
                AnalyticsUtil.visitMethodWithLoadedParams(mv, Opcodes.INVOKESTATIC, HookConfig.LOG_ANALYTICS_BASE, methodCell.agentName,
                        methodCell.agentDesc, methodCell.paramsStart, methodCell.paramsCount, methodCell.opcodes);
                mv.visitInsn(Opcodes.RETURN);
                mv.visitMaxs(methodCell.paramsCount, methodCell.paramsCount);
                mv.visitEnd();
                mv.visitAnnotation("Lcom/mmc/lamandys/liba_datapick/AutoDataInstrumented;", false);
            }
        }
        Logx.i(TAG, "||---结束扫描类：${mClassName}\n"+mClassName);
        super.visitEnd();
    }

    /**
     * app的module里头设置的自动埋点方法修改器
     *
     * @param methodVisitor 需要修改的方法
     * @param name 方法名
     * @param desc 参数描述符
     */
    private static MethodVisitor getSettingMethodVisitor(ClassDesc filter,
                                                         MethodVisitor methodVisitor, int access, String name, String desc) {
        MethodVisitor adapter = null;
        Closure vivi = filter.MethodVisitor;
        if (vivi != null) {
            try {
                adapter = (MethodVisitor) vivi.call(methodVisitor, access, name, desc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adapter;
    }
}
