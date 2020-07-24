package com.king.video;

/**
 * @author kingtiger
 */

public enum DaveAndAvaForType {
    DouYin("mp4douyin", 51, 60),
    ShiPinHao("mp4shipinhao", 1, 1);

    DaveAndAvaForType(String name, int start, int end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    private String name;
    private int start;
    private int end;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
