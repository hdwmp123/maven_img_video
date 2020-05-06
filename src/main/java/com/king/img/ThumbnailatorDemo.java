package com.king.img;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 图片工具Demo
 */
public class ThumbnailatorDemo {


    /**
     * Demo
     *
     * @throws IOException
     */
    private static void demo() throws IOException {
        //原图像素 1024*682
        //缩放
        String path = "/Users/kingtiger/Develop/Me/Img";
        Thumbnails.of(path + "/image/blue.jpg").size(100, 100).toFile(path + "/thumb/thumb_blue.jpg");
        Thumbnails.of(path + "/image/blue.jpg").size(2560, 2048).toFile(path + "/thumb/thumb_large_blue.jpg");

        //按照比例缩放
        Thumbnails.of(path + "/image/blue.jpg").scale(0.7).toFile(path + "/thumb/thumb_0.7_blue.jpg");
        Thumbnails.of(path + "/image/blue.jpg").scale(1.2).toFile(path + "/thumb/thumb_1.2_blue.jpg");

        //keepAspectRatio(false) 默认是按照比例缩放的
        Thumbnails.of(path + "/image/blue.jpg").size(200, 200).keepAspectRatio(false).toFile(path + "/thumb/thumb_noscale_blue.jpg");

        //旋转
        Thumbnails.of(path + "/image/blue.jpg").size(1024, 682).rotate(90).toFile(path + "/thumb/thumb_rotate90_blue.jpg");
        Thumbnails.of(path + "/image/blue.jpg").size(1024, 682).rotate(-90).toFile(path + "/thumb/thumb_rotate-90_blue.jpg");

        //水印
        Thumbnails.of(path + "/image/blue.jpg").size(1024, 682).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(path + "/image/baidu.png")), 0.5f).toFile(path + "/thumb/thumb_watermarkbr_blue.jpg");
        Thumbnails.of(path + "/image/blue.jpg").size(1024, 682).watermark(Positions.CENTER, ImageIO.read(new File(path + "/image/baidu.png")), 0.8f).toFile(path + "/thumb/thumb_watermarkc_blue.jpg");

        //裁剪
        Thumbnails.of(path + "/image/blue.jpg").sourceRegion(Positions.CENTER, 400, 400).size(200, 200).keepAspectRatio(false).toFile(path + "/thumb/thumb_regioncenter_blue.jpg");
        Thumbnails.of(path + "/image/blue.jpg").sourceRegion(Positions.BOTTOM_RIGHT, 400, 400).size(200, 200).keepAspectRatio(false).toFile(path + "/thumb/thumb_regionbr_blue.jpg");
        Thumbnails.of(path + "/image/blue.jpg").sourceRegion(650, 500, 400, 400).size(200, 200).keepAspectRatio(false).toFile(path + "/thumb/thumb_realregion_blue.jpg");

        //转换图像格式
        Thumbnails.of(path + "/image/blue.jpg").size(1024, 682).outputFormat("png").toFile(path + "/thumb/thumb_blue.png");
        Thumbnails.of(path + "/image/blue.jpg").size(1024, 682).outputFormat("gif").toFile(path + "/thumb/thumb_blue.gif");
        Thumbnails.of(path + "/image/blue.jpg").size(1024, 682).outputFormat("JPEG").toFile(path + "/thumb/thumb_blue.jpeg");

        //输出到outputstream中
        OutputStream outputStream = new FileOutputStream(new File(path + "/thumb/thumb_output_blue.gif"));
        Thumbnails.of(path + "/image/blue.jpg").size(1024, 682).toOutputStream(outputStream);

        //asBufferedImage() 返回BufferedImage
        BufferedImage thumbnail = Thumbnails.of(path + "/image/blue.jpg").size(1024, 682).asBufferedImage();
        ImageIO.write(thumbnail, "jpg", new File(path + "/thumb/thumb_bufferedImage_output_blue.gif"));
    }
}
