package com.king.img;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;

public class ImgDavaAndAvaForDouYin {

    public static void main(String[] args) {
        int size = 3;
        int h = 720;
        int w = 1280 / size;
        int x = 0;
        for (int i = 0; i < size; i++) {
            try {
                Thumbnails.of("/Users/kingtiger/Downloads/Me/儿童教育/DaveAndAva/temp/out.jpg")
                        .sourceRegion(x, 0, w, h)
                        .size(w, h)
                        .outputFormat("jpg")
                        .keepAspectRatio(false)
                        .toFile(String.format("/Users/kingtiger/Downloads/Me/儿童教育/DaveAndAva/temp/out_%s.jpg", i + 1));
                x += w;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
