package com.king.video;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by leo on 2017/2/10. 视频文件分割操作类
 */
public class VideoFileOperate {
    // 分割视频的大小，装包动作为了避免越界。long应该够使了。。。
    // private long blockSize = 1 * new Long(1024) * 1024 * 1024;
    private long blockSize = 100 * 1024 * 1024;
    // private loadingListener mListener;
    // private boolean ffmpegWorkingFlag = false;

    public static void main(String[] args) throws Exception {
        VideoFileOperate v = new VideoFileOperate();
        // v.cutVideo("D:\\fm\\jj.mp4");
        v.screenShot("D:\\fm\\jj.mp4");
    }

    /**
     * 获取视频文件时长
     *
     * @param file
     *            文件
     * @return 时长 格式hh:MM:ss
     * @throws FileNotFoundException
     *             视频不存在抛出此异常
     */
    private String getVideoTime(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + "不存在");
        }
        List<String> commands = new ArrayList<String>();
        commands.add("D:/Develop/ffmpeg/ffmpeg-20180704-5aba5b8-win64-static/bin/ffmpeg.exe");
        commands.add("-i");
        commands.add(file.getAbsolutePath());
        CmdResult result = runCommand(commands);
        String msg = result.getMsg();
        System.out.println(msg);
        if (result.isSuccess()) {
            // \d{2}:\d{2}:\d{2}
            Pattern pattern = Pattern.compile(" \\d{2}:\\d{2}:\\d{2}");
            Matcher matcher = pattern.matcher(msg);
            String time = "";
            while (matcher.find()) {
                time = matcher.group();
            }
            return time;
        } else {
            return "";
        }
    }

    /**
     * 获取视频文件时长
     *
     * @param file
     *            文件
     * @return 时长 格式hh:MM:ss
     * @throws FileNotFoundException
     *             视频不存在抛出此异常
     * @throws JSONException
     */
    private int getVideoTime2(File file) throws FileNotFoundException, JSONException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + "不存在");
        }
        List<String> commands = new ArrayList<String>();
        commands.add("D:/Develop/ffmpeg/ffmpeg-20180704-5aba5b8-win64-static/bin/ffprobe.exe");
        commands.add("-v");
        commands.add("quiet");
        commands.add("-print_format");
        commands.add("json");
        commands.add("-show_format");
        commands.add("-show_streams");
        commands.add(file.getAbsolutePath());
        CmdResult result = runCommand(commands);
        String msg = result.getMsg();
        System.out.println(msg);
        if (result.isSuccess()) {
            JSONObject json = new JSONObject(msg);
            JSONArray streams = json.getJSONArray("streams");
            JSONObject video = streams.getJSONObject(0);
            double duration = video.getDouble("duration");
            return (int) duration;
        } else {
            return 0;
        }
    }

    /**
     * 获取文件大小
     *
     * @param file
     *            去的文件长度，单位为字节b
     * @return 文件长度的字节数
     * @throws FileNotFoundException
     *             文件未找到异常
     */
    private long getVideoFileLength(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + "不存在");
        }
        return file.length();
    }

    /**
     * @param filePath
     *            要处理的文件路径
     * @return 分割后的文件路径
     * @throws Exception
     *             文件
     */
    List<String> cutVideo(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath + "文件不存在");
        }
        if (!filePath.endsWith(".mp4")) {
            throw new Exception("文件格式错误");
        }
        // // 从ffmpeg获得的时间长度00:00:00格式
        // String videoTimeString = getVideoTime(file);
        // System.err.println(String.format("总时长（分钟）：%s", videoTimeString));
        // // 将时长转换为秒数
        // int videoSecond = parseTimeToSecond(videoTimeString);
        // System.err.println(String.format("总时长（秒）：%s", videoSecond));
        //
        int videoSecond = this.getVideoTime2(file);
        // 视频文件的大小
        long fileLength = getVideoFileLength(file);
        List<String> cutedVideoPaths = new ArrayList<String>();
        if (fileLength <= blockSize) {// 如果视频文件大小不大于预设值，则直接返回原视频文件
            cutedVideoPaths.add(filePath);
        } else {// 如果超过预设大小，则需要切割
            int partNum = (int) (fileLength / blockSize);// 文件大小除以分块大小的商
            long remainSize = fileLength % blockSize;// 余数
            int cutNum;
            if (remainSize > 0) {
                cutNum = partNum + 1;
            } else {
                cutNum = partNum;
            }
            int eachPartTime = videoSecond / cutNum;
            List<String> commands = new ArrayList<String>();
            String fileFolder = file.getParentFile().getAbsolutePath();
            String fileName[] = file.getName().split("\\.");
            commands.add("D:/Develop/ffmpeg/ffmpeg-20180704-5aba5b8-win64-static/bin/ffmpeg.exe");
            for (int i = 0; i < cutNum; i++) {
                commands.add("-i");
                commands.add(filePath);
                commands.add("-ss");
                commands.add(parseTimeToString(eachPartTime * i));
                if (i != cutNum - 1) {
                    commands.add("-t");
                    commands.add(parseTimeToString(eachPartTime));
                }
                commands.add("-acodec");
                commands.add("copy");
                commands.add("-vcodec");
                commands.add("copy");
                commands.add(fileFolder + File.separator + fileName[0] + "_part" + i + "." + fileName[1]);
                commands.add("-y");
                cutedVideoPaths.add(fileFolder + File.separator + fileName[0] + "_part" + i + "." + fileName[1]);
                break;
            }
            System.out.println(commands);
            runCommand(commands);
        }
        return cutedVideoPaths;
    }

    /**
     * 屏幕截图
     * 
     * @param file
     * @throws Exception
     */
    void screenShot(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath + "文件不存在");
        }
        if (!filePath.endsWith(".mp4")) {
            throw new Exception("文件格式错误");
        }
        int videoSecond = this.getVideoTime2(file);
        String fileFolder = file.getParentFile().getAbsolutePath();
        String fileName[] = file.getName().split("\\.");
        for (int i = 0; i < videoSecond; i++) {
            final List<String> commands = new ArrayList<String>();
            commands.add("D:/Develop/ffmpeg/ffmpeg-20180704-5aba5b8-win64-static/bin/ffmpeg.exe");
            commands.add("-ss");
            commands.add(parseTimeToString(i));
            commands.add("-i");
            commands.add(filePath);
            commands.add(fileFolder + File.separator + "img" + File.separator + fileName[0] + "_" + i + ".jpg");
            commands.add("-r");
            commands.add("1");
            commands.add("-vframes");
            commands.add("1");
            commands.add("-an");
            commands.add("-f");
            commands.add("mjpeg");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(commands.toString());
                    runCommand(commands);
                }
            }).start();
        }
    }

    /**
     * 执行Cmd命令方法
     *
     * @param command
     *            相关命令
     * @return 执行结果
     */
    private synchronized CmdResult runCommand(List<String> command) {
        CmdResult cmdResult = new CmdResult(false, "");
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            final StringBuilder stringBuilder = new StringBuilder();
            final InputStream inputStream = process.getInputStream();
            new Thread(new Runnable() {// 启动新线程为异步读取缓冲器，防止线程阻塞

                @Override
                public void run() {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                            // mListener.isLoading(true);
                            // System.out.println(line);
                        }
                        // mListener.isLoading(false);
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            process.waitFor();
            cmdResult.setSuccess(true);
            cmdResult.setMsg(stringBuilder.toString());
            System.out.println(String.format("执行结果:%s", stringBuilder.toString()));
        } catch (Exception e) {
            throw new RuntimeException("ffmpeg执行异常" + e.getMessage());
        }
        return cmdResult;
    }

    /**
     * 将字符串时间格式转换为整型，以秒为单位
     *
     * @param timeString
     *            字符串时间时长
     * @return 时间所对应的秒数
     */
    private int parseTimeToSecond(String timeString) {
        Pattern pattern = Pattern.compile(" \\d{2}:\\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(timeString);
        if (!matcher.matches()) {
            try {
                throw new Exception("时间格式不正确");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] time = timeString.trim().split(":");
        return Integer.parseInt(time[0]) * 3600 + Integer.parseInt(time[1]) * 60 + Integer.parseInt(time[2]);
    }

    /**
     * 将秒表示时长转为00:00:00格式
     *
     * @param second
     *            秒数时长
     * @return 字符串格式时长
     */
    private String parseTimeToString(int second) {
        int end = second % 60;
        int mid = second / 60;
        if (mid < 60) {
            return "00:" + mid + ":" + end;
        } else if (mid == 60) {
            return "1:00:" + end;
        } else {
            int first = mid / 60;
            mid = mid % 60;
            return first + ":" + mid + ":" + end;
        }

    }

    interface loadingListener {
        void isLoading(boolean loading);
    }

    // /**
    // * 用于判断ffmpeg是否在工作
    // *
    // * @return true在工作 暂时无法验证是否准确
    // */
    // public boolean isFFmpegWorking() {
    //
    // mListener = new loadingListener() {
    // @Override
    // public void isLoading(boolean loading) {
    // ffmpegWorkingFlag = loading;
    // }
    // };
    // return ffmpegWorkingFlag;
    // }
}

class PrintStream extends Thread {
    java.io.InputStream __is = null;

    public PrintStream(java.io.InputStream is) {
        __is = is;
    }

    public void run() {
        try {
            while (this != null) {
                int _ch = __is.read();
                if (_ch != -1)
                    System.out.print((char) _ch);
                else
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}