package com.example.coupon.controller;

import android.util.Log;

public class logger {
    private static Boolean LOG_SWITCH = true;   // 日志总开关
    //private static Boolean WRITE_LOG_TO_FILE = true;    // 日志写入文件开关
    public static void info (String tag, String msg) {
        if (LOG_SWITCH) {
            Log.i(tag, msg);
        }
    }

    public static void debug (String tag, String msg) {
        if (LOG_SWITCH) {
            Log.d(tag, msg);
        }
    }

    public static void error (String tag, String msg) {
        if (LOG_SWITCH) {
            Log.e(tag, msg);
        }
    }
//    private static Boolean MYLOG_SWITCH = true; // 日志文件总开关
//    private static Boolean MYLOG_WRITE_TO_FILE = true;// 日志写入文件开关
//    private static char MYLOG_TYPE = 'v';// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
//    private static String MYLOG_PATH_SDCARD_DIR = "/sdcard/kantu/log";// 日志文件在sdcard中的路径
//    private static int SDCARD_LOG_FILE_SAVE_DAYS = 0;// sd卡中日志文件的最多保存天数
//    private static String MYLOGFILEName = "Log.txt";// 本类输出的日志文件名称
//    private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式
//    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
//    public Context context;
//    /**
//     * 打开日志文件并写入日志
//     * @param mylogtype
//     * @param tag
//     * @param text
//     */
//    private static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打开日志文件
//        Date nowtime = new Date();
//        String needWriteFiel = logfile.format(nowtime);
//        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype + "    " + tag + "    " + text;
//        File dirPath = Environment.getExternalStorageDirectory();
//
//        File dirsFile = new File(MYLOG_PATH_SDCARD_DIR);
//        if (!dirsFile.exists()){
//            dirsFile.mkdirs();
//        }
//        //Log.i("创建文件","创建文件");
//        File file = new File(dirsFile.toString(), needWriteFiel + MYLOGFILEName);// MYLOG_PATH_SDCARD_DIR
//        if (!file.exists()) {
//            try {
//                //在指定的文件夹中创建文件
//                file.createNewFile();
//            } catch (Exception e) {
//            }
//        }
//
//        try {
//            FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
//            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
//            bufWriter.write(needWriteMessage);
//            bufWriter.newLine();
//            bufWriter.close();
//            filerWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 删除制定的日志文件
//     */
//    public static void delFile() {// 删除日志文件
//        String needDelFiel = logfile.format(getDateBefore());
//        File dirPath = Environment.getExternalStorageDirectory();
//        File file = new File(dirPath, needDelFiel + MYLOGFILEName);// MYLOG_PATH_SDCARD_DIR
//        if (file.exists()) {
//            file.delete();
//        }
//    }
//
//    /**
//     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
//     */
//    private static Date getDateBefore() {
//        Date nowtime = new Date();
//        Calendar now = Calendar.getInstance();
//        now.setTime(nowtime);
//        now.set(Calendar.DATE, now.get(Calendar.DATE) - SDCARD_LOG_FILE_SAVE_DAYS);
//        return now.getTime();
//    }
}
