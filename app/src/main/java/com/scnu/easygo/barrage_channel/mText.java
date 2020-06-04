package com.scnu.easygo.barrage_channel;

import android.graphics.Paint;

public class mText {
    //文字
    private String text;
    //文字大小
    private float size;
    //文字颜色
    private Integer color;
    //文字X轴
    private float x;
    //文字Y轴
    private float y;
    //文字移动速度
    private int speed;
    //画笔
    private Paint paint;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }


}