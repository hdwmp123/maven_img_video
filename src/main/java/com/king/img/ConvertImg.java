package com.king.img;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ConvertImg {
    public static void main(String[] args) {
        if (args == null || args.length == 0 || args.length != 3) {
            System.out.println(String.format("请输入参数  source formatName result"));
            System.exit(0);
        }
        String sourcePath = args[0];
        String formatName = args[1];
        String resultPath = args[2];
        File sourceDir = new File(sourcePath);
        if(!sourceDir.exists() || !sourceDir.isDirectory()) {
            System.out.println("source 不存在");
            System.exit(0);
        }
        File[] listFiles = sourceDir.listFiles();
        for (File file : listFiles) {
            String oldFileName = file.getName();
            String newFileName = oldFileName.substring(0, oldFileName.indexOf(".") + 1) + formatName;
            convert(file.getAbsolutePath(), formatName, resultPath + "/" + newFileName);
        }
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
           System.out.println(String.format("转换成功%s-->%s", source, result));
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
