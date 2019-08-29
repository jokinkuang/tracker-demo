package com.uc.plugin.log;

/**
 * 图片库Log输出日志
 */
public class Logx {
    private static ILogx debugLog = new ILogx() {

        @Override
        public void v(final String tag, final String format, final Object... params) {
            String log = (params == null || params.length == 0) ? format : String.format(format, params);
            System.out.println("V:"+ tag + ":" + log);
        }

        @Override
        public void i(final String tag, final String format, final Object... params) {
            String log = (params == null || params.length == 0) ? format : String.format(format, params);
            System.out.println("I:"+ tag + ":" + log);
        }

        @Override
        public void d(final String tag, final String format, final Object... params) {
            String log = (params == null || params.length == 0) ? format : String.format(format, params);
            System.out.println("D:"+ tag + ":" + log);
        }

        @Override
        public void w(final String tag, final String format, final Object... params) {
            String log = (params == null || params.length == 0) ? format : String.format(format, params);
            System.out.println("W:"+ tag + ":" + log);
        }

        @Override
        public void e(final String tag, final String format, final Object... params) {
            String log = (params == null || params.length == 0) ? format : String.format(format, params);
            System.err.println("E:"+ tag + ":" + log);
        }

        @Override
        public void printErrStackTrace(String tag, Throwable tr, String format, Object... params) {
            String log = (params == null || params.length == 0) ? format : String.format(format, params);
            if (log == null) {
                log = "";
            }
            log += "  " + tr.toString();
            e(tag, log);
        }

        @Override
        public int getLogLevel() {
            return LOG_LEVEL_DEFAULT;
        }
    };

    private static ILogx logImp = debugLog;

    private Logx() {
    }

    public static void setImageLogImp(ILogx imp) {
        logImp = imp;
    }

    public static ILogx getImpl() {
        return logImp;
    }

    public static boolean isVerbose() {
        return isLevel(ILogx.LOG_LEVEL_VERBOSE);
    }

    public static boolean isDebug() {
        return isLevel(ILogx.LOG_LEVEL_DEBUG);
    }

    public static boolean isInfo() {
        return isLevel(ILogx.LOG_LEVEL_INFO);
    }

    public static boolean isWarning() {
        return isLevel(ILogx.LOG_LEVEL_WARNING);
    }

    public static boolean isError() {
        return isLevel(ILogx.LOG_LEVEL_ERROR);
    }

    private static boolean isLevel(int level) {
        if (logImp != null) {
            return logImp.getLogLevel() >= level;
        }
        return false;
    }

    public static void v(final String tag, final String msg, final Object... obj) {
        if (logImp != null) {
            logImp.v(tag, msg, obj);
        }
    }

    public static void e(final String tag, final String msg, final Object... obj) {
        if (logImp != null) {
            logImp.e(tag, msg, obj);
        }
    }

    public static void w(final String tag, final String msg, final Object... obj) {
        if (logImp != null) {
            logImp.w(tag, msg, obj);
        }
    }

    public static void i(final String tag, final String msg, final Object... obj) {
        if (logImp != null) {
            // logImp.i(tag, msg, obj);
        }
    }

    public static void d(final String tag, final String msg, final Object... obj) {
        if (logImp != null) {
            logImp.d(tag, msg, obj);
        }
    }

    public static void printErrStackTrace(String tag, Throwable tr, final String format, final Object... obj) {
        if (logImp != null) {
            logImp.printErrStackTrace(tag, tr, format, obj);
        }
    }

    public interface ILogx {
        int LOG_LEVEL_VERBOSE = 5;
        int LOG_LEVEL_DEBUG = 4;
        int LOG_LEVEL_INFO = 3;
        int LOG_LEVEL_WARNING = 2;
        int LOG_LEVEL_ERROR = 1;
        int LOG_LEVEL_DEFAULT = LOG_LEVEL_WARNING;

        void v(final String tag, final String msg, final Object... obj);

        void i(final String tag, final String msg, final Object... obj);

        void w(final String tag, final String msg, final Object... obj);

        void d(final String tag, final String msg, final Object... obj);

        void e(final String tag, final String msg, final Object... obj);

        void printErrStackTrace(String tag, Throwable tr, final String format, final Object... obj);

        int getLogLevel();
    }
}
