package com.uc.plugin.transform.ops;

import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.uc.plugin.log.Logx;
import com.uc.plugin.transform.asm.AsmClassModifier;
import com.uc.plugin.transform.base.BaseTaskChain;
import com.uc.plugin.transform.base.IResult;
import com.uc.plugin.transform.data.TransformData;
import com.uc.plugin.transform.util.MatchUtil;
import com.uc.plugin.transform.util.Modifier;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class JarTask extends BaseTaskChain<TransformData, TransformData> {
    private static final String TAG = "JarTask";
    private HashMap<String, File> modifiedFiles = new HashMap<>();
    private TransformData mData;

    public JarTask() {
    }

    private void saveFiles() {
        // 1. copy the whole dir
        try {
            FileUtils.copyDirectory(mData.getInput().getFile(), mData.getInputDir());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. override with modified files
        for (Map.Entry<String, File> entry : modifiedFiles.entrySet()) {
            File target = new File(mData.getInputDir().getAbsolutePath() + entry.getKey());
            Logx.i(TAG, target.getAbsolutePath());
            if (target.exists()) {
                target.delete();
            }
            try {
                FileUtils.copyFile(entry.getValue(), target);
            } catch (IOException e) {
                e.printStackTrace();
            }
            entry.getValue().delete();
        }
    }

    @Override
    public boolean run(TransformData data) {
        mData = data;
        runInner(data.getInput());
        return true;
    }

    private void runInner(QualifiedContent jarInput) {
        File file = jarInput.getFile();
        String destName = file.getName();
        String absolutePath = file.getAbsolutePath();

        if (! destName.endsWith(".jar")) {
            return;
        }

        /** 截取文件路径的md5值重命名输出文件,因为可能同名,会覆盖*/
        String hexName = DigestUtils.md5Hex(absolutePath).substring(0, 8);
        destName = destName.substring(0, destName.length() - 4);

        /** 获得输出文件*/
        File dest = mData.getTransformInvocation().getOutputProvider().getContentLocation(destName
                + "_" + hexName, jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);

        Logx.i(TAG, "||-->开始遍历特定jar ${dest.absolutePath}");

        File modifiedJar = modifyJarFile(file, mData.getTransformInvocation().getContext().getTemporaryDir());

        Logx.i(TAG, "||-->结束遍历特定jar ${dest.absolutePath}");

        if (modifiedJar == null) {
            modifiedJar = file;
        }

        try {
            FileUtils.copyFile(modifiedJar, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Jar文件中修改对应字节码
     */
    private static File modifyJarFile(File jarFile, File tempDir) {
        if (jarFile.exists()) {
            return modifyJar(jarFile, tempDir, true);

        }
        return null;
    }

    /**
     * 读取原jar
     */
    private static File modifyJar(File jarFile, File tempDir, boolean nameHex) {
        try {
            /** 设置输出到的jar */
            String hexName = "";
            if (nameHex) {
                hexName = DigestUtils.md5Hex(jarFile.getAbsolutePath()).substring(0, 8);
            }

            JarFile file = new JarFile(jarFile);
            File outputJar = new File(tempDir, hexName + jarFile.getName());
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(outputJar));

            Enumeration enumeration = file.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement();
                InputStream inputStream = file.getInputStream(jarEntry);

                String entryName = jarEntry.getName();
                String className;

                ZipEntry zipEntry = new ZipEntry(entryName);

                jarOutputStream.putNextEntry(zipEntry);

                byte[] modifiedClassBytes = null;
                byte[] sourceClassBytes = IOUtils.toByteArray(inputStream);
                if (entryName.endsWith(".class")) {
                    className = entryName.replace("/", ".").replace(".class", "");
//                Logger.info("Jar:className:" + className)
                    if (MatchUtil.isShouldModifyClass(className)) {
                        modifiedClassBytes = AsmClassModifier.modifyClasses(className, sourceClassBytes);
                    }
                }
                if (modifiedClassBytes == null) {
                    jarOutputStream.write(sourceClassBytes);
                } else {
                    jarOutputStream.write(modifiedClassBytes);
                }
                jarOutputStream.closeEntry();
            }
            jarOutputStream.close();
            file.close();
            return outputJar;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
