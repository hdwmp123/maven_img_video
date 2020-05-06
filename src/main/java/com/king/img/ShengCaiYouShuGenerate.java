package com.king.img;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 生财有术
 */
public class ShengCaiYouShuGenerate {
    public static void main(String[] args) throws IOException {
        //
        reSize();
        cutQrCode();
        showSize();
        coverQrCode();
    }

    /**
     * 重置大小
     */
    private static void reSize() throws IOException {
        String sourceDir = "/Users/kingtiger/Downloads/Me/生财日历";
        String targetDir = sourceDir + "_resize";
        File sourceDirF = new File(sourceDir);
        File[] monthDir = sourceDirF.listFiles();
        for (int i = 0; i < monthDir.length; i++) {
            File month = monthDir[i];
            if (!month.isDirectory()) {
                continue;
            }
            File[] images = month.listFiles();
            if (images == null || images.length == 0) {
                continue;
            }
            for (int j = 0; j < images.length; j++) {
                File image = images[j];
                boolean jpgEnd = image.getName().toLowerCase().endsWith("jpg");
                if (!image.isFile() || !jpgEnd) {
                    continue;
                }
                String src = image.getAbsolutePath();
                String target = String.format(targetDir + "/%s/%s", month.getName(), image.getName());
                File targetFile = new File(target);
                if (!targetFile.exists()) {
                    targetFile.getParentFile().mkdirs();
                    targetFile.createNewFile();
                }
                System.out.println("源文件：" + src);
                System.out.println("目标文件：" + target);
                Thumbnails.of(src).size(1080, 1777).keepAspectRatio(true).toFile(target);
                System.out.println("----------------------");
            }
        }
    }


    /**
     * QR裁剪并压缩
     */
    private static void cutQrCode() throws IOException {
        // 690 × 930
        String src = "/Users/kingtiger/Downloads/Me/生财日历/share.png";
        // 194 x 194
        String target = "/Users/kingtiger/Downloads/Me/生财日历/scys_qrcode.png";
        String targetPng = "/Users/kingtiger/Downloads/Me/生财日历/scys_qrcode.png";
        // 起点坐标
        int[] startXY = new int[]{375, 656};
        // 终点坐标
        int[] endXY = new int[]{528, 809};
        // 裁剪大小
        int qrW = endXY[0] - startXY[0];
        int qrH = endXY[1] - startXY[1];
        System.out.println(String.format("qrW=%s,qrH=%s", qrW, qrH));
        //  截取
        Thumbnails.of(src).sourceRegion(startXY[0], startXY[1], qrW, qrH).size(qrW, qrH).keepAspectRatio(false).toFile(target);
        // 压缩并转换
        Thumbnails.of(target).size(158, 158).outputFormat("png").toFile(targetPng);
    }

    /**
     * 展示原图二维码尺寸
     */
    private static void showSize() {
        // 起点坐标
        int[] startXY = new int[]{136, 1493};
        // 终点坐标
        int[] endXY = new int[]{292, 1649};
        // 裁剪大小
        int qrW = endXY[0] - startXY[0];
        int qrH = endXY[1] - startXY[1];
        // 136,136
        System.out.println(String.format("qrW=%s,qrH=%s", qrW, qrH));
    }

    /**
     * 覆盖二维码
     */
    private static void coverQrCode() throws IOException {
        String base = "/Users/kingtiger/Downloads/Me/生财日历";
        String sourceDir = base + "_resize";
        String targetDir = base + "_qrcode";
        String qrcodePath = base + "/scys_qrcode.png";
        File sourceDirF = new File(sourceDir);
        File[] monthDir = sourceDirF.listFiles();
        for (int i = 0; i < monthDir.length; i++) {
            File month = monthDir[i];
            File[] images = month.listFiles();
            if (images == null || images.length == 0) {
                continue;
            }
            for (int j = 0; j < images.length; j++) {
                File image = images[j];
                if (!image.getName().toLowerCase().endsWith("jpg")) {
                    continue;
                }
                String src = image.getAbsolutePath();
                String target = String.format(targetDir + "/%s/%s", month.getName(), image.getName());
                File targetFile = new File(target);
                if (!targetFile.exists()) {
                    targetFile.getParentFile().mkdirs();
                    targetFile.createNewFile();
                }
                System.out.println(src);
                System.out.println(target);
                Position position = new Position() {
                    public Point calculate(
                            int enclosingWidth,
                            int enclosingHeight,
                            int width,
                            int height,
                            int insetLeft,
                            int insetRight,
                            int insetTop,
                            int insetBottom
                    ) {
                        // 起点坐标
                        int y = 4;
                        int[] startXY = new int[]{136, 1493 - y};
                        return new Point(startXY[0], startXY[1]);
                    }
                };
                Thumbnails.of(src).size(1080, 1777).watermark(position, ImageIO.read(new File(qrcodePath)), 1f).toFile(target);
            }
        }
    }
}
