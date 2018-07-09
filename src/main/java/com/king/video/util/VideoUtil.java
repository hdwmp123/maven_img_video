package com.king.video.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.king.video.CmdResult;

public class VideoUtil {
    private static String FFMPEG_BASE = "D:/Develop/ffmpeg/ffmpeg-win64-static/bin/";
    public static String FFMPEG = FFMPEG_BASE + "ffmpeg.exe";
    public static String FFPROBE = FFMPEG_BASE + "ffprobe.exe";

    /**
     * 
     * @param file
     * @return 和获取视频时长
     * @throws FileNotFoundException
     * @throws JSONException
     */
    public int getVideoTime2(File file) throws FileNotFoundException,
            JSONException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + "不存在");
        }
        List<String> commands = new ArrayList<String>();
        commands.add(FFPROBE);
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
     *            取的文件长度，单位为字节b
     * @return 文件长度的字节数
     * @throws FileNotFoundException
     *             文件未找到异常
     */
    public long getVideoFileLength(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + "不存在");
        }
        return file.length();
    }

    /**
     * 将秒表示时长转为00:00:00格式
     *
     * @param second
     *            秒数时长
     * @return 字符串格式时长
     */
    public String parseTimeToString(int second) {
        int mid = second / 60;// 分钟
        int end = second % 60;// 秒
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

    /**
     * 执行Cmd命令方法
     *
     * @param command
     *            相关命令
     * @return 执行结果
     */
    public synchronized CmdResult runCommand(List<String> command) {
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
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(inputStream));
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
            System.out.println(String.format("执行结果:%s",
                    stringBuilder.toString()));
        } catch (Exception e) {
            throw new RuntimeException("ffmpeg执行异常" + e.getMessage());
        }
        return cmdResult;
    }

    /**
     * 屏幕截图
     * 
     * @param file
     * @throws Exception
     */
    public void screenShot(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath + "文件不存在");
        }
        if (!filePath.endsWith(".mp4")) {
            throw new Exception("文件格式错误");
        }
        int videoSecond = getVideoTime2(file);
        String fileFolder = file.getParentFile().getAbsolutePath();
        String fileName[] = file.getName().split("\\.");
        for (int i = 0; i < videoSecond; i++) {
            final List<String> commands = new ArrayList<String>();
            commands.add(VideoUtil.FFMPEG);
            commands.add("-ss");
            commands.add(parseTimeToString(i+1));
            commands.add("-i");
            commands.add(filePath);
            commands.add(fileFolder + File.separator + "img" + File.separator
                    + fileName[0] + "_" + (i+1) + ".jpg");
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
            break;
        }
        
    }
}
