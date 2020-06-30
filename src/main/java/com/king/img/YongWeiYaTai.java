package com.king.img;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

public class YongWeiYaTai {
    public static void main(String[] args) {
        // 起点坐标
        int[] startXY = new int[]{490, 469};
        // 终点坐标
        int[] endXY = new int[]{713, 692};
        // 裁剪大小
        int qrW = endXY[0] - startXY[0];
        int qrH = endXY[1] - startXY[1];
        System.out.println(String.format("qrW=%s,qrH=%s", qrW, qrH));
        File dir = new File("/Users/kingtiger/Downloads/咏威亚太/亲子游戏人物卡/森林动奥会卡片");
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String fileName = file.getName();
            if (!fileName.endsWith("png")) {
                continue;
            }
            //  截取
            String target = file.getParentFile().getAbsolutePath() + "/" + fileName.substring(0, fileName.indexOf(".")) + "_qrcode.png";
            System.out.println(target);
            try {
                Thumbnails.of(file.getAbsolutePath())
                        .sourceRegion(startXY[0], startXY[1], qrW, qrH)
                        .size(qrW, qrH)
                        .outputFormat("png")
                        .keepAspectRatio(false)
                        .toFile(target);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
