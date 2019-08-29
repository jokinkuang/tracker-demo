package com.uc.plugin.transform.ops;

import com.uc.plugin.log.Logx;
import com.uc.plugin.transform.base.IResult;
import com.uc.plugin.transform.base.ITransformOp;
import com.uc.plugin.transform.data.TransformData;
import com.uc.plugin.transform.util.Modifier;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/7/31
 */
public class DirectoryFilterOp implements ITransformOp<TransformData, TransformData> {
    private static final String TAG = "DirectoryFilterOp";

    @Override
    public void start(TransformData data, IResult<TransformData, TransformData> cb) {
        startInner(data, cb);
        cb.onOk(data, data);
    }

    private void startInner(TransformData data, IResult<TransformData, TransformData> cb) {
        if (data.getInput().getFile().isDirectory()) {
            File dest = data.getInputDir();
            Logx.i(TAG, "||-->开始遍历特定目录  ${dest.absolutePath}");

            File dir = data.getInput().getFile();
            if (dir.exists()) {
                HashMap<String, File> modifyMap = new HashMap<>();

                File[] fileList = dir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        return true;
                    }
                });
                if (fileList == null) {
                    cb.onRecall(data, data);
                    return;
                }
                for (File file : fileList) {
                    File modified = Modifier.modifyClassFile(dir, file,
                            data.getTransformInvocation().getContext().getTemporaryDir());
                    if (modified != null) {
                        //key为相对路径
                        modifyMap.put(file.getAbsolutePath().replace(dir.getAbsolutePath(), ""), modified);
                    }
                }

                try {
                    FileUtils.copyDirectory(data.getInput().getFile(), dest);
                } catch (IOException e) {
                    e.printStackTrace();
                    cb.onFail(data, data);
                }

                for (Map.Entry<String, File> entry : modifyMap.entrySet()) {
                    File target = new File(dest.getAbsolutePath() + entry.getKey());
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
            Logx.i(TAG,"||-->结束遍历特定目录  ${dest.absolutePath}");
        }
    }

    private void dump(File file, int space) {
        if (file.isDirectory()) {
            Logx.d(TAG, "dir:"+file.getPath());
            space++;
            for (File subFile : file.listFiles()) {
                dump(subFile, space);
            }
        }
        String prefix = "";
        for (int i = 0; i < space; ++i) {
            prefix = prefix + "-";
        }
        Logx.d(TAG, prefix + "file:"+file.getPath());
    }
}
