package com.king.video;

import com.king.util.FileUtil;
import com.king.video.util.VideoUtil;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DaveAndAva2 {
    static final VideoUtil util = new VideoUtil();

    public static String[] bgColor = new String[]{
            "#E54B4B",
            "#157C80",
            "#34B57A",
            "#0D38B1",
            "#035A5B",
            "#3C765F",
            "#384D9D",
    };
    public static String[] fontColor = new String[]{
            "#FFFFFF",
            "#FFFFFF",
            "#FFFFFF",
            "#FFFFFF",
            "#FFFFFF",
            "#F2CA6C",
            "#FFFFFF",
    };

    public static String[] paomadeng = new String[]{
            "哇，好棒的儿歌，关注视频号，可领取完整视频。",
            "孩子特别喜欢，关注视频号，可领取完整视频。",
            "启蒙视频好棒，孩子好喜欢，关注视频号，可领取完整视频。",
            "英语儿歌特别棒，关注视频号，可领取完整视频。",
            "真的超棒，关注视频号，可领取完整视频。"
    };

    public static void main(String[] args) throws IOException {
        run(DaveAndAva.type);
    }

    private static void run(DaveAndAvaForType type) throws IOException {
        String filePath = "/Users/kingtiger/Downloads/Me/儿童教育/DaveAndAva/" + type.getName();
        File dir = new File(filePath);
        boolean exists = dir.exists();
        if (!exists) {
            throw new FileNotFoundException(filePath + "文件不存在");
        }
        File[] files = dir.listFiles();
        int w = 1280;
        //  文字坐标
        String x = "(w-text_w)/2";
        String y = "(h-text_h)/5";
        //
        String fontfile = "/Users/kingtiger/Library/Fonts/5337-b7e2fa7b49.ttf";
        // 字体大小
        int fontSize = 100;
        // 阴影
        String shadowy = "0";
        // 字体颜色
        String fontcolor = "";
        // 背景色
        String boxcolor = "";
        //
        String box = "1";

        for (int i = 0; i < files.length; i++) {
            File subDir = files[i];
            if (!subDir.isDirectory()) {
                continue;
            }
            String indexStr = subDir.getName();
            int indexInt = Integer.parseInt(indexStr);
            if (!DaveAndAva.checkIndexRun(indexInt)) {
                continue;
            }
            int indexColor = getrandom(0, bgColor.length - 1);
            boxcolor = bgColor[indexColor];
            fontcolor = fontColor[indexColor];
            System.out.println("创建文件夹");
            String absolutePath = subDir.getAbsolutePath();
            String headImgDir = absolutePath + "/headImg";
            String headDir = absolutePath + "/head";
            String bodyDir = absolutePath + "/body";
            String filelistDir = absolutePath + "/filelist";
            String outDir = absolutePath + "/out";
            String targetDir = absolutePath + "/target";
            new File(headImgDir).mkdirs();
            new File(headDir).mkdirs();
            new File(bodyDir).mkdirs();
            new File(filelistDir).mkdirs();
            new File(outDir).mkdirs();
            new File(targetDir).mkdirs();
            // 创建子文件夹
            DaveAndAva.printLine();
            for (int j = 1; j < 4; j++) {
                // 图片生成头部视频
                System.out.println("图片生成视频");
                List<String> commands = new ArrayList<String>();
                String headMp4 = headDir + "/douyin_" + j + ".mp4";
                if (!DaveAndAva.checkExists(headMp4)) {
                    commands.clear();
                    commands.add(VideoUtil.FFMPEG);
                    commands.add("-y");
                    commands.add("-loop");
                    commands.add("1");
                    commands.add("-i");
                    commands.add(headImgDir + "/douyin_" + j + ".jpg");
                    commands.add("-i");
                    commands.add("/Users/kingtiger/sph/VideoScript/mp3/source.aac");
                    commands.add("-vf");
                    StringBuilder commandSb = new StringBuilder();
                    if (type == DaveAndAvaForType.DouYin) {
                        commandSb.append(String.format("fps=30,scale=-1:%s", w));
                    } else {
                        commandSb.append(String.format("fps=30,scale=%s:-1", w));
                    }
                    commands.add(commandSb.toString());
                    commands.add("-t");
                    commands.add("2");
                    commands.add("-c:v");
                    commands.add("libx264");
                    commands.add("-c:a");
                    commands.add("aac");
                    commands.add("-strict");
                    commands.add("experimental");
                    commands.add("-b:a");
                    commands.add("192k");
                    commands.add("-shortest");
                    commands.add(headMp4);
                    util.runCommand(commands);
                    System.out.println("图片生成视频-OK");
                } else {
                    System.out.println("图片生成视频-存在");
                }
                DaveAndAva.printLine();
                // 头部视频转流
                System.out.println("头部视频转流");
                String headDirTs = headDir + "/douyin_" + j + ".ts";
                if (!DaveAndAva.checkExists(headDirTs)) {
                    commands.clear();
                    commands.add(VideoUtil.FFMPEG);
                    commands.add("-y");
                    commands.add("-i");
                    commands.add(headMp4);
                    commands.add("-vcodec");
                    commands.add("copy");
                    commands.add("-acodec");
                    commands.add("copy");
                    commands.add("-vbsf");
                    commands.add("h264_mp4toannexb");
                    commands.add(headDirTs);
                    util.runCommand(commands);
                    System.out.println("头部视频转流—OK");
                } else {
                    System.out.println("头部视频转流-存在");
                }
                DaveAndAva.printLine();
                // 主体视频转流
                System.out.println("主体视频转流");
                String bodyTs = bodyDir + "/douyin_" + j + ".ts";
                if (!DaveAndAva.checkExists(bodyTs)) {
                    commands.clear();
                    commands.add(VideoUtil.FFMPEG);
                    commands.add("-y");
                    commands.add("-i");
                    commands.add(bodyDir + "/douyin_" + j + ".mp4");
                    commands.add("-vcodec");
                    commands.add("copy");
                    commands.add("-acodec");
                    commands.add("copy");
                    commands.add("-vbsf");
                    commands.add("h264_mp4toannexb");
                    commands.add(bodyTs);
                    util.runCommand(commands);
                    System.out.println("主体视频转流-OK");
                } else {
                    System.out.println("主体视频转流-存在");
                }
                DaveAndAva.printLine();
                // 写入合并文件
                System.out.println("写入合并文件");
                String outMp4 = outDir + "/douyin_" + j + ".mp4";
                if (!DaveAndAva.checkExists(outMp4)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("file ").append("'").append(headDirTs).append("'").append("\n");
                    sb.append("file ").append("'").append(bodyTs).append("'").append("\n");
                    String footTs;
                    if (type == DaveAndAvaForType.DouYin) {
                        footTs = "/Users/kingtiger/sph/VideoScript/video/douyin2_V.ts";
                    } else {
                        footTs = "/Users/kingtiger/sph/VideoScript/video/shipinhao2_H.ts";
                    }
                    sb.append("file ").append("'").append(footTs).append("'").append("\n");
                    System.out.println(sb.toString());
                    String filelist = String.format(filelistDir + "/filelist_%s.txt", j);
                    FileUtil.str2File(sb.toString(), filelist);
                    // 视频合成
                    System.out.println("视频合成");
                    commands.clear();
                    commands.add(VideoUtil.FFMPEG);
                    commands.add("-y");
                    commands.add("-f");
                    commands.add("concat");
                    commands.add("-safe");
                    commands.add("0");
                    commands.add("-i");
                    commands.add(filelist);
                    commands.add("-bsf:a");
                    commands.add("aac_adtstoasc");
                    //
                    //
                    if (type == DaveAndAvaForType.ShiPinHao) {
                        commands.add("-vf");
                        StringBuilder commandSb = new StringBuilder();
                        commandSb.append(String.format("pad=iw:iw*7/6:0:(oh-ih)/2:%s", boxcolor));
                        // 引导
                        String text = "▲点击头像关注我";
                        x = "85";
                        y = "40";
                        fontSize = 55;
                        fontfile = "/Users/kingtiger/Library/Fonts/HYQiHei-60S.otf";
                        commandSb.append(String.format(",drawtext=fontfile='%s':text='%s':x=%s:y=%s:fontsize=%s:fontcolor=%s:shadowy=%s:box=%s:boxcolor=%s", fontfile, text, x, y, fontSize, fontcolor, shadowy, box, boxcolor));
                        // 默认值
                        x = "(w-text_w)/2";
                        fontSize = 100;
                        fontfile = "/Users/kingtiger/Library/Fonts/5337-b7e2fa7b49.ttf";
                        // 标题
                        text = String.format("No.%s Part-%s", indexStr, j);
                        y = "(h-text_h)/6.2";
                        commandSb.append(String.format(",drawtext=fontfile='%s':text='%s':x=%s:y=%s:fontsize=%s:fontcolor=%s:shadowy=%s:box=%s:boxcolor=%s", fontfile, text, x, y, fontSize, fontcolor, shadowy, box, boxcolor));
                        // 脚
                        text = "每天一首儿歌\n快乐英语启蒙";
                        y = "(h-text_h)-144";
                        commandSb.append(String.format(",drawtext=fontfile='%s':text='%s':x=%s:y=%s:fontsize=%s:fontcolor=%s:shadowy=%s:box=%s:boxcolor=%s", fontfile, text, x, y, fontSize, fontcolor, shadowy, box, boxcolor));
                        // 字幕
                        // subtitles=subtitle.ass
                        //commandSb.append(String.format(",subtitles=%s:force_style='Fontsize=24,PrimaryColour=&H80000000'", "/Users/kingtiger/sph/VideoScript/zimu/test.srt"));
                        // 跑马灯
                        text = paomadeng[getrandom(0, paomadeng.length - 1)];
                        fontfile = "/Users/kingtiger/Library/Fonts/HYQiHei-60S.otf";
                        fontSize = 55;
                        int sped = 10;
                        commandSb.append(String.format(",drawtext=fontfile=%s:text=%s:y=h-line_h-%s:x=(w-mod(%s*n\\,w+tw)):fontcolor=%s:fontsize=%s:shadowx=1:shadowy=1", fontfile, text, fontSize * 0.4, sped, fontcolor, fontSize));
                        //
                        commands.add(commandSb.toString());
                    }
                    //commands.add("-filter_complex");
                    //commands.add(String.format("subtitles=%s:force_style='Fontsize=24,PrimaryColour=&H80000000'", "/Users/kingtiger/sph/VideoScript/zimu/test.srt"));
                    //
                    commands.add("-y");
                    commands.add(outMp4);
                    util.runCommand(commands);
                    System.out.println("写入合并文件-OK");
                } else {
                    System.out.println("写入合并文件-已存在");
                }
                DaveAndAva.printLine();
                // 增加封面
                System.out.println("增加封面");
                String targetMp4 = targetDir + "/douyin_" + j + ".mp4";
                if (!DaveAndAva.checkExists(targetMp4)) {
                    commands.clear();
                    commands.add(VideoUtil.FFMPEG);
                    commands.add("-i");
                    commands.add(outMp4);
                    commands.add("-i");
                    commands.add(headImgDir + "/douyin_" + j + ".jpg");
                    commands.add("-map");
                    commands.add("1");
                    commands.add("-map");
                    commands.add("0");
                    commands.add("-c");
                    commands.add("copy");
                    commands.add("-disposition:0");
                    commands.add("attached_pic");
                    commands.add(targetMp4);
                    commands.add("-y");
                    util.runCommand(commands);
                    System.out.println("增加封面-OK");
                } else {
                    System.out.println("增加封面-已存在");
                }
                DaveAndAva.printLine();
            }
            System.out.println("====================================");
        }
    }

    /**
     * 获取指定长度的16进制字符串.
     */
    public static String randomHexStr(int len) {
        try {
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < len; i++) {
                //随机生成0-15的数值并转换成16进制
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            return result.toString().toUpperCase();
        } catch (Exception e) {
            System.out.println("获取16进制字符串异常，返回默认...");
            return "00CCCC";
        }
    }

    public static Color randomColor() {
        int color = Integer.valueOf(randomHexStr(6), 16);
        return new Color(color);
    }

    public static int getrandom(int start, int end) {
        int num = (int) (Math.random() * (end - start + 1) + start);
        return num;
    }
}
