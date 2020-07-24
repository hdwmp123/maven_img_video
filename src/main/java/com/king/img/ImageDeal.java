package com.king.img;


import com.king.video.DaveAndAva;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * 图像处理类.
 *
 * @author nagsh
 */
public class ImageDeal {

    /**
     * 原始图片打开路径
     */
    String saveUrl;
    /**
     * 新图保存路径
     */
    String saveName;
    /**
     * 新图名称
     */
    String suffix;
    /**
     * 新图类型 只支持gif,jpg,png
     */
    String openUrl;

    public ImageDeal(String openUrl, String saveUrl, String saveName,
                     String suffix) {
        this.openUrl = openUrl;
        this.saveName = saveName;
        this.saveUrl = saveUrl;
        this.suffix = suffix;
    }

    /**
     * 图片缩放.
     *
     * @param width  需要的宽度
     * @param height 需要的高度
     * @throws Exception
     */
    public void zoom(int width, int height) throws Exception {
        double sx = 0.0;
        double sy = 0.0;

        File file = new File(openUrl);
        if (!file.isFile()) {
            throw new Exception("ImageDeal>>>" + file + " 不是一个图片文件!");
        }
        BufferedImage bi = ImageIO.read(file);
        // 读取该图片

        // 计算x轴y轴缩放比例--如需等比例缩放，在调用之前确保参数width和height是等比例变化的
        sx = (double) width / bi.getWidth();
        sy = (double) height / bi.getHeight();

        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(sx, sy), null);
        File sf = new File(saveUrl, saveName + "." + suffix);
        Image zoomImage = op.filter(bi, null);
        try {
            ImageIO.write((BufferedImage) zoomImage, suffix, sf);
            // 保存图片
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 旋转
     *
     * @param degree 旋转角度
     * @throws Exception
     */
    public void spin(int degree) throws Exception {
        // 旋转后的宽度
        int swidth = 0;
        // 旋转后的高度
        int sheight = 0;
        // 原点横坐标
        int x;
        // 原点纵坐标
        int y;
        File file = new File(openUrl);
        if (!file.isFile()) {
            throw new Exception("ImageDeal>>>" + file + " 不是一个图片文件!");
        }
        // 读取该图片
        BufferedImage bi = ImageIO.read(file);
        // 处理角度--确定旋转弧度
        degree = degree % 360;
        if (degree < 0) {
            // 将角度转换到0-360度之间
            degree = 360 + degree;
        }
        // 将角度转为弧度
        double theta = Math.toRadians(degree);


        // 确定旋转后的宽和高
        if (degree == 180 || degree == 0 || degree == 360) {
            swidth = bi.getWidth();
            sheight = bi.getHeight();
        } else if (degree == 90 || degree == 270) {
            sheight = bi.getWidth();
            swidth = bi.getHeight();
        } else {
            swidth = (int) (Math.sqrt(bi.getWidth() * bi.getWidth()
                    + bi.getHeight() * bi.getHeight()));
            sheight = (int) (Math.sqrt(bi.getWidth() * bi.getWidth()
                    + bi.getHeight() * bi.getHeight()));
        }

        // 确定原点坐标
        x = (swidth / 2) - (bi.getWidth() / 2);
        y = (sheight / 2) - (bi.getHeight() / 2);

        BufferedImage spinImage = new BufferedImage(swidth, sheight,
                bi.getType());

        // 设置图片背景颜色
        Graphics2D gs = (Graphics2D) spinImage.getGraphics();
        gs.setColor(Color.white);

        // 以给定颜色绘制旋转后图片的背景
        gs.fillRect(0, 0, swidth, sheight);

        AffineTransform at = new AffineTransform();
        // 旋转图象
        at.rotate(theta, swidth / 2, sheight / 2);
        at.translate(x, y);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        spinImage = op.filter(bi, spinImage);
        File sf = new File(saveUrl, saveName + "." + suffix);
        // 保存图片
        ImageIO.write(spinImage, suffix, sf);

    }

    /**
     * 马赛克化.
     *
     * @param size 马赛克尺寸，即每个矩形的长宽
     * @return
     * @throws Exception
     */
    public boolean mosaic(int size) throws Exception {
        File file = new File(openUrl);
        if (!file.isFile()) {
            throw new Exception("ImageDeal>>>" + file + " 不是一个图片文件!");
        }
        BufferedImage bi = ImageIO.read(file);
        // 读取该图片
        int imgWidth = bi.getWidth();
        int imgHeight = bi.getHeight();
        BufferedImage spinImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        // 马赛克格尺寸太大或太小
        if (imgWidth < size || imgHeight < size || size <= 0) {
            return false;
        }
        // 方向绘制个数
        int xcount = 0;
        // y方向绘制个数
        int ycount = 0;
        if (imgWidth % size == 0) {
            xcount = imgWidth / size;
        } else {
            xcount = imgWidth / size + 1;
        }
        if (imgHeight % size == 0) {
            ycount = imgHeight / size;
        } else {
            ycount = imgHeight / size + 1;
        }
        //坐标
        int x = 0;
        int y = 0;

        // 绘制马赛克(绘制矩形并填充颜色)
        Graphics gs = spinImage.getGraphics();
        for (int i = 0; i < xcount; i++) {
            for (int j = 0; j < ycount; j++) {

                //马赛克矩形格大小
                int mwidth = size;
                int mheight = size;
                //横向最后一个比较特殊，可能不够一个size
                if (i == xcount - 1) {
                    mwidth = imgWidth - x;
                }
                //同理
                if (j == ycount - 1) {
                    mheight = imgHeight - y;
                }

                // 矩形颜色取中心像素点RGB值
                int centerX = x;
                int centerY = y;
                if (mwidth % 2 == 0) {
                    centerX += mwidth / 2;
                } else {
                    centerX += (mwidth - 1) / 2;
                }
                if (mheight % 2 == 0) {
                    centerY += mheight / 2;
                } else {
                    centerY += (mheight - 1) / 2;
                }
                Color color = new Color(bi.getRGB(centerX, centerY));
                gs.setColor(color);
                gs.fillRect(x, y, mwidth, mheight);
                // 计算下一个矩形的y坐标
                y = y + size;
            }
            // 还原y坐标
            y = 0;
            // 计算x坐标
            x = x + size;
        }
        gs.dispose();
        File sf = new File(saveUrl, saveName + "." + suffix);
        ImageIO.write(spinImage, suffix, sf);
        // 保存图片
        return true;
    }

    public static void main(String[] args) throws Exception {
        String dir = "/Users/kingtiger/Downloads/Img/masaike/yingmu/";
        String openUrl = dir + "S0000.jpg";
        int[] wh = DaveAndAva.getWH(openUrl);
        System.out.println(wh[0] + "," + wh[1]);
        int size = wh[1];
        for (int i = 0; i < size; i++) {
            String index = getIndex(size - i);
            String saveName = String.format("S%s", index);
            ImageDeal imageDeal = new ImageDeal(openUrl, dir, saveName, "jpg");
            // 测试缩放
            //imageDeal.zoom(200, 300);
            // 测试旋转
            //imageDeal.spin(90);
            // 测试马赛克
            int mW = (i + 1) * 1;
            imageDeal.mosaic(mW);
            int count = size / mW;
            System.out.println(mW + "," + count);
            if (count <= 32) {
                break;
            }
        }
    }

    public static String getIndex(int index) {
        if (index < 10) {
            return "000" + index;
        }
        if (index >= 10 && index < 100) {
            return "00" + index;
        }
        if (index >= 100 && index < 1000) {
            return "0" + index;
        }
        return index + "";
    }

}

