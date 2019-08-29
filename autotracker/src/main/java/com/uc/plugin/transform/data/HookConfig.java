package com.uc.plugin.transform.data;

import org.objectweb.asm.Opcodes;

import java.util.HashMap;

/**
 * Copyright (C) 2004 - 2019 UCWeb Inc. All Rights Reserved.
 * Description : 描述当前文件的功能和使用范围
 * Attention: 如果是公共类，仔细说明使用方法和注意事项：特别是一些设计上的职责边界
 * <p>
 * Created by zukai.kzk@alibaba-inc.com on 2019/8/1
 */
public class HookConfig {

    /**
     * 日志采集埋点入口类
     */
    public static final String LOG_ANALYTICS_BASE = "com/mmc/lamandys/liba_datapick/AutoTrackHelper";


    public final
    static HashMap<String, MethodData> sInterfaceMethods = new HashMap<>();

    static {
        sInterfaceMethods.put("onClick(Landroid/view/View;)V", new MethodData(
                "onClick",
                "(Landroid/view/View;)V",
                "android/view/View$OnClickListener",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                new int[]{Opcodes.ALOAD}));
        sInterfaceMethods.put("onCheckedChanged(Landroid/widget/CompoundButton;Z)V", new MethodData(
                "onCheckedChanged",
                "(Landroid/widget/CompoundButton;Z)V",
                "android/widget/CompoundButton$OnCheckedChangeListener",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                new int[]{Opcodes.ALOAD}));
        sInterfaceMethods.put("onRatingChanged(Landroid/widget/RatingBar;FZ)V", new MethodData(
                "onRatingChanged",
                "(Landroid/widget/RatingBar;FZ)V",
                "android/widget/RatingBar$OnRatingBarChangeListener",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                new int[]{Opcodes.ALOAD}));
        sInterfaceMethods.put("onStopTrackingTouch(Landroid/widget/SeekBar;)V", new MethodData(
                "onStopTrackingTouch",
                "(Landroid/widget/SeekBar;)V",
                "android/widget/SeekBar$OnSeekBarChangeListener",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                new int[]{Opcodes.ALOAD}));
        sInterfaceMethods.put("onCheckedChanged(Landroid/widget/RadioGroup;I)V", new MethodData(
                "onCheckedChanged",
                "(Landroid/widget/RadioGroup;I)V",
                "android/widget/RadioGroup$OnCheckedChangeListener",
                "trackRadioGroup",
                "(Landroid/widget/RadioGroup;I)V",
                1, 2,
                new int[]{Opcodes.ALOAD, Opcodes.ILOAD}));
        sInterfaceMethods.put("onClick(Landroid/content/DialogInterface;I)V", new MethodData(
                "onClick",
                "(Landroid/content/DialogInterface;I)V",
                "android/content/DialogInterface$OnClickListener",
                "trackDialog",
                "(Landroid/content/DialogInterface;I)V",
                1, 2,
                new int[]{Opcodes.ALOAD, Opcodes.ILOAD}));
        sInterfaceMethods.put("onItemClick(Landroid/widget/AdapterView;Landroid/view/View;IJ)V", new MethodData(
                "onItemClick",
                "(Landroid/widget/AdapterView;Landroid/view/View;IJ)V",
                "android/widget/AdapterView$OnItemClickListener",
                "trackListView",
                "(Landroid/widget/AdapterView;Landroid/view/View;I)V",
                1, 3,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD}));
        sInterfaceMethods.put("onItemSelected(Landroid/widget/AdapterView;Landroid/view/View;IJ)V", new MethodData(
                "onItemSelected",
                "(Landroid/widget/AdapterView;Landroid/view/View;IJ)V",
                "android/widget/AdapterView$OnItemSelectedListener",
                "trackListView",
                "(Landroid/widget/AdapterView;Landroid/view/View;I)V",
                1, 3,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD}));
        sInterfaceMethods.put("onGroupClick(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z", new MethodData(
                "onGroupClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z",
                "android/widget/ExpandableListView$OnGroupClickListener",
                "trackExpandableListViewOnGroupClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;I)V",
                1, 3,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD}));
        sInterfaceMethods.put("onChildClick(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z", new MethodData(
                "onChildClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z",
                "android/widget/ExpandableListView$OnChildClickListener",
                "trackExpandableListViewOnChildClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;II)V",
                1, 4,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD}));
        sInterfaceMethods.put("onTabChanged(Ljava/lang/String;)V", new MethodData(
                "onTabChanged",
                "(Ljava/lang/String;)V",
                "android/widget/TabHost$OnTabChangeListener",
                "trackTabHost",
                "(Ljava/lang/String;)V",
                1, 1,
                new int[]{Opcodes.ALOAD}));

        sInterfaceMethods.put("onNavigationItemSelected(Landroid/view/MenuItem;)Z", new MethodData(
                "onNavigationItemSelected",
                "(Landroid/view/MenuItem;)Z",
                "android/support/design/widget/NavigationView$OnNavigationItemSelectedListener",
                "trackMenuItem",
                "(Ljava/lang/Object;Landroid/view/MenuItem;)V",
                0, 2,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD}));

        sInterfaceMethods.put("onTabSelected(Landroid/support/design/widget/TabLayout$Tab;)V", new MethodData(
                "onTabSelected",
                "(Landroid/support/design/widget/TabLayout$Tab;)V",
                "android/support/design/widget/TabLayout$OnTabSelectedListener",
                "trackTabLayoutSelected",
                "(Ljava/lang/Object;Ljava/lang/Object;)V",
                0, 2,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD}));

//        sInterfaceMethods.put("onPageSelected(I)V", new MethodData(
//                "onPageSelected",
//                "(I)V",
//                "android/support/v4/view/ViewPager$OnPageChangeListener",
//                "trackViewPageSelected",
//                "(Ljava/lang/Object;I)V",
//                0, 2,
//                new int[]{Opcodes.ALOAD, Opcodes.ALOAD}));

        // Todo: 扩展
    }

    /**
     * Fragment中的方法
     */
    public final
    static HashMap<String, MethodData> sFragmentMethods = new HashMap<>();
    static {
        sFragmentMethods.put("onResume()V", new MethodData(
                "onResume",
                "()V",
                "",// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
                "trackFragmentResume",
                "(Ljava/lang/Object;)V",
                0, 1,
                new int[]{Opcodes.ALOAD}));
        sFragmentMethods.put("setUserVisibleHint(Z)V", new MethodData(
                "setUserVisibleHint",
                "(Z)V",
                "",// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
                "trackFragmentSetUserVisibleHint",
                "(Ljava/lang/Object;Z)V",
                0, 2,
                new int[]{Opcodes.ALOAD, Opcodes.ILOAD}));
        sFragmentMethods.put("onHiddenChanged(Z)V", new MethodData(
                "onHiddenChanged",
                "(Z)V",
                "",
                "trackOnHiddenChanged",
                "(Ljava/lang/Object;Z)V",
                0, 2,
                new int[]{Opcodes.ALOAD, Opcodes.ILOAD}));
        sFragmentMethods.put("onViewCreated(Landroid/view/View;Landroid/os/Bundle;)V", new MethodData(
                "onViewCreated",
                "(Landroid/view/View;Landroid/os/Bundle;)V",
                "",
                "onFragmentViewCreated",
                "(Ljava/lang/Object;Landroid/view/View;Landroid/os/Bundle;)V",
                0, 3,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ALOAD}));
        sFragmentMethods.put("onDestroy()V", new MethodData(
                "onDestroy",
                "()V",
                "",// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
                "trackFragmentDestroy",
                "(Ljava/lang/Object;)V",
                0, 1,
                new int[]{Opcodes.ALOAD}));
    }

    /**
     * android.gradle 3.2.1 版本中，针对 Lambda 表达式处理
     */
    public final
    static HashMap<String, MethodData> sLambdaMethods = new HashMap<>();

    static {
        sLambdaMethods.put("(Landroid/view/View;)V", new MethodData(
                "onClick",
                "(Landroid/view/View;)V",
                "android/view/View$OnClickListener",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                new int[]{Opcodes.ALOAD}));
        sLambdaMethods.put("(Landroid/widget/CompoundButton;Z)V", new MethodData(
                "onCheckedChanged",
                "(Landroid/widget/CompoundButton;Z)V",
                "android/widget/CompoundButton$OnCheckedChangeListener",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                new int[]{Opcodes.ALOAD}));
        sLambdaMethods.put("(Landroid/widget/RatingBar;FZ)V", new MethodData(
                "onRatingChanged",
                "(Landroid/widget/RatingBar;FZ)V",
                "android/widget/RatingBar$OnRatingBarChangeListener",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                new int[]{Opcodes.ALOAD}));
        sLambdaMethods.put("(Landroid/widget/SeekBar;)V", new MethodData(
                "onStopTrackingTouch",
                "(Landroid/widget/SeekBar;)V",
                "android/widget/SeekBar$OnSeekBarChangeListener",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                new int[]{Opcodes.ALOAD}));
        sLambdaMethods.put("(Landroid/widget/RadioGroup;I)V", new MethodData(
                "onCheckedChanged",
                "(Landroid/widget/RadioGroup;I)V",
                "android/widget/RadioGroup$OnCheckedChangeListener",
                "trackRadioGroup",
                "(Landroid/widget/RadioGroup;I)V",
                1, 2,
                new int[]{Opcodes.ALOAD, Opcodes.ILOAD}));
        sLambdaMethods.put("(Landroid/content/DialogInterface;I)V", new MethodData(
                "onClick",
                "(Landroid/content/DialogInterface;I)V",
                "android/content/DialogInterface$OnClickListener",
                "trackDialog",
                "(Landroid/content/DialogInterface;I)V",
                1, 2,
                new int[]{Opcodes.ALOAD, Opcodes.ILOAD}));
        sLambdaMethods.put("(Landroid/widget/AdapterView;Landroid/view/View;IJ)V", new MethodData(
                "onItemClick",
                "(Landroid/widget/AdapterView;Landroid/view/View;IJ)V",
                "android/widget/AdapterView$OnItemClickListener",
                "trackListView",
                "(Landroid/widget/AdapterView;Landroid/view/View;I)V",
                1, 3,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD}));
        sLambdaMethods.put("(Landroid/widget/AdapterView;Landroid/view/View;IJ)V", new MethodData(
                "onItemSelected",
                "(Landroid/widget/AdapterView;Landroid/view/View;IJ)V",
                "android/widget/AdapterView$OnItemSelectedListener",
                "trackListView",
                "(Landroid/widget/AdapterView;Landroid/view/View;I)V",
                1, 3,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD}));
        sLambdaMethods.put("(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z", new MethodData(
                "onGroupClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z",
                "android/widget/ExpandableListView$OnGroupClickListener",
                "trackExpandableListViewOnGroupClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;I)V",
                1, 3,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD}));
        sLambdaMethods.put("(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z", new MethodData(
                "onChildClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z",
                "android/widget/ExpandableListView$OnChildClickListener",
                "trackExpandableListViewOnChildClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;II)V",
                1, 4,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD}));
        sLambdaMethods.put("(Ljava/lang/String;)V", new MethodData(
                "onTabChanged",
                "(Ljava/lang/String;)V",
                "android/widget/TabHost$OnTabChangeListener",
                "trackTabHost",
                "(Ljava/lang/String;)V",
                1, 1,
                new int[]{Opcodes.ALOAD}));

        sLambdaMethods.put("(Landroid/view/MenuItem;)Z", new MethodData(
                "onNavigationItemSelected",
                "(Landroid/view/MenuItem;)Z",
                "android/support/design/widget/NavigationView$OnNavigationItemSelectedListener",
                "trackMenuItem",
                "(Ljava/lang/Object;Landroid/view/MenuItem;)V",
                0, 2,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD}));

        sLambdaMethods.put("(Landroid/support/design/widget/TabLayout$Tab;)V", new MethodData(
                "onTabSelected",
                "(Landroid/support/design/widget/TabLayout$Tab;)V",
                "android/support/design/widget/TabLayout$OnTabSelectedListener",
                "trackTabLayoutSelected",
                "(Ljava/lang/Object;Ljava/lang/Object;)V",
                0, 2,
                new int[]{Opcodes.ALOAD, Opcodes.ALOAD}));

        // Todo: 扩展
    }
}
