package com.lfk.justweengine.Utils.logger;

/**
 * @author Kale
 * @date 2016/3/25
 */
public class LogParser {

    private static final String SILENT = "-s"; // Set default filter to SILENT. Like '*:s'

    private static final String FILE = "-f"; //   <filename>   Log to FILE. Default to stdout

    private static final String BYTES = "-r"; //  Rotate log every bytes(k). (16 if unspecified). Requires -f 

    private static final String COUNT = "-n"; //  Sets max number of rotated logs to <count>, default 4

    private static final String FORMAT = "-v"; // Sets the log print format, where  is one of: brief process tag thread raw time 

    private static final String CLEAR = "-c"; //  clear (flush) the entire log and e // thread time 

    private static final String DUMP = "-d"; //   dump the log and then exit (don't block) // 不会引起线程阻塞

    ///////////////////////////////////////////////////////////////////////////
    // lev
    ///////////////////////////////////////////////////////////////////////////

    public static final String VERBOSE = "V"; //   Verbose (明细)  

    public static final String DEBUG = "D"; //     Debug (调试)

    public static final String INFO = "I"; //      Info (信息)

    public static final String WARN = "W"; //      Warn (警告)

    public static final String ERROR = "E"; //     Error (错误)

    private static final String FATAL = "F"; //     Fatal (严重错误)

    private static final String ASSERT = "S"; //    Silent(Super all output) (最高的优先级, 前所未有的错误); 

    public static String parse(Options options) {
        switch (options) {
            case SILENT:
                return SILENT;
            case FILE:
                return FILE;
            case BYTES:
                return BYTES;
            case COUNT:
                return COUNT;
            case FORMAT:
                return FORMAT;
            case CLEAR:
                return CLEAR;
            case DUMP:
                //return DUMP;
            default:
                return DUMP;
        }
    }

    public static String parse(LogLevel level) {
        switch (level) {
            case VERBOSE:
                return VERBOSE;
            case DEBUG:
                return DEBUG;
            case INFO:
                return INFO;
            case WARN:
                return WARN;
            case ERROR:
                return ERROR;
            case FATAL:
                return FATAL;
            case ASSERT:
                //return ASSERT;
            default:
                return ASSERT;
        }
    }

    public static LogLevel parseLev(String level) {
        switch (level) {
            case VERBOSE:
                return LogLevel.VERBOSE;
            case DEBUG:
                return LogLevel.DEBUG;
            case INFO:
                return LogLevel.INFO;
            case WARN:
                return LogLevel.WARN;
            case ERROR:
                return LogLevel.ERROR;
            case FATAL:
                return LogLevel.FATAL;
            case ASSERT:
                return LogLevel.ASSERT;
            default:
                return LogLevel.ASSERT;
        }
    }

}
