package com.uc.plugin.transform.util;

import com.uc.plugin.log.Logx;
import com.uc.plugin.transform.asm.AsmClassModifier;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class Modifier {
    private static final String TAG = "AsmClassModifier";
    /**
     * 目录文件中修改对应字节码
     */
    public static File modifyClassFile(File dir, File classFile, File tempDir) {
        File modified = null;
        FileOutputStream outputStream = null;
        try {
            String className = TextUtil.path2ClassName(classFile.getAbsolutePath()
                    .replace(dir.getAbsolutePath() + File.separator, ""));
            Logx.i(TAG, "File:className:" + className);

            if (MatchUtil.isShouldModifyClass(className)) {
                byte[] sourceClassBytes = IOUtils.toByteArray(new FileInputStream(classFile));
                byte[] modifiedClassBytes = AsmClassModifier.modifyClasses(className, sourceClassBytes);
                if (modifiedClassBytes.length > 0) {
                    modified = new File(tempDir, className.replace(".", "") + ".class");
                    if (modified.exists()) {
                        modified.delete();
                    }
                    modified.createNewFile();
                    outputStream = new FileOutputStream(modified);
                    outputStream.write(modifiedClassBytes);
                }
            } else {
                return classFile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                // nop
            }
        }
        return modified;
    }

}
