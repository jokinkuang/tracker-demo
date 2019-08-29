package com.uc.plugin.transform;

import com.android.build.api.transform.Context;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;
import com.uc.plugin.transform.data.TransformData;
import com.uc.plugin.transform.ops.DirectoryFilterOp;
import com.uc.plugin.transform.ops.FilterOp;
import com.uc.plugin.transform.ops.FilterOp2;
import com.uc.plugin.transform.ops.TaskOp;
import com.uc.plugin.transform.ops.TaskOp2;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 字节码处理器
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/7/29
 */
public class MainTransform extends Transform {
    private static final String TRANSFORM_NAME = "autotrack";
    private MainChain mMainChain = new MainChain();

    @Override
    public String getName() {
        return TRANSFORM_NAME;
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        if (! transformInvocation.isIncremental()) {
            transformInvocation.getOutputProvider().deleteAll();
        }
        mMainChain.run(new TransformData(transformInvocation));
        // transformInner(transformInvocation);
    }

    private void transformInner(TransformInvocation transformInvocation) {
        transformInner(transformInvocation.getContext(), transformInvocation.getInputs(),
                transformInvocation.getReferencedInputs(), transformInvocation.getOutputProvider(),
                transformInvocation.isIncremental());
    }

    private void transformInner(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) {
        for (TransformInput input : inputs) {
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                if (directoryInput.getFile().isDirectory()) {
                    // System.out.println("==== directoryInput.file = " + directoryInput.getFile());
                    // for (File file : directoryInput.getFile().listFiles()) {
                    //     System.out.println("file:" + file.getName());
                    // }
                    handleFileRecurse(directoryInput.getFile());
                }
                //处理完输入文件之后，要把输出给下一个任务
                File dest = outputProvider.getContentLocation(directoryInput.getName(), directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);
                try {
                    FileUtils.copyDirectory(directoryInput.getFile(), dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (JarInput jarInput : input.getJarInputs()) {
                System.out.println("------=== jarInput.file === " + jarInput.getFile().getAbsolutePath());
                File tempFile = null;
                if (jarInput.getFile().getAbsolutePath().endsWith(".jar")) {
                    // ...对jar进行插入字节码
                }
                /**
                 * 重名输出文件,因为可能同名,会覆盖
                 */
                String jarName = jarInput.getName();
                String md5Name = DigestUtils.md5Hex(jarInput.getFile().getAbsolutePath());
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4);
                }
                //处理jar进行字节码注入处理
                File dest = outputProvider.getContentLocation(jarName + md5Name, jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);
                try {
                    FileUtils.copyFile(jarInput.getFile(), dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void handleFileRecurse(final File file) {
        if (file.isDirectory()) {
            System.out.println("==== directoryInput.file = " + file);
            for (File file1 : file.listFiles()) {
                // next
                handleFileRecurse(file1);
            }
        } else {
            // Thread thread = new Thread() {
            //     @Override
            //     public void run() {
            //         System.out.println("==== file = " + this.getId() + ":"+ file);
            //     }
            // };
            // thread.start();
            // TODO: thread
            System.out.println("==== file = " + Thread.currentThread().getId() + ":"+ file);
        }
    }
}