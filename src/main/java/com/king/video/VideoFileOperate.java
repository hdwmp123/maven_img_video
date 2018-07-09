package com.king.video;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.king.video.util.VideoUtil;

/**
 * 
 * @ClassName: VideoFileOperate
 * @Description: 视频切割
 * @author wangmipeng
 * @date 2018年7月6日 下午11:04:04
 *
 */
public class VideoFileOperate {
    // 单个文件大小
    private long blockSize = 100 * 1024 * 1024;
    static final VideoUtil util = new VideoUtil();

    public static void main(String[] args) throws Exception {
        VideoFileOperate v = new VideoFileOperate();
        String filePath = "E:\\DaveAndAva\\01.mp4";
        util.screenShot(filePath);
        //v.cutVideo(filePath);
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
        int videoSecond = util.getVideoTime2(file);
        // 视频文件的大小
        long fileLength = util.getVideoFileLength(file);
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
            commands.add(VideoUtil.FFMPEG);
            for (int i = 0; i < cutNum; i++) {
                commands.add("-i");
                commands.add(filePath);
                commands.add("-ss");
                commands.add(util.parseTimeToString(eachPartTime * i));
                if (i != cutNum - 1) {
                    commands.add("-t");
                    commands.add(util.parseTimeToString(eachPartTime));
                }
                commands.add("-acodec");
                commands.add("copy");
                commands.add("-vcodec");
                commands.add("copy");
                commands.add(fileFolder + File.separator + fileName[0]
                        + "_part" + i + "." + fileName[1]);
                commands.add("-y");
                cutedVideoPaths.add(fileFolder + File.separator + fileName[0]
                        + "_part" + i + "." + fileName[1]);
                break;
            }
            System.out.println(commands);
            util.runCommand(commands);
            
        }
        return cutedVideoPaths;
    }
}
