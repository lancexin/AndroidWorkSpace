package com.hostxin.android.util;


/**
 * Log utility class.
 * 
 * <pre class="prettyprint">
 * public class CustomApplication extends Application {
 * 
 *     &#064;Override
 *     public void onCreate() {
 *         super.onCreate();
 * 
 *         LogUtil.setToggleRelease(false);
 * 
 *         LogUtil.setToggle(true);
 *         LogUtil.setLogLevel(LogUtil.VERBOSE);
 * 
 *         LogUtil.setToggleThrowable(true);
 * 
 *         LogUtil.setToggleThread(false);
 *         LogUtil.setToggleClassMethod(false);
 *         LogUtil.setToggleFileLineNumber(false);
 * 
 *         LogUtil.setToggleAuthor(false);
 *         LogUtil.setAuthorDefault(&quot;Default&quot;);
 * 
 *         LogUtil.setTagDefault(&quot;Log&quot;);
 *     }
 * }
 * </pre>
 */
public final class Dbg {

    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = android.util.Log.VERBOSE;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = android.util.Log.DEBUG;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = android.util.Log.INFO;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = android.util.Log.WARN;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = android.util.Log.ERROR;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = android.util.Log.ASSERT;

    /** Author: default */
    public static final String AUTHOR_DEFAULT = "Author";

    /** Tag: default */
    public static final String TAG_DEFAULT = "Tag";

    /** Tag: database operation */
    public static final String TAG_DATABASE = "Database";

    /** Tag: network operation */
    public static final String TAG_NETWORK = "Network";

    /** Tag: utility method */
    public static final String TAG_UTILITY = "Utility";

    /** Log toggle, default is true. */
    private static boolean sToggle = true;

    /** Log type level, default value is VERBOSE **/
    private static int sLogLevel = VERBOSE;

    /** Log toggle for print Throwable info, default value is true. **/
    private static boolean sToggleThrowable = true;

    /** Log toggle for print author name, default value is false. **/
    private static boolean sToggleAuthor = false;

    /** Log default author name. */
    private static String sAuthorDefault = AUTHOR_DEFAULT;

    /** Log default tag. */
    private static String sTagDefault = TAG_DEFAULT;

    /** Log toggle for print thread name, default value is false. **/
    private static boolean sToggleThread = false;

    /** Log toggle for print class name and method name, default value is false. **/
    private static boolean sToggleClassMethod = false;

    /**
     * Log toggle for print file name and code line number, default value is
     * false.
     **/
    private static boolean sToggleFileLineNumber = false;

    /** Log toggle for release, default value is false. **/
    private static boolean sToggleRelease = false;

    /**
     * Set log print toggle, default is turn on.
     * 
     * @param on
     */
    public static void setToggle(boolean on) {
        Dbg.sToggle = on;
    }

    /**
     * Set throwable print toggle, default value is true.
     * 
     * @param on
     */
    public static void setToggleThrowable(boolean on) {
        Dbg.sToggleThrowable = on;
    }

    /**
     * Set author print toggle, default value is false.
     * 
     * @param on
     */
    public static void setToggleAuthor(boolean on) {
        Dbg.sToggleAuthor = on;
    }

    /**
     * Set thread name print toggle, default value is false.
     * 
     * @param on
     */
    public static void setToggleThread(boolean on) {
        Dbg.sToggleThread = on;
    }

    /**
     * Set class and method name print toggle, default value is false.
     * 
     * @param on
     */
    public static void setToggleClassMethod(boolean on) {
        Dbg.sToggleClassMethod = on;
    }

    /**
     * Set file name and line number print toggle, default value is false.
     * 
     * @param on
     */
    public static void setToggleFileLineNumber(boolean on) {
        Dbg.sToggleFileLineNumber = on;
    }

    /**
     * Set release toggle, default value is false.
     * 
     * @param on
     */
    public static void setToggleRelease(boolean on) {
        Dbg.sToggleRelease = on;
    }

    /**
     * Return true if log print toggle is turn on.
     * 
     * @return
     */
    public static boolean isToggle() {
        return sToggle;
    }

    /**
     * Return true if throwable info allow print.
     * 
     * @return
     */
    public static boolean isToggleThrowable() {
        return sToggleThrowable;
    }

    /**
     * Return true if author name allow print.
     * 
     * @return
     */
    public static boolean isToggleAuthor() {
        return sToggleAuthor;
    }

    /**
     * Return true if thread name allow print.
     * 
     * @return
     */
    public static boolean isToggleThread() {
        return sToggleThread;
    }

    /**
     * Return true if class name and method name allow print.
     * 
     * @return
     */
    public static boolean isToggleClassMethod() {
        return sToggleClassMethod;
    }

    /**
     * Return true if file name and line number allow print.
     * 
     * @return
     */
    public static boolean isToggleFileLineNumber() {
        return sToggleFileLineNumber;
    }

    /**
     * Return true if release version toggle is turn on.
     * 
     * @return
     */
    public static boolean isToggleRelease() {
        return sToggleRelease;
    }

    /**
     * Set log print level, which log below the level will not print. Default
     * value is VERBOSE.
     * 
     * @param logType
     */
    public static void setLogLevel(int logType) {
        Dbg.sLogLevel = logType;
    }

    /**
     * Get log print level, which log below the level will not print.
     * 
     * @return
     */
    public static int getLogLevel() {
        return sLogLevel;
    }

    /**
     * Set default author name. If not set, will use the value
     * {@linkplain #AUTHOR_DEFAULT}.
     * 
     * @param author
     */
    public static void setAuthorDefault(String author) {
        if (author == null) {
            Dbg.sAuthorDefault = AUTHOR_DEFAULT;
        } else {
            Dbg.sAuthorDefault = author;
        }
    }

    /**
     * Get default author name.
     * 
     * @return
     */
    public static String getAuthorDefault() {
        return sAuthorDefault;
    }

    /**
     * Set default tag string. If not set, will use the value
     * {@linkplain #TAG_DEFAULT}.
     * 
     * @param tag
     */
    public static void setTagDefault(String tag) {
        if (tag == null) {
            Dbg.sTagDefault = TAG_DEFAULT;
        } else {
            Dbg.sTagDefault = tag;
        }
    }

    /**
     * Get default tag string.
     * 
     * @return
     */
    public static String getTagDefault() {
        return sTagDefault;
    }

    /**
     * see {@link #e(String, String, String, Throwable)}
     * 
     * @param msg
     */
    public static void e(String msg) {
        printLog(ERROR, null, msg, null, null);
    }

    /**
     * see {@link #e(String, String, String, Throwable)}
     * 
     * @param msg
     */
    public static void e(String msg, Throwable e) {
        printLog(ERROR, null, msg, null, e);
    }

    /**
     * see {@link #e(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        printLog(ERROR, tag, msg, null, null);
    }

    /**
     * see {@link #e(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     * @param e
     */
    public static void e(String tag, String msg, Throwable e) {
        printLog(ERROR, tag, msg, null, e);
    }

    /**
     * see {@link #e(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     * @param author
     */
    public static void e(String tag, String msg, String author) {
        printLog(ERROR, tag, msg, author, null);
    }

    /**
     * Send an {@link Dbg#ERROR} log message.
     * 
     * @param tag suggest use function tag description, e.g.
     *            {@link #TAG_DEFAULT}, {@link #TAG_DB}, {@link #TAG_NETWORK},
     *            {@link #TAG_UTILITY}, user define string value.
     * @param msg The message you would like logged.
     * @param author author of code, default value is {@link #AUTHOR_DEFAULT}
     * @param e A exception to log.
     */
    public static void e(String tag, String msg, String author, Throwable e) {
        printLog(ERROR, tag, msg, author, e);
    }

    /**
     * see {@link #w(String, String, String, Throwable)}
     * 
     * @param msg
     */
    public static void w(String msg) {
        printLog(WARN, null, msg, null, null);
    }

    /**
     * see {@link #w(String, String, String, Throwable)}
     * 
     * @param msg
     */
    public static void w(String msg, Throwable e) {
        printLog(WARN, null, msg, null, e);
    }

    /**
     * see {@link #w(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        printLog(WARN, tag, msg, null, null);
    }

    /**
     * see {@link #w(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     * @param e
     */
    public static void w(String tag, String msg, Throwable e) {
        printLog(WARN, tag, msg, null, e);
    }

    /**
     * see {@link #w(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     * @param author
     */
    public static void w(String tag, String msg, String author) {
        printLog(WARN, tag, msg, author, null);
    }

    /**
     * Send an {@link Dbg#WARN} log message.
     * 
     * @param tag suggest use function tag description, e.g.
     *            {@link #TAG_DEFAULT}, {@link #TAG_DB}, {@link #TAG_NETWORK},
     *            {@link #TAG_UTILITY}, user define string value.
     * @param msg The message you would like logged.
     * @param author author of code, default value is {@link #AUTHOR_DEFAULT}
     * @param e A exception to log.
     */
    public static void w(String tag, String msg, String author, Throwable e) {
        printLog(WARN, tag, msg, author, e);
    }

    /**
     * see {@link #i(String, String, String, Throwable)}
     * 
     * @param msg
     */
    public static void i(String msg) {
        printLog(INFO, null, msg, null, null);
    }

    /**
     * see {@link #i(String, String, String, Throwable)}
     * 
     * @param msg
     */
    public static void i(String msg, Throwable e) {
        printLog(INFO, null, msg, null, e);
    }

    /**
     * see {@link #i(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        printLog(INFO, tag, msg, null, null);
    }

    /**
     * see {@link #i(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     * @param e
     */
    public static void i(String tag, String msg, Throwable e) {
        printLog(INFO, tag, msg, null, e);
    }

    /**
     * see {@link #i(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     * @param author
     */
    public static void i(String tag, String msg, String author) {
        printLog(INFO, tag, msg, author, null);
    }

    /**
     * Send an {@link Dbg#INFO} log message.
     * 
     * @param tag suggest use function tag description, e.g.
     *            {@link #TAG_DEFAULT}, {@link #TAG_DB}, {@link #TAG_NETWORK},
     *            {@link #TAG_UTILITY}, user define string value.
     * @param msg The message you would like logged.
     * @param author author of code, default value is {@link #AUTHOR_DEFAULT}
     * @param e A exception to log.
     */
    public static void i(String tag, String msg, String author, Throwable e) {
        printLog(INFO, tag, msg, author, e);
    }

    /**
     * see {@link #d(String, String, String, Throwable)}
     * 
     * @param msg
     */
    public static void d(String msg) {
        printLog(DEBUG, null, msg, null, null);
    }

    /**
     * see {@link #d(String, String, String, Throwable)}
     * 
     * @param msg
     */
    public static void d(String msg, Throwable e) {
        printLog(DEBUG, null, msg, null, e);
    }

    /**
     * see {@link #d(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        printLog(DEBUG, tag, msg, null, null);
    }

    /**
     * see {@link #d(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     * @param e
     */
    public static void d(String tag, String msg, Throwable e) {
        printLog(DEBUG, tag, msg, null, e);
    }

    /**
     * see {@link #d(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     * @param author
     */
    public static void d(String tag, String msg, String author) {
        printLog(DEBUG, tag, msg, author, null);
    }

    /**
     * Send an {@link Dbg#DEBUG} log message.
     * 
     * @param tag suggest use function tag description, e.g.
     *            {@link #TAG_DEFAULT}, {@link #TAG_DB}, {@link #TAG_NETWORK},
     *            {@link #TAG_UTILITY}, user define string value.
     * @param msg The message you would like logged.
     * @param author author of code, default value is {@link #AUTHOR_DEFAULT}
     * @param e A exception to log.
     */
    public static void d(String tag, String msg, String author, Throwable e) {
        printLog(DEBUG, tag, msg, author, e);
    }

    /**
     * see {@link #v(String, String, String, Throwable)}
     * 
     * @param msg
     */
    public static void v(String msg) {
        printLog(VERBOSE, null, msg, null, null);
    }

    /**
     * see {@link #v(String, String, String, Throwable)}
     * 
     * @param msg
     */
    public static void v(String msg, Throwable e) {
        printLog(VERBOSE, null, msg, null, e);
    }

    /**
     * see {@link #v(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        printLog(VERBOSE, tag, msg, null, null);
    }

    /**
     * see {@link #v(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     * @param e
     */
    public static void v(String tag, String msg, Throwable e) {
        printLog(VERBOSE, tag, msg, null, e);
    }

    /**
     * see {@link #v(String, String, String, Throwable)}
     * 
     * @param tag
     * @param msg
     * @param author
     */
    public static void v(String tag, String msg, String author) {
        printLog(VERBOSE, tag, msg, author, null);
    }

    /**
     * Send an {@link Dbg#VERBOSE} log message.
     * 
     * @param tag suggest use function tag description, e.g.
     *            {@link #TAG_DEFAULT}, {@link #TAG_DB}, {@link #TAG_NETWORK},
     *            {@link #TAG_UTILITY}, user define string value.
     * @param msg The message you would like logged.
     * @param author author of code, default value is {@link #AUTHOR_DEFAULT}
     * @param e A exception to log.
     */
    public static void v(String tag, String msg, String author, Throwable e) {
        printLog(VERBOSE, tag, msg, author, e);
    }

    /**
     * Send an specify {@code logType} log message.
     * 
     * @param logType log type, e.g. {@link #ERROR}, {@link #DEBUG}.
     * @param tag suggest use function tag description, e.g.
     *            {@link #TAG_DEFAULT}, {@link #TAG_DB}, {@link #TAG_NETWORK},
     *            {@link #TAG_UTILITY}, user define string value.
     * @param msg The message you would like logged.
     * @param author author of code, default value is {@link #AUTHOR_DEFAULT}
     * @param e A exception to log.
     */
    public static void log(int logType, String tag, String msg, String author, Throwable e) {
        printLog(logType, tag, msg, author, e);
    }

    /**
     * Send an specify {@code logType} log message.
     * 
     * @param logType
     * @param tag
     * @param msg
     * @param author
     * @param e
     */
    private static void printLog(int logType, String tag, String msg, String author, Throwable e) {
        if (sToggleRelease) {
            if (logType < INFO) {
                return;
            }

            String tagStr = sTagDefault;
            String msgStr = (e == null) ? msg : (msg + "\n" + android.util.Log
                    .getStackTraceString(e));

            switch (logType) {
                case ERROR:
                    android.util.Log.e(tagStr, msgStr);

                    break;
                case WARN:
                    android.util.Log.w(tagStr, msgStr);

                    break;
                case INFO:
                    android.util.Log.i(tagStr, msgStr);

                    break;
                default:
                    break;
            }

            return;
        }

        if ((sToggleRelease && logType >= INFO)
                || (!sToggleRelease && sToggle && logType >= sLogLevel)) {
            String tagStr = tag == null ? sTagDefault : tag;
            StringBuilder msgStr = new StringBuilder();

            if (sToggleAuthor) {
                msgStr.append("[");
                msgStr.append(author == null ? sAuthorDefault : author);
                msgStr.append("] ");
            }

            if (sToggleThread || sToggleClassMethod || sToggleFileLineNumber) {
                Thread currentThread = Thread.currentThread();

                if (sToggleThread) {
                    msgStr.append("<");
                    msgStr.append(currentThread.getName());
                    msgStr.append("> ");
                }

                if (sToggleClassMethod) {
                    StackTraceElement ste = currentThread.getStackTrace()[4];

                    String className = ste.getClassName();

                    msgStr.append("[");
                    msgStr.append(className == null ? null : className.substring(className
                            .lastIndexOf('.') + 1));
                    msgStr.append("::");
                    msgStr.append(ste.getMethodName());
                    msgStr.append("] ");
                }

                if (sToggleFileLineNumber) {
                    StackTraceElement ste = currentThread.getStackTrace()[4];

                    msgStr.append("[");
                    msgStr.append(ste.getFileName());
                    msgStr.append("::");
                    msgStr.append(ste.getLineNumber());
                    msgStr.append("] ");
                }
            }

            msgStr.append(msg);

            if (e != null && sToggleThrowable) {
                msgStr.append('\n');
                msgStr.append(android.util.Log.getStackTraceString(e));
            }

            switch (logType) {
                case ERROR:
                    android.util.Log.e(tagStr, msgStr.toString());

                    break;
                case WARN:
                    android.util.Log.w(tagStr, msgStr.toString());

                    break;
                case INFO:
                    android.util.Log.i(tagStr, msgStr.toString());

                    break;
                case DEBUG:
                    android.util.Log.d(tagStr, msgStr.toString());

                    break;
                case VERBOSE:
                    android.util.Log.v(tagStr, msgStr.toString());

                    break;
                default:
                    break;
            }
        }
    }

	public static String length(byte[] arrby) {
		if(arrby == null){
			return "null";
		}
		return arrby.length+"";
	}
}
