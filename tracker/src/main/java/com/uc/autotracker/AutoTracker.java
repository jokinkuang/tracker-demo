package com.uc.autotracker;

import android.util.Log;

import com.uc.autotracker.model.DataPools;
import com.uc.autotracker.model.PathStack;
import com.uc.autotracker.model.sem.Entry;
import com.uc.autotracker.model.sem.Instance;
import com.uc.autotracker.model.sem.Module;
import com.uc.autotracker.model.spm.Dot;
import com.uc.autotracker.model.spm.Page;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/15
 */
public class AutoTracker {
    private static final String TAG = "AutoTracker";

    private IDataProvider mDataProvider;
    private IDataProvider mDefaultDataProvider = new DefaultDataProvider();
    private DataPools mDataPools = new DataPools();
    private PathStack mPathStack = new PathStack();

    private final static class Holder {
        private final static AutoTracker INSTANCE = new AutoTracker();
    }
    public static AutoTracker getInstance() {
        return Holder.INSTANCE;
    }

    private AutoTracker() {
        // nop
    }

    public void setDataProvider(IDataProvider dataProvider) {
        mDataProvider = dataProvider;
    }

    public IDataProvider getDataProvider() {
        if (mDataProvider == null) {
            Log.e(TAG, "## Fatal Error!");
            return mDefaultDataProvider;
        }
        return mDataProvider;
    }

    public void enterEntry(Entry entry) {
        mDataPools.put(entry, entry);
    }

    public void enterModule(Module module) {
        mDataPools.put(module, module);

        if (Module.equals(mPathStack.get(0), module)) {
            return;
        }
        mPathStack.add(module);
    }

    public void exitModule(Module module) {
        int index = -1;
        Iterator iterator = mPathStack.iterator();
        while (iterator.hasNext()) {
            Object mod = iterator.next();
            if (Module.equals(mod, module)) {
                index = 1;
                continue;
            };
            if (index == 1) {
                iterator.remove();
            }
        }
    }

    public void enterPage(Page page) {

    }

    public void exitPage(Page page) {

    }

    public void pv(Instance instance) {
        mDataPools.put(instance, instance);
        Log.e(TAG, "[PV] "+getLog()+instance);
    }

    public void click(Dot dot) {
        mDataPools.put(dot, dot);
        Log.e(TAG, "[CLICK] "+getLog()+dot);
    }

    public void expo(Dot dot) {
        mDataPools.put(dot, dot);
        Log.e(TAG, "[EXPO] "+getLog()+dot);
    }

    private String getLog() {
        StringBuilder log = new StringBuilder();
        log.append("Sem:"+getSemPath());
        log.append("Spm:"+getSpmPath());
        log.append("Common:"+getCommonInfo());
        return log.toString();
    }


    private String getSemPath() {
        StringBuilder semPath = new StringBuilder();
        Object entry = getDataProvider().getData().get("entry");
        if (entry != null) {
            semPath.append(entry.toString());
            semPath.append(".");
        }

        Object module = getDataProvider().getData().get("module");
        if (module != null) {
            semPath.append(module.toString());
            semPath.append(".");
        }

        Object instance = getDataProvider().getData().get("instance");
        if (instance != null) {
            semPath.append(instance.toString());
        }
        return semPath.toString();
    }

    private String getSpmPath() {
        StringBuilder spmPath = new StringBuilder();
        Object entry = getDataProvider().getData().get("page");
        if (entry != null) {
            spmPath.append(entry.toString());
            spmPath.append(".");
        }

        Object module = getDataProvider().getData().get("block");
        if (module != null) {
            spmPath.append(module.toString());
            spmPath.append(".");
        }

        Object instance = getDataProvider().getData().get("dot");
        if (instance != null) {
            spmPath.append(instance.toString());
        }
        return spmPath.toString();
    }

    private String getCommonInfo() {
        StringBuilder common = new StringBuilder();
        common.append(getInfo("network"));
        common.append(getInfo("ch_id"));
        common.append(getInfo("app"));
        return common.toString();
    }

    private String getInfo(String key) {
        Object obj = getDataProvider().getData().get(key);
        if (obj != null) {
            return key + ":" + obj.toString() + ",";
        }
        return "";
    }

}
