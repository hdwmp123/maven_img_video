package com.king.video;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.king.video.util.VideoUtil;

/**
 * 视频分隔
 */
public class DaveAndAva {
    static final VideoUtil util = new VideoUtil();

    public static void main(String[] args) throws Exception {
        String filePath = "E:\\DaveAndAva\\01.mp4";
        DaveAndAva d = new DaveAndAva();
        d.cutVideo(filePath);
    }

    /**
     * @param filePath
     *            要处理的文件路径
     * @return 分割后的文件路径
     * @throws Exception
     *             文件
     */
    void cutVideo(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath + "文件不存在");
        }
        if (!filePath.endsWith(".mp4")) {
            throw new Exception("文件格式错误");
        }
        // 分隔标题
        String[] title = new String[] { 
                "1",
                "2"
        };
        int[] time = new int[] { 
                22,
                217
        };
        List<String> commands = new ArrayList<String>();
        String fileFolder = file.getParentFile().getAbsolutePath();
        String fileName[] = file.getName().split("\\.");
        commands.add(VideoUtil.FFMPEG);
        int step = 1;
        for (int i = 0; i < title.length; i++) {
            commands.add("-i");
            commands.add(filePath);
            commands.add("-ss");
            commands.add(util.parseTimeToString(time[i]));
            if (i != title.length - 1) {
                commands.add("-t");
                commands.add(util.parseTimeToString(time[i + 1] - time[i]));
            }
            commands.add("-acodec");
            commands.add("copy");
            commands.add("-vcodec");
            commands.add("copy");
            commands.add(fileFolder + File.separator + title[i] + "."
                    + fileName[1]);
            commands.add("-y");
        }
        System.out.println(commands);
        util.runCommand(commands);
    }
}
