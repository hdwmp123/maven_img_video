package com.king.video;

import com.king.util.FileUtil;
import com.king.video.util.VideoUtil;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 视频分隔
 *
 * @author kingtiger
 */
public class DaveAndAva {
    static final VideoUtil util = new VideoUtil();
    static DaveAndAvaForType type = DaveAndAvaForType.ShiPinHao;
    static String sourceDir = "/Users/kingtiger/Downloads/Me/儿童教育/DaveAndAva/mp4rename";
    /**
     * 视频时长
     */
    static int secondLength = 36;

    public static void main(String[] args) throws Exception {
        copy2Sub(type);
        DaveAndAva2.main(args);
        //DaveAndAva.type = DaveAndAvaForType.DouYin;
        //copy2Sub(type);
        //DaveAndAva2.main(args);
    }

    private static void copy2Sub(DaveAndAvaForType type) throws Exception {
        File dir = new File(sourceDir);
        boolean exists = dir.exists();
        if (!exists) {
            throw new FileNotFoundException(sourceDir + "文件不存在");
        }
        String filePath = "/Users/kingtiger/Downloads/Me/儿童教育/DaveAndAva/" + type.getName();
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String name = file.getName();
            if (!name.endsWith("mp4")) {
                continue;
            }
            String indexStr = getIndex(name);
            int indexInt = Integer.parseInt(indexStr);
            if (!checkIndexRun(indexInt)) {
                continue;
            }
            printLine2();
            // 创建子目录
            File subDir = new File(filePath + "/" + indexStr);
            if (!subDir.exists()) {
                subDir.mkdirs();
            }
            File body = new File(subDir.getAbsolutePath() + "/body");
            if (!body.exists()) {
                body.mkdirs();
            }
            File headImg = new File(subDir.getAbsolutePath() + "/headImg");
            if (!headImg.exists()) {
                headImg.mkdirs();
            }
            // 复制源视频
            File subFile = new File(subDir.getAbsolutePath() + "/source.mp4");
            if (!subFile.exists()) {
                FileUtil.copyFile(file, subFile);
            }
            // 视频截取
            String[] ss = getSs(indexStr);
            if (ss != null) {
                cutVideo(subFile.getAbsolutePath(), ss, type);
            }
            printLine2();
        }
        for (int j = 0; j < files.length; j++) {
            //
            File file = files[j];
            String name = file.getName();
            if (!name.endsWith("mp4")) {
                continue;
            }
            String index = getIndex(name);
            File headImgDir = new File(filePath + "/" + index + "/headImg");
            //
            int oldW = 2276;
            int oldH = 1280;
            //
            int size = 3;
            int h = 1280;
            int w = 720;
            int x = 0;
            int step = (oldW - w * 3) / 2;
            String douyin_jpg = headImgDir + "/douyin.jpg";
            if (!checkExists(douyin_jpg)) {
                continue;
            }
            System.out.println(douyin_jpg);
            for (int i = 0; i < size; i++) {
                try {
                    int[] wh = getWH(douyin_jpg);
                    if (wh[0] != oldW || wh[1] != oldH) {
                        Thumbnails.of(douyin_jpg)
                                .size(oldW, oldH)
                                .outputFormat("jpg")
                                .keepAspectRatio(true)
                                .toFile(douyin_jpg);
                    }

                    String out = String.format(headImgDir + "/douyin_%s.jpg", i + 1);
                    if (!checkExists(out)) {
                        if (type == DaveAndAvaForType.DouYin) {
                            Thumbnails.of(douyin_jpg)
                                    .sourceRegion(step + x, 0, w, h)
                                    .size(w, h)
                                    .outputFormat("jpg")
                                    .keepAspectRatio(false)
                                    .toFile(out);
                        } else {
                            FileUtil.copyFile(douyin_jpg, out);
                        }
                        System.out.println(x);
                        System.out.println(out);
                    }
                    x += w;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            printLine();
        }
    }

    /**
     * 获取索引
     *
     * @param name
     * @return
     */
    private static String getIndex(String name) {
        return name.split(" ")[3].replace("[", "").replace("]", "");
    }

    /**
     * @param filePath 要处理的文件路径
     * @return 分割后的文件路径
     * @throws Exception 文件
     */
    static void cutVideo(String filePath, String[] ss, DaveAndAvaForType type) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath + "文件不存在");
        }
        System.out.println(filePath);
        List<String> commands = new ArrayList<String>();
        String bodyDir = file.getParentFile().getAbsolutePath() + "/body";
        String headImgDir = file.getParentFile().getAbsolutePath() + "/headImg";
        // 视频截图
        String douyin_jpg = headImgDir + "/douyin.jpg";
        if (!checkExists(douyin_jpg)) {
            commands.clear();
            commands.add(VideoUtil.FFMPEG);
            commands.add("-ss");
            commands.add(ss[0]);
            commands.add("-i");
            commands.add(filePath);
            commands.add("-f");
            commands.add("image2");
            commands.add("-t");
            commands.add("0.001");
            commands.add("-y");
            commands.add(douyin_jpg);
            util.runCommand(commands);
        }
        String startTime = "";
        for (int i = 1; i < 4; i++) {
            String douyin_mp4 = String.format(bodyDir + "/douyin_%s.mp4", i);
            if (!checkExists(douyin_mp4)) {
                commands.clear();
                commands.add(VideoUtil.FFMPEG);
                commands.add("-ss");
                startTime = timeAdd(ss[1], (i - 1) * secondLength);
                commands.add(startTime);
                commands.add("-i");
                commands.add(filePath);
                commands.add("-t");
                commands.add(String.valueOf(secondLength));
                if (type == DaveAndAvaForType.DouYin) {
                    commands.add("-vf");
                    commands.add("transpose=1,scale=-1:1280");
                } else {
                    commands.add("-vf");
                    commands.add("scale=-1:720");
                }
                commands.add("-strict");
                commands.add("-2");
                commands.add("-qscale");
                commands.add("0");
                commands.add("-intra");
                commands.add(douyin_mp4);
                commands.add("-y");
                util.runCommand(commands);
            }
        }
    }

    public static String[] getSs(String index) {
        String[] result = null;
        switch (index) {
            case "3":
                result = new String[]{
                        "00:00:20.510",
                        "00:00:19.400",
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
                };
                break;
            case "55":
            case "64":
            case "66":
                result = new String[]{
                        "00:00:21.000",
                        "00:00:19.400",
                };
                break;
            case "23":
                result = new String[]{
                        "00:00:21.310",
                        "00:00:19.400",
                };
                break;
            case "9":
                result = new String[]{
                        "00:00:22.510",
                        "00:00:19.400",
                };
                break;
            case "49":
                result = new String[]{
                        "00:00:37.510",
                        "00:00:34.400",
                };
                break;
            case "77":
                result = new String[]{
                        "00:00:36.810",
                        "00:00:34.400",
                };
                break;
            default:
                result = new String[]{
                        "00:00:36.510",
                        "00:00:34.400",
                };
                break;
        }
        return result;
    }

    static SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 时间戳增加
     *
     * @param startTime
     * @param second
     */
    public static String timeAdd(String startTime, int second) {
        if (startTime == null || "".equals(startTime)) {
            return "00:00:00.000";
        }
        String date = "2000-01-01 " + startTime;
        try {
            Date relDate = fm.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(relDate);
            calendar.add(Calendar.SECOND, second);
            String result = fm.format(calendar.getTime());
            return result.substring(11, result.length());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 检测文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean checkExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 获取图片宽和高
     *
     * @param input
     * @return
     */
    public static int[] getWH(String input) {
        Thumbnails.Builder<File> fileBuilder = Thumbnails.of(input).scale(1.0).outputQuality(1.0);
        try {
            BufferedImage src = fileBuilder.asBufferedImage();
            return new int[]{
                    src.getWidth(null),
                    src.getHeight(null)
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[]{0, 0};
    }

    /**
     * 检测是否可运行
     *
     * @param indexInt
     * @return
     */
    public static boolean checkIndexRun(int indexInt) {
        return type.getStart() <= indexInt && indexInt <= type.getEnd();
    }

    public static void printLine() {
        System.out.println("-----------------------------");
    }

    public static void printLine2() {
        System.out.println("############################################");
    }
}

