package com.king.util;


import java.io.*;


/**
 *
 */
public class FileUtil {

    /**
     * 拷贝文件夹
     *
     * @param srcPath
     * @param destPath
     */
    public static void copyDir(
            String srcPath,
            String destPath
    ) {
        File src = new File(srcPath);
        File dest = new File(destPath);
        copyDir(src, dest);
    }
    //拷贝文件夹方法
    //File 对象

    /**
     * 拷贝文件夹
     *
     * @param src
     * @param dest
     */
    public static void copyDir(
            File src,
            File dest
    ) {
        if (src.isDirectory()) {//文件夹
            dest = new File(dest, src.getName());
        }
        copyDirDetail(src, dest);
    }


    /**
     * 拷贝文件夹
     *
     * @param src
     * @param dest
     */
    public static void copyDirDetail(
            File src,
            File dest
    ) {
        //文件直接复制
        if (src.isFile()) {
            try {
                FileUtil.copyFile(src, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (src.isDirectory()) {//目录
            //确保文件夹存在
            dest.mkdirs();
            //获取下一级目录|文件
            for (File sub : src.listFiles()) {
                copyDirDetail(sub, new File(dest, sub.getName()));
            }
        }
    }

    /**
     * 拷贝文件
     *
     * @param srcPath
     * @param destPath
     * @throws IOException
     */
    public static void copyFile(
            String srcPath,
            String destPath
    ) throws IOException {
        File src = new File(srcPath);
        File dest = new File(destPath);
        copyFile(src, dest);
    }

    /**
     * 拷贝文件
     *
     * @param srcPath
     * @param destPath
     * @throws IOException
     */
    public static void copyFile(
            File srcPath,
            File destPath
    ) throws IOException {
        if (!destPath.exists()) {
            destPath.getParentFile().mkdirs();
        }
        //选择流
        InputStream is = new FileInputStream(srcPath);
        OutputStream os = new FileOutputStream(destPath);
        //拷贝  循环+读取+写出
        byte[] flush = new byte[1024];
        int len = 0;
        //读取
        while (-1 != (len = is.read(flush))) {
            //写出
            os.write(flush, 0, len);
        }
        os.flush();//强制刷出
        //关闭流  先打开后关闭
        os.close();
        is.close();
    }

    public static void str2File(String str, String filePath) {
        FileWriter writer;
        try {
            writer = new FileWriter(filePath);
            //清空原文件内容
            writer.write("");
            writer.write(str);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
