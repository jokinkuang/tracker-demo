package com.uc.plugin.transform.ops;

import com.android.build.api.transform.QualifiedContent;
import com.android.ddmlib.Log;
import com.uc.plugin.log.Logx;
import com.uc.plugin.transform.base.BaseTaskChain;
import com.uc.plugin.transform.base.IResult;
import com.uc.plugin.transform.data.TransformData;
import com.uc.plugin.transform.util.Modifier;

import org.apache.commons.io.FileUtils;
import org.gradle.internal.impldep.bsh.commands.dir;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class DirectoryTask extends BaseTaskChain<TransformData, TransformData> {
    private static final String TAG = "DirectoryTask";
    private HashMap<String, File> modifiedFiles = new HashMap<>();
    private TransformData mData;

    public DirectoryTask() {
    }

    private void saveFiles() {
        // 1. copy the whole dir
        try {
            FileUtils.copyDirectory(mData.getInput().getFile(), mData.getInputDir());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. override with modified files
        Logx.e(TAG, "modify map size:"+modifiedFiles.size());
        for (Map.Entry<String, File> entry : modifiedFiles.entrySet()) {
            File target = new File(mData.getInputDir().getAbsolutePath() + entry.getKey());
            Logx.i(TAG, target.getAbsolutePath());
            if (target.exists()) {
                target.delete();
            }
            try {
                Logx.e(TAG, "file:"+entry.getValue().getPath()+" to file:"+target.getPath());
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
        Logx.e(TAG, "| dir:"+ data.getInput().getFile().getPath());

        handleDirRecurse(data.getInput().getFile());
        saveFiles();

        return true;
    }

    private static Pattern classPostfix = Pattern.compile("~/.*\\.class/");

    private void handleDirRecurse(File dir) {
        if (! dir.isDirectory()) {
            Logx.e(TAG, "||-->not dir:"+dir.getPath());
            return;
        }

        Logx.e(TAG, "||-->开始遍历特定目录  ${dest.absolutePath}"+mData.getInputDir().getAbsolutePath());

        if (dir.exists()) {
            // File[] fileList = dir.listFiles(new FilenameFilter() {
            //     @Override
            //     public boolean accept(File file, String s) {
            //         return classPostfix.matcher(s).matches();
            //         // return true;
            //     }
            // });
            File[] fileList = dir.listFiles();
            if (fileList == null) {
                return;
            }
            for (File file : fileList) {
                if (file.isDirectory()) {
                    Logx.e(TAG, "| dir:"+file.getPath());
                    handleDirRecurse(file);
                    continue;
                }
                // if (! file.getPath().endsWith(".class")) {
                //     continue;
                // }

                Logx.e(TAG, "| file:"+file.getPath());
                File modified = Modifier.modifyClassFile(dir, file, mData.getTransformInvocation().getContext().getTemporaryDir());
                if (modified != null) {
                    //key为相对路径
                    Logx.e(TAG, "| modify file:"+file.getPath()+", " +file.getAbsolutePath().replace(mData.getInputDir().getAbsolutePath(), ""));
                    modifiedFiles.put(file.getAbsolutePath().replace(mData.getInputDir().getAbsolutePath(), ""), modified);
                }
            }
        }

        Logx.i(TAG, "||-->结束遍历特定目录  ${dest.absolutePath}"+dir.getPath());
    }

    private void runInner(QualifiedContent input) {
        if (! input.getFile().isDirectory()) {
            return;
        }

        Logx.e(TAG, "||-->开始遍历特定目录  ${dest.absolutePath}"+mData.getInputDir().getAbsolutePath());

        File dir = input.getFile();
        if (dir.exists()) {
            File[] fileList = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    // return classPostfix.matcher(s).matches();
                    return true;
                }
            });
            if (fileList == null) {
                return;
            }
            for (File file : fileList) {
                File modified = Modifier.modifyClassFile(dir, file, mData.getTransformInvocation().getContext().getTemporaryDir());
                if (modified != null) {
                    //key为相对路径
                    modifiedFiles.put(file.getAbsolutePath().replace(dir.getAbsolutePath(), ""), modified);
                }
            }
            saveFiles();

        }

        Logx.i(TAG, "||-->结束遍历特定目录  ${dest.absolutePath}"+mData.getInputDir().getAbsolutePath());
    }
}
