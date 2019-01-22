package com.king.img;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageUtils {
    /**
     * 
     * cutJPG:(这里用一句话描述这个方法的作用). <br/>
     * 
     * @author KingTgier
     * @param input
     *            源文件
     * @param out
     *            目标文件
     * @param x
     *            起始坐标X
     * @param y
     *            起始坐标Y
     * @param width
     *            目标图片宽
     * @param height
     *            目标图片高
     * @throws IOException
     * @since JDK 1.8
     */
    public static void cutJPG(InputStream input, OutputStream out, int x, int y, int width, int height)
            throws IOException {
        ImageInputStream imageStream = null;
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpg");
            ImageReader reader = readers.next();
            imageStream = ImageIO.createImageInputStream(input);
            reader.setInput(imageStream, true);
            ImageReadParam param = reader.getDefaultReadParam();

            System.out.println(reader.getWidth(0));
            System.out.println(reader.getHeight(0));
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, "jpg", out);
        } finally {
            imageStream.close();
        }
    }

    public static void cutPNG(InputStream input, OutputStream out, int x, int y, int width, int height)
            throws IOException {
        ImageInputStream imageStream = null;
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("png");
            ImageReader reader = readers.next();
            imageStream = ImageIO.createImageInputStream(input);
            reader.setInput(imageStream, true);
            ImageReadParam param = reader.getDefaultReadParam();

            System.out.println(reader.getWidth(0));
            System.out.println(reader.getHeight(0));

            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, "png", out);
        } finally {
            imageStream.close();
        }
    }

    public static void cutImage(InputStream input, OutputStream out, String type, int x, int y, int width, int height)
            throws IOException {
        ImageInputStream imageStream = null;
        try {
            String imageType = (null == type || "".equals(type)) ? "jpg" : type;
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imageType);
            ImageReader reader = readers.next();
            imageStream = ImageIO.createImageInputStream(input);
            reader.setInput(imageStream, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, imageType, out);
        } finally {
            imageStream.close();
        }
    }

    public static void main(String[] args) throws Exception {
//        String src = "D:/Img/in/001.png";
//        int[] imgWidthHeight = ReduceImg.getImgWidthHeight(new File(src));
//        int width = imgWidthHeight[0];
//        int height = imgWidthHeight[1];
//        int index = 1;
//        for (int i = 0; i < height; i += width * 1) {
//            ImageUtils.cutPNG(new FileInputStream(src), new FileOutputStream(String.format("D:/Img/out/%s.png", index)), 0,
//                    i, width, width * 1);
//            index++;
//        }
        String src = "C:\\Users\\chanp\\Desktop\\Img\\小程序\\2.jpg";
        File file = new File(src);
        int[] imgWidthHeight = ReduceImg.getImgWidthHeight(file);
        double w_h_scale = divide(1125,2436);//标准宽高比
        int relWidth = imgWidthHeight[0];
        int relHeight = imgWidthHeight[1];
        double real_scale = divide(relWidth,relHeight);//实际宽高比
        String absolutePath = file.getParentFile().getAbsolutePath();
        String out = String.format("%s/out/%s", absolutePath,file.getName());
        if (real_scale > w_h_scale) {//胖
            //高度为标准，宽度裁剪
            int width = (int) (relHeight * w_h_scale);
            int height = relHeight;
            int x = (relWidth-width) / 2;
            int y = 0;
            System.out.println(String.format("x:%s,y:%s,width:%s,height:%s", x,y,width,height));
            ImageUtils.cutJPG(new FileInputStream(src), new FileOutputStream(out), x, y, width, height);
        } else { // 瘦
            // 宽度为标准，高度裁剪
            int width = relWidth;
            int height = (int) (relWidth / w_h_scale);
            int x = 0;
            int y = (relHeight-height) / 2;
            System.out.println(String.format("x:%s,y:%s,width:%s,height:%s", x,y,width,height));
            ImageUtils.cutJPG(new FileInputStream(src), new FileOutputStream(out), x, y, width, height);
        }
        System.out.println("OK");
    }
    
    private static double divide(int a,int b) {
        return new BigDecimal(a).divide(new BigDecimal(b),10,RoundingMode.UP).doubleValue();
    }
}
