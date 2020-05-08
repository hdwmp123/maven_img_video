package com.king.png;

import java.awt.Color;
import java.awt.image.*;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
/**
 * 
 * ClassName: PngExe <br/> 
 * Reason: png特定颜色转换. <br/> 
 * date: 2019年1月22日 下午3:37:29 <br/> 
 * 
 * @author KingTiger 
 * @version  
 * @since JDK 1.8
 * @see https://ask.csdn.net/questions/674484?sort=id
 */
public class PngExe extends RGBImageFilter {
    public static void main(String[] args) {
        File dir = new File("D:\\Img\\cond-icon-heweather");
        File[] fileList = dir.listFiles();
        for (File file : fileList) {
            String fileName = file.getName();
            System.out.println(String.format("处理图片:%s",fileName));
            if(!fileName.toLowerCase().contains(".png")) {
                continue;
            }
            try {
                BufferedImage imageBiao = ImageIO.read(new FileInputStream(file));
                //白色
                ImageFilter imgf = new PngExe(new Color(71,89,120).getRGB(),Color.WHITE.getRGB());
                FilteredImageSource fis = new FilteredImageSource(imageBiao.getSource(), imgf);
                Image im = Toolkit.getDefaultToolkit().createImage(fis);
                im.flush();
                BufferedImage newImage = new BufferedImage(imageBiao.getWidth(), imageBiao.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = newImage.createGraphics();
                g.drawImage(im, 0, 0, null);
                g.dispose();
                newImage.flush();
                ImageIO.write(newImage, "png", new File(String.format("D:\\Img\\new_png\\%s", fileName)));
                // 把以上原图和加上图标后的图像
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("OK");
    }
    /**
     * 继承它实现图象ARGB的处理
     */

    int sourceRgb = 0;
    int targetRgb = 0;

    /**
     * 构造器，用来接收需要过滤图象的尺寸，以及透明度
     * @param sourceRgb
     * @param targetRgb
     */
    public PngExe(int sourceRgb,int targetRgb) {

        this.canFilterIndexColorModel = true;
        this.sourceRgb = sourceRgb;
        this.targetRgb = targetRgb;
    }
    DirectColorModel dcm = (DirectColorModel) ColorModel.getRGBdefault();

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int alp = dcm.getAlpha(rgb);
        if (alp != 0) {
            return targetRgb;
        }
        if (dcm.getRGB(rgb) == dcm.getRGB(this.sourceRgb)) {
            System.out.println("same");
            return targetRgb;
        }
        // 进行标准ARGB输出以实现图象过滤
        return alp << 24 | dcm.getRGB(rgb);
    }
}
