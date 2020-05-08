package com.king.img;

import com.gif4j.GifEncoder;
import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.ImageUtils;
import com.king.util.FileUtil;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 表情包文件生成工具
 * 规格
 * 详情页横幅：JPEG 或PNG 格式，W 750 ×H 400 像素，不超过80KB
 */
public class BiaoQingBaoGenerate {
    public static void main(String[] args) {
        biaoQingBao();
    }

    /**
     * 表情包裁剪
     */
    private static void biaoQingBao() {
        //convertTitle("/Users/kingtiger/Downloads/Me");
        String source = "/Users/kingtiger/Downloads/Me/诺基亚表情包/source";
        File dir = new File(source);
        File[] files = dir.listFiles();
        int index = 1;
        int step = 1;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkFile(file, "jpg")) {
                continue;
            }
            String dirPath = String.format("/Users/kingtiger/Downloads/Me/诺基亚表情包/%s/", step);
            String destPath = dirPath + "source/" + file.getName();
            try {
                FileUtil.copyFile(file, new File(destPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (index == 16) {
                rename(dirPath);
                convertToSquare(dirPath);
                convertAll(dirPath);
                //
                index = 1;
                step++;
            } else {
                index++;
            }
        }
    }

    private static void rename(String dirPath) {
        File inDir = new File(dirPath + "source/");
        File[] listFiles = inDir.listFiles();
        int nameIndex = 0;
        for (int i = 0; i < listFiles.length; i++) {
            File file = listFiles[i];
            if (checkFile(file, "jpg")) {
                continue;
            }
            String newName = String.format(file.getParentFile().getAbsolutePath() + "/%s.jpg", getName(nameIndex));
            System.out.println(file.getAbsolutePath());
            System.out.println(newName);
            System.out.println("#");
            file.renameTo(new File(newName));
            nameIndex++;
        }
    }

    /**
     * 裁剪标题图片
     *
     * @param dirPath
     */
    private static void convertTitle(
            String dirPath
    ) {
        //详情页横幅：JPEG 或PNG 格式，W 750 ×H 400 像素，不超过80KB
        String titleInPath = dirPath + "A.jpg";
        String titleOutPath = dirPath + "A_title.jpg";
        // 裁剪压缩
        autoCut(
                titleInPath,
                titleOutPath,
                750,
                400
        );
        // 赞赏引导 GIF 或PNG 格式，W 750 ×H 560 像素，不超过100KB
        titleInPath = dirPath + "B.jpg";
        titleOutPath = dirPath + "B_title.png";
        // 裁剪压缩
        autoCut(
                titleInPath,
                titleOutPath,
                750,
                560
        );
        // 赞赏感谢 GIF 或PNG 格式，W 750 ×H 750 像素，不超过200KB
        titleInPath = dirPath + "C.jpg";
        titleOutPath = dirPath + "C_title.png";
        // 裁剪压缩
        autoCut(
                titleInPath,
                titleOutPath,
                750,
                750
        );
        // 裁剪
    }

    /**
     * @param dirPath
     * @throws IOException
     */
    private static void convertAll(
            String dirPath
    ) {

        //----------------------------------------------
        File inDir = new File(dirPath + "src/");
        File[] listFiles = inDir.listFiles();
        for (File file : listFiles) {
            if (checkFile(file, "jpg")) {
                continue;
            }
            String absolutePath = file.getAbsolutePath();
            // JPG 尺寸
            int[] sizeList = new int[]{
                    640,
                    240,
            };
            String outPath;
            //压缩到
            for (int i = 0; i < sizeList.length; i++) {
                int size = sizeList[i];
                outPath = absolutePath.replace("src", "src" + size);
                createFile(outPath);
                try {
                    Thumbnails.of(absolutePath).size(size, size).outputFormat("jpg").toFile(outPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // PNG尺寸
            sizeList = new int[]{
                    240,
                    120,
                    50,
            };
            // 转换
            for (int i = 0; i < sizeList.length; i++) {
                int size = sizeList[i];
                outPath = absolutePath.replace("src", "png" + size).replaceAll(".jpg", ".png");
                createFile(outPath);
                try {
                    Thumbnails.of(absolutePath).size(size, size).outputFormat("png").toFile(outPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 转换
        inDir = new File(dirPath + "src240/");
        listFiles = inDir.listFiles();
        for (File file : listFiles) {
            if (checkFile(file, "jpg")) {
                continue;
            }
            //转换为240X240 gif
            GifImage gifImage = new GifImage();
            BufferedImage image = null;
            try {
                image = ImageUtils.toBufferedImage(ImageIO.read(file));
                gifImage.addGifFrame(new GifFrame((image)));
                gifImage.setLoopNumber(0);
                String absolutePath = file.getAbsolutePath();
                String outPath = absolutePath.replace("src", "gif").replaceAll(".jpg", ".gif");
                File outFile = createFile(outPath);
                GifEncoder.encode(gifImage, outFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("OK");
    }

    /**
     * 检测并创建文件
     *
     * @param outPath 文件路径
     * @return
     * @throws IOException
     */
    private static File createFile(
            String outPath
    ) {
        File outFile = new File(outPath);
        File parentFile = outFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try {
            outFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outFile;
    }

    /**
     * 检测文件是否合规
     *
     * @param file 文件
     * @param end  文件后缀
     * @return
     */
    public static boolean checkFile(
            File file,
            String end
    ) {
        boolean directory = file.isDirectory();
        if (directory) {
            return true;
        }
        boolean endsWith = file.getName().endsWith(end);
        if (!endsWith) {
            return true;
        }
        return false;
    }


    /**
     * 裁剪成正方形
     */
    public static void convertToSquare(
            String dirPath
    ) {
        File dirFile = new File(dirPath + "/source");
        if (!dirFile.exists()) {
            System.out.println("文件路径不存在");
            return;
        }
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkFile(file, "jpg")) {
                continue;
            }
            String outUrl = file.getAbsolutePath().replace("source", "src");
            createFile(outUrl);
            try {
                // TODO:这里数值根据自己情况修改
                Thumbnails.of(file).sourceRegion(150, 159, 855, 855).scale(1).toFile(outUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("OK");
    }

    /**
     * 长图片均匀分隔
     *
     * @throws IOException
     */
    private static void bigToSmall(
            String filePath
    ) throws IOException {
        int[] imgWidthHeight = getImgWidthHeight(filePath);
        int width = imgWidthHeight[0];
        int height = imgWidthHeight[1];
        int index = 1;
        for (int i = 0; i < height; i += width * 1) {
            String out = String.format("D:/Img/out/%s.png", index);
            Thumbnails.of(filePath).sourceRegion(0, i, width, width * 1).toFile(out);
            index++;
        }
    }

    /**
     * 自动比例裁剪
     *
     * @throws IOException
     */
    /**
     * @param inFilePath  输入文件
     * @param outFilePath 输出文件
     * @param widthParam  比例宽度
     * @param heightParam 比例高度
     */
    private static void autoCut(
            String inFilePath,
            String outFilePath,
            int widthParam,
            int heightParam
    ) {
        //
        int[] imgWidthHeight = getImgWidthHeight(inFilePath);
        //标准宽高比
        double w_h_scale = divide(widthParam, heightParam);
        int relWidth = imgWidthHeight[0];
        int relHeight = imgWidthHeight[1];
        //实际宽高比
        double real_scale = divide(relWidth, relHeight);
        int width, height, x, y;
        //胖
        if (real_scale > w_h_scale) {
            //高度为标准，宽度裁剪
            width = (int) (relHeight * w_h_scale);
            height = relHeight;
            x = (relWidth - width) / 2;
            y = 0;
        } else { // 瘦
            // 宽度为标准，高度裁剪
            width = relWidth;
            height = (int) (relWidth / w_h_scale);
            x = 0;
            y = (relHeight - height) / 2;
        }
        try {
            String format = outFilePath.substring(outFilePath.indexOf(".") + 1);
            Thumbnails.of(inFilePath).sourceRegion(x, y, width, height).outputFormat(format).size(widthParam, heightParam).toFile(outFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("OK");
    }

    /**
     * a/b
     *
     * @param a
     * @param b
     * @return
     */
    private static double divide(int a, int b) {
        return new BigDecimal(a).divide(new BigDecimal(b), 10, RoundingMode.UP).doubleValue();
    }

    /**
     * 获取图片宽度和高度
     *
     * @param filePath 图片路径
     * @return 返回图片的宽度
     */
    public static int[] getImgWidthHeight(
            String filePath
    ) {
        File file = new File(filePath);
        int result[] = {0, 0};
        try {
            // 获得文件输入流
            InputStream inputStream = new FileInputStream(file);
            // 从流里将图片写入缓冲图片区
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            // 得到源图片宽
            result[0] = bufferedImage.getWidth(null);
            // 得到源图片高
            result[1] = bufferedImage.getHeight(null);
            inputStream.close();  //关闭输入流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getName(int nameIndex) {
        nameIndex++;
        return nameIndex < 10 ? "0" + nameIndex : nameIndex + "";
    }
}
