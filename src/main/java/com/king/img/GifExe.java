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
      //----------------------------------------------
        //压缩到640X640
        File inDir = new File(path + "src/");
        File[] listFiles = inDir.listFiles();
        for (File file : listFiles) {
            String outPath = file.getAbsolutePath().replace("src", "src640");
            ReduceImg.reduceImg(file.getAbsolutePath(), outPath, 640, 640, null);
        }
        //----------------------------------------------
        //压缩到240X240
        for (File file : listFiles) {
            String outPath = file.getAbsolutePath().replace("src", "src240");
            ReduceImg.reduceImg(file.getAbsolutePath(), outPath, 240, 240, null);
        }
        //转换为240X240 gif
        inDir = new File(path + "src240/");
        listFiles = inDir.listFiles();
        for (File file : listFiles) {
            //gif240
            GifImage gifImage = new GifImage();
            BufferedImage image = ImageUtils.toBufferedImage(ImageIO.read(file));
            gifImage.addGifFrame(new GifFrame((image)));
            gifImage.setLoopNumber(0);

            String outPath = file.getAbsolutePath().replace("src", "gif").replaceAll(".jpg", ".gif");
            File outFile = new File(outPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            outFile.createNewFile();
            GifEncoder.encode(gifImage, outFile);
            
            
        }
        //转换为240X240 png
        for (File file : listFiles) {
            //png240
            String outPath = file.getAbsolutePath().replace("src", "png").replaceAll(".jpg", ".png");
            File outFile = new File(outPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            outFile.createNewFile();
            convert(file.getAbsolutePath(), "png", outPath);
        }
        //----------------------------------------------
        //压缩120X120
        inDir = new File(path + "src/");
        listFiles = inDir.listFiles();
        for (File file : listFiles) {
            String outPath = file.getAbsolutePath().replace("src", "src120");
            ReduceImg.reduceImg(file.getAbsolutePath(), outPath, 120, 120, null);
        }
        //转换为120X120png
        inDir = new File(path + "src240/");
        listFiles = inDir.listFiles();
        for (File file : listFiles) {
            String outPath = file.getAbsolutePath().replace("src", "png").replaceAll(".jpg", ".png");
            File outFile = new File(outPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            outFile.createNewFile();
            convert(file.getAbsolutePath(), "png", outPath);
        }
        //----------------------------------------------
        //压缩为50X50
        inDir = new File(path + "src/");
        listFiles = inDir.listFiles();
        for (File file : listFiles) {
            String outPath = file.getAbsolutePath().replace("src", "src50");
            ReduceImg.reduceImg(file.getAbsolutePath(), outPath, 50, 50, null);
            
        }
        //转换
        inDir = new File(path + "src50/");
        listFiles = inDir.listFiles();
        for (File file : listFiles) {
            String outPath = file.getAbsolutePath().replace("src", "png").replaceAll(".jpg", ".png");
            File outFile = new File(outPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            outFile.createNewFile();
            convert(file.getAbsolutePath(), "png", outPath);
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
