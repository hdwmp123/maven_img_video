package com.king.font;

import com.king.video.DaveAndAva2;
import com.king.video.DaveAndAvaForType;
import com.king.video.util.VideoUtil;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllFont {
    static final VideoUtil util = new VideoUtil();

    public static void main(String[] args) {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = e.getAvailableFontFamilyNames();
        for (String fontName : fontNames) {
            //System.out.println(fontName);
        }
        System.out.println("---------");
        //
        int w = 1280;
        //  文字坐标
        String x = "(w-text_w)/2";
        String y = "(h-text_h)/5";
        //
        String fontfile;
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
        //
        e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts();
        List<String> commands = new ArrayList<String>();
        for (Font font : fonts) {
            String fontName = font.getFontName();
        }
        File dir = new File("/Users/kingtiger/Library/Fonts/");
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String name = file.getName();
            if (!(name.endsWith("ttf") || name.endsWith("otf"))) {
                continue;
            }


        }
        String[] fontsArr = new String[]{
                "12-5315e16560.ttf",
                "5011-ed5e853f7d.ttf",
                "16-42a8749086.ttf",
                "18-85dce71883.ttf",
                "76-74bea24a25.ttf",
                "80-00691f2050.ttf",
                "82-84d1b9be8c.ttf",
                "263-245a88d0ef.ttf",
                "450-868aae1df3.ttf",
                "488-d833f28fc4.ttf",
                "528-52e0988dff.ttf",
                "666-c6530fc450.ttf",
                "903-16656dc107.ttf",
                "943-8dbecc9e72.ttf",
                "5010-8ba649cd4b.ttf",
                "5011-ed5e853f7d.ttf",
                "5012-13d3611caa.ttf",
                "5013-94692aa79b.ttf",
                "5014-61c98e351a.ttf",
                "5015-c560f40290.ttf",
                "5312-7bed99482e.ttf",
                "5313-c3c66f9fed.ttf",
                "5315-e1cb261c89.ttf",
                "5317-6705b36f5a.ttf",
                "5321-a03812633f.ttf",
                "5323-0e07e648bf.ttf",
                "5327-0ad1c966ef.ttf",
                "5335-d1de8464ae.ttf",
                "5337-b7e2fa7b49.ttf",
                "5349-03d9e1d166.ttf",
                "5357-8564a23205.ttf",
        };
        for (int i = 0; i < fontsArr.length; i++) {
            String name = fontsArr[i];
            //
            fontfile = "/Users/kingtiger/Library/Fonts/" + name;
            int indexColor = DaveAndAva2.getrandom(0, DaveAndAva2.bgColor.length - 1);
            boxcolor = DaveAndAva2.bgColor[indexColor];
            fontcolor = DaveAndAva2.fontColor[indexColor];
            //
            commands.clear();
            commands.add(VideoUtil.FFMPEG);
            commands.add("-y");
            commands.add("-loop");
            commands.add("1");
            commands.add("-i");
            commands.add("/Users/kingtiger/Downloads/Me/儿童教育/DaveAndAva/mp4shipinhao/30/headImg/douyin_3.jpg");
            commands.add("-i");
            commands.add("/Users/kingtiger/Downloads/Me/VideoScript/mp3/source.aac");
            commands.add("-vf");
            StringBuilder commandSb = new StringBuilder();
            commandSb.append(String.format("fps=30,scale=%s:-1", w));
            //
            String text = String.format("\nNo.%s Part-%s\n", "99", "3");
            commandSb.append(String.format(",drawtext=fontfile='%s':text='%s':x=%s:y=%s:fontsize=%s:fontcolor=%s:shadowy=%s:box=%s:boxcolor=%s", fontfile, text, x, y, fontSize, fontcolor, shadowy, box, boxcolor));
            text = "\n每天一首儿歌\n快乐英语启蒙\n";
            String tempY = y + "+120";
            commandSb.append(String.format(",drawtext=fontfile='%s':text='%s':x=%s:y=%s:fontsize=%s:fontcolor=%s:shadowy=%s:box=%s:boxcolor=%s", fontfile, text, x, tempY, fontSize, fontcolor, shadowy, box, boxcolor));
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
            commands.add(String.format("/Users/kingtiger/Downloads/Me/儿童教育/DaveAndAva/temp/%s.mp4", name.replace(" ", "_")));
            util.runCommand(commands);
            System.out.println(name);
        }
    }
}
