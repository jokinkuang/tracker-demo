package com.uc.plugin;

import org.gradle.api.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 插件配置
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/7/29
 */
public class PluginSettings {

    private static String getPluginSettingsKey() {
        return Plugin.GRADLE_SETTINGS;
    }

    // Main //

    private static Project sProject;
    public static void apply(Project project) {
        sProject = project;
        sProject.getExtensions().create(getPluginSettingsKey(), PluginSettings.class);
    }

    public static PluginSettings get() {
        return (PluginSettings) sProject.getExtensions().getByName(getPluginSettingsKey());
    }

    // Properties //

    public String name = "AutoTrack";
    /**
     * 是否是Debug模式进行日志打印
     */
    public boolean isDebug = false;
    /**
     * 是否打开日志采集的全埋点
     */
    public boolean isOpenLogTrack = true;
    /**
     * 用户自定义功能
     */
//    List<Map<String, Object>> matchData = [[:]]
    public List<Map<String, Object>> matchData = new ArrayList<>();
    /**
     * 需要手动过滤的包
     */
    public List<String> exclude = new ArrayList<>();
    /**
     * 需要手动添加的包
     */
    public List<String> include = new ArrayList<>();

    @Override
    public String toString() {
        return "PluginSettings{" +
                "name='" + name + '\'' +
                ", isDebug=" + isDebug +
                ", isOpenLogTrack=" + isOpenLogTrack +
                ", matchData=" + matchData +
                ", exclude=" + exclude +
                ", include=" + include +
                '}';
    }
}
