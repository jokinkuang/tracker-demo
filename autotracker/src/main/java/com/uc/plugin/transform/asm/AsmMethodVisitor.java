package com.uc.plugin.transform.asm;


import com.uc.plugin.log.Logx;
import com.uc.plugin.transform.data.HookConfig;
import com.uc.plugin.transform.data.MethodData;
import com.uc.plugin.transform.util.AnalyticsUtil;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.HashSet;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class AsmMethodVisitor extends AdviceAdapter {
    private static final String TAG = "AsmMethodVisitor";
    public HashSet<String> visitedFragMethods;
    String methodName;
    int access;
    MethodVisitor methodVisitor;
    String methodDesc;
    String superName;
    String className;
    String[] interfaces;

    public AsmMethodVisitor(MethodVisitor methodVisitor, int access, String name, String desc,
                            String superName, String className, String[] interfaces, HashSet<String> visitedFragMethods) {
        super(Opcodes.ASM6, methodVisitor, access, name, desc);
        this.methodName = name;
        this.access = access;
        this.methodVisitor = methodVisitor;
        this.methodDesc = desc;
        this.superName = superName;
        this.className = className;
        this.interfaces = interfaces;
        this.visitedFragMethods = visitedFragMethods;
        Logx.i(TAG, "||开始扫描方法：${Logger.accCode2String(access)} ${methodName}${desc}"
                +access+","+methodName+","+desc);
    }

    boolean isAutoTrackViewOnClickAnnotation = false;
    boolean isAutoTrackIgnoreTrackOnClick = false;
    boolean isHasInstrumented = false;
    boolean isHasTracked = false;

    @Override
    public void visitEnd() {
        super.visitEnd();
        if (isHasTracked) {
            visitAnnotation("Lcom/mmc/lamandys/liba_datapick/AutoDataInstrumented;", false);
            Logx.e(TAG, "||Hooked method: ${methodName}${methodDesc}"+methodName+","+methodDesc);
        }
        Logx.i(TAG, "||结束扫描方法：${methodName}"+methodName);
    }

    private static String lclassName;
    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();

        if (lclassName == null || !lclassName.equals(className)) {
            // Logx.e(TAG, "class:"+className);
            lclassName = className;
        }


        // Logx.e(TAG, "method:"+className+","+methodDesc);

        if (isAutoTrackIgnoreTrackOnClick) {
            return;
        }

        /**
         * 在 android.gradle 的 3.2.1 版本中，针对 view 的 setOnClickListener 方法 的 lambda 表达式做特殊处理。
         */
        if (methodName.trim().startsWith("lambda$") && AnalyticsUtil.isPrivate(access) && AnalyticsUtil.isSynthetic(access)) {
            MethodData MethodData = HookConfig.sLambdaMethods.get(methodDesc);
            if (MethodData != null) {
                int paramStart = MethodData.paramsStart;
                if (AnalyticsUtil.isStatic(access)) {
                    paramStart = paramStart - 1;
                }
                AnalyticsUtil.visitMethodWithLoadedParams(methodVisitor, Opcodes.INVOKESTATIC, HookConfig.LOG_ANALYTICS_BASE,
                        MethodData.agentName, MethodData.agentDesc,
                        paramStart, MethodData.paramsCount, MethodData.opcodes);
                isHasTracked = true;
                return;
            }
        }

        if (!(AnalyticsUtil.isPublic(access) && !AnalyticsUtil.isStatic(access))) {
            return;
        }

        /**
         * 之前已经添加过埋点代码，忽略
         */
        if (isHasInstrumented) {
            return;
        }

        /**
         * Method 描述信息
         */
        String methodNameDesc = methodName + methodDesc;

        /**
         * Fragment
         * 目前支持 android/support/v4/app/ListFragment 和 android/support/v4/app/Fragment
         */
        if (AnalyticsUtil.isInstanceOfFragment(superName)) {
            MethodData MethodData = HookConfig.sFragmentMethods.get(methodNameDesc);
//            Log.info("fragment:methodNameDesc:" + methodNameDesc)
//            Log.info("fragment:MethodData:" + MethodData)
            if (MethodData != null) {
                visitedFragMethods.add(methodNameDesc);
                AnalyticsUtil.visitMethodWithLoadedParams(methodVisitor, Opcodes.INVOKESTATIC, HookConfig.LOG_ANALYTICS_BASE, MethodData.agentName, MethodData.agentDesc, MethodData.paramsStart, MethodData.paramsCount, MethodData.opcodes);
                isHasTracked = true;
            }
        }

        /**
         * Menu
         * 目前支持 onContextItemSelected(MenuItem item)、onOptionsItemSelected(MenuItem item)
         */
        if (AnalyticsUtil.isTargetMenuMethodDesc(methodNameDesc)) {
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKESTATIC, HookConfig.LOG_ANALYTICS_BASE, "trackMenuItem", "(Ljava/lang/Object;Landroid/view/MenuItem;)V", false);
            isHasTracked = true;
            return;
        }

        if (methodNameDesc.equals("onDrawerOpened(Landroid/view/View;)V")) {
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKESTATIC, HookConfig.LOG_ANALYTICS_BASE, "trackDrawerOpened", "(Landroid/view/View;)V", false);
            isHasTracked = true;
            return;
        } else if (methodNameDesc.equals("onDrawerClosed(Landroid/view/View;)V")) {
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKESTATIC, HookConfig.LOG_ANALYTICS_BASE, "trackDrawerClosed", "(Landroid/view/View;)V", false);
            isHasTracked = true;
            return;
        }

        if (className.equals("android/databinding/generated/callback/OnClickListener")) {
            if (methodNameDesc.equals("onClick(Landroid/view/View;)V")) {
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitMethodInsn(INVOKESTATIC, HookConfig.LOG_ANALYTICS_BASE,
                        "trackViewOnClick", "(Landroid/view/View;)V", false);
                isHasTracked = true;
                return;
            }
        }

        if (className.startsWith("android") || className.startsWith("androidx")) {
            return;
        }

        if (methodNameDesc.equals("onItemSelected(Landroid/widget/AdapterView;Landroid/view/View;IJ)V")
                || methodNameDesc.equals("onListItemClick(Landroid/widget/ListView;Landroid/view/View;IJ)V")) {
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitVarInsn(ILOAD, 3);
            methodVisitor.visitMethodInsn(INVOKESTATIC, HookConfig.LOG_ANALYTICS_BASE,
                    "trackListView", "(Landroid/widget/AdapterView;Landroid/view/View;I)V", false);
            isHasTracked = true;
            return;
        }

        if (isAutoTrackViewOnClickAnnotation) {
            if (methodDesc.equals("(Landroid/view/View;)V")) {
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitMethodInsn(INVOKESTATIC, HookConfig.LOG_ANALYTICS_BASE,
                        "trackViewOnClick", "(Landroid/view/View;)V", false);
                isHasTracked = true;
                return;
            }
        }

        if (interfaces != null && interfaces.length > 0) {
            MethodData MethodData = HookConfig.sInterfaceMethods.get(methodNameDesc);
            if (MethodData != null && contains(interfaces, MethodData.parent)) {
                AnalyticsUtil.visitMethodWithLoadedParams(methodVisitor, Opcodes.INVOKESTATIC, HookConfig.LOG_ANALYTICS_BASE
                        , MethodData.agentName, MethodData.agentDesc, MethodData.paramsStart,
                        MethodData.paramsCount, MethodData.opcodes);
                isHasTracked = true;
            }
        }

        // 其它事件优先注入
        if (!isHasTracked) {
            if (methodNameDesc.equals("onClick(Landroid/view/View;)V")) {
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitMethodInsn(INVOKESTATIC, HookConfig.LOG_ANALYTICS_BASE, "trackViewOnClick", "(Landroid/view/View;)V", false);
                isHasTracked = true;
            }
        }
    }

    private boolean contains(String[] list, String key) {
        for (String val : list) {
            if (val.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 该方法是当扫描器扫描到类注解声明时进行调用
     * @param s 注解的类型。它使用的是（“L” + “类型路径” + “;”）形式表述
     * @param b 表示的是，该注解是否在 JVM 中可见
     * 1.RetentionPolicy.SOURCE：声明注解只保留在 Java 源程序中，在编译 Java 类时注解信息不会被写入到 Class。如果使用的是这个配置 ASM 也将无法探测到这个注解。
     * 2.RetentionPolicy.CLASS：声明注解仅保留在 Class 文件中，JVM 运行时并不会处理它，这意味着 ASM 可以在 visitAnnotation 时候探测到它，但是通过Class 反射无法获取到注解信息。
     * 3.RetentionPolicy.RUNTIME：这是最常用的一种声明，ASM 可以探测到这个注解，同时 Java 反射也可以取得注解的信息。所有用到反射获取的注解都会用到这个配置，就是这个原因。
     * @return
     */
    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        if (s.equals("Lcom/mmc/lamandys/liba_datapick/AutoTrackDataViewOnClick;")) {
            isAutoTrackViewOnClickAnnotation = true;
            Logx.i(TAG, "||发现 ${methodName}${methodDesc} 有注解 @AutoTrackDataViewOnClick");
        }

        if (s.equals("Lcom/mmc/lamandys/liba_datapick/AutoIgnoreTrackDataOnClick;")) {
            isAutoTrackIgnoreTrackOnClick = true;
            Logx.i(TAG, "||发现 ${methodName}${methodDesc} 有注解 @AutoIgnoreTrackDataOnClick");
        }

        if (s.equals("Lcom/mmc/lamandys/liba_datapick/AutoDataInstrumented;")) {
            isHasInstrumented = true;
        }

        return super.visitAnnotation(s, b);
    }
}
