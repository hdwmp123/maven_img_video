package com.king.video;

import com.king.util.FileUtil;
import com.king.video.util.VideoUtil;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视频分隔
 *
 * @author kingtiger
 */
public class DaveAndAvaForDouYin {
    static final VideoUtil util = new VideoUtil();

    public static void main(String[] args) throws Exception {
        String filePath = "/Users/kingtiger/Downloads/Me/儿童教育/DaveAndAva/mp4shipinhao";
        copy2Sub(filePath, false);
    }

    private static void copy2Sub(String filePath, boolean transpose) throws Exception {
        File dir = new File(filePath);
        boolean exists = dir.exists();
        if (!exists) {
            throw new FileNotFoundException(filePath + "文件不存在");
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String name = file.getName();
            if (!name.endsWith("mp4")) {
                continue;
            }
            String index = name.split(" ")[3].replace("[", "").replace("]", "");
            System.out.println(index + "-----------------------------");
            File subDir = new File(filePath + "/" + index);
            if (!subDir.exists()) {
                subDir.mkdirs();
            }
            File subFile = new File(subDir.getAbsolutePath() + "/source.mp4");
            if (!subFile.exists()) {
                FileUtil.copyFile(file, subFile);
            }
            //
            String[] ss = getSs(index);
            if (ss != null) {
                cutVideo(subFile.getAbsolutePath(), ss, transpose);
            }
            //break;
        }
        for (int j = 0; j < files.length; j++) {
            //
            File file = files[j];
            String name = file.getName();
            if (!name.endsWith("mp4")) {
                continue;
            }
            String index = name.split(" ")[3].replace("[", "").replace("]", "");
            File subDir = new File(filePath + "/" + index);
            //
            int size = 3;
            int h = 720;
            int w = 1280 / size;
            int x = 0;
            String douyin_out_jpg = subDir + "/douyin_out.jpg";
            if (!isExists(douyin_out_jpg)) {
                continue;
            }
            System.out.println(douyin_out_jpg);
            for (int i = 0; i < size; i++) {
                try {
                    Thumbnails.of(douyin_out_jpg)
                            .size(1280, 720)
                            .outputFormat("jpg")
                            .keepAspectRatio(true)
                            .toFile(douyin_out_jpg);
                    String out = String.format(subDir + "/douyin_out_%s.jpg", i + 1);
                    if (!isExists(out)) {
                        Thumbnails.of(douyin_out_jpg)
                                .sourceRegion(x, 0, w, h)
                                .size(w, h)
                                .outputFormat("jpg")
                                .keepAspectRatio(false)
                                .toFile(out);
                    }
                    x += w;
                    System.out.println(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("-----------------------------");
        }
    }

    public static String[] getSs(String index) {
        String[] result = null;
        switch (index) {
            case "3":
                result = new String[]{
                        "00:00:20.510",
                        "00:00:19.400",
                        "00:01:19.400",
                        "00:02:19.400",
                };
                break;
            case "4":
            case "7":
            case "10":
            case "12":
            case "13":
            case "14":
            case "16":
            case "17":
            case "18":
            case "20":
            case "22":
            case "24":
            case "25":
            case "26":
            case "27":
            case "28":
            case "30":
            case "32":
            case "34":
            case "35":
            case "36":
            case "37":
            case "38":
            case "39":
            case "40":
            case "41":
            case "42":
            case "43":
            case "44":
            case "45":
            case "46":
            case "47":
            case "48":
            case "50":
            case "51":
            case "52":
            case "53":
            case "54":
            case "56":
            case "58":
            case "59":
            case "60":
            case "61":
            case "62":
            case "63":
            case "65":

            case "68":
            case "70":
            case "71":
            case "72":
            case "73":
            case "74":
            case "76":
            case "78":
            case "79":
            case "80":
            case "81":
                result = new String[]{
                        "00:00:21.510",
                        "00:00:19.400",
                        "00:01:19.400",
                        "00:02:19.400",
                };
                break;
            case "55":
            case "64":
            case "66":
                result = new String[]{
                        "00:00:21.000",
                        "00:00:19.400",
                        "00:01:19.400",
                        "00:02:19.400",
                };
                break;
            case "23":
                result = new String[]{
                        "00:00:21.310",
                        "00:00:19.400",
                        "00:01:19.400",
                        "00:02:19.400",
                };
                break;
            case "9":
                result = new String[]{
                        "00:00:22.510",
                        "00:00:19.400",
                        "00:01:19.400",
                        "00:02:19.400",
                };
                break;
            case "49":
                result = new String[]{
                        "00:00:37.510",
                        "00:00:34.400",
                        "00:01:34.400",
                        "00:02:34.400",
                };
                break;
            case "77":
                result = new String[]{
                        "00:00:36.810",
                        "00:00:34.400",
                        "00:01:34.400",
                        "00:02:34.400",
                };
                break;
            default:
                result = new String[]{
                        "00:00:36.510",
                        "00:00:34.400",
                        "00:01:34.400",
                        "00:02:34.400",
                };
                break;
        }
        return result;
    }

    /**
     * @param filePath 要处理的文件路径
     * @return 分割后的文件路径
     * @throws Exception 文件
     */
    static void cutVideo(String filePath, String[] ss, boolean transpose) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath + "文件不存在");
        }
        System.out.println(filePath);
        List<String> commands = new ArrayList<String>();
        String fileFolder = file.getParentFile().getAbsolutePath();
        commands.add(VideoUtil.FFMPEG);
//        # 视频截图
//        ffmpeg -i source.mp4 -y -f image2 -ss 00:00:36.510 -t 0.001 out.jpg
//        # 视频卡帧截取
//        ffmpeg -i source.mp4 -y -ss 00:00:34.400 -t 00:01:00.000 -vf "transpose=1" -strict -2  -qscale 0 -intra out1.mp4
//        ffmpeg -i source.mp4 -y -ss 00:01:34.400 -t 00:01:00.000 -vf "transpose=1" -strict -2  -qscale 0 -intra out2.mp4
//        ffmpeg -i source.mp4 -y -ss 00:02:34.400 -t 00:01:00.000 -vf "transpose=1" -strict -2  -qscale 0 -intra out3.mp4
        String douyin_out_jpg = fileFolder + "/douyin_out.jpg";
        if (!isExists(douyin_out_jpg)) {
            commands.add("-i");
            commands.add(filePath);
            commands.add("-y");
            commands.add("-f");
            commands.add("image2");
            commands.add("-ss");
            commands.add(ss[0]);
            commands.add("-t");
            commands.add("0.001");
            commands.add(douyin_out_jpg);
        }
        for (int i = 1; i < ss.length; i++) {
            String douyin_out_mp4 = String.format(fileFolder + "/douyin_%s.mp4", i);
            if (!isExists(douyin_out_mp4)) {
                commands.add("-i");
                commands.add(filePath);
                commands.add("-y");
                commands.add("-ss");
                commands.add(ss[i]);
                commands.add("-t");
                commands.add("00:01:00.000");
                if (transpose) {
                    commands.add("-vf");
                    commands.add("transpose=1");
                }
                commands.add("-strict");
                commands.add("-2");
                commands.add("-qscale");
                commands.add("0");
                commands.add("-intra");
                commands.add(douyin_out_mp4);
            }
        }
        System.out.println(commands);
        util.runCommand(commands);
    }

    private static boolean isExists(String filePath) {
        return new File(filePath).exists();
    }
}
