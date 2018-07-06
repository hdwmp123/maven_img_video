package com.king.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gif4j.GifEncoder;
import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import com.gif4j.ImageUtils;

public class GifExe {
    public static void main(String[] args) throws IOException {
        String path = "C:/Users/chanp/Desktop/Img/小猫咪/";
        File inDir = new File(path + "src/");
        File[] listFiles = inDir.listFiles();
        for (File file : listFiles) {
            String path240 = file.getAbsolutePath().replace("src", "src240");
            ReduceImg.reduceImg(file.getAbsolutePath(), path240, 240, 240, null);
            //
            GifImage gifImage = new GifImage();
            File file240 = new File(path240);
            BufferedImage image = ImageUtils.toBufferedImage(ImageIO.read(file240));
            gifImage.addGifFrame(new GifFrame((image)));
            gifImage.setLoopNumber(0);

            String outPath = file240.getAbsolutePath().replace("src", "gif").replaceAll(".jpg", ".gif");
            File outFile = new File(outPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            outFile.createNewFile();
            GifEncoder.encode(gifImage, outFile);
        }

        for (File file : listFiles) {
            String path120 = file.getAbsolutePath().replace("src", "src120");
            ReduceImg.reduceImg(file.getAbsolutePath(), path120, 120, 120, null);
            File file120 = new File(path120);
            String outPath = file120.getAbsolutePath().replace("src", "png").replaceAll(".jpg", ".png");
            File outFile = new File(outPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            outFile.createNewFile();
            convert(path120, "png", outPath);
        }
        System.out.println("OK");
    }

    /**
     *
     * @param source
     *            源图片路径
     * @param formatName
     *            将要转换的图片格式
     * @param result
     *            目标图片路径
     */
    public static void convert(String source, String formatName, String result) {
        try {
            File f = new File(source);
            f.canRead();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
