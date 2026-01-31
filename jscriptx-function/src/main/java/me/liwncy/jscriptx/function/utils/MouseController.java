package me.liwncy.jscriptx.function.utils;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * 鼠标控制器，用于模拟鼠标操作
 */
public class MouseController {
    private Robot robot;

    public MouseController() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("创建 Robot 实例失败", e);
        }
    }

    /**
     * 移动鼠标到指定位置
     *
     * @param x X坐标
     * @param y Y坐标
     */
    public void move(int x, int y) {
        robot.mouseMove(x, y);
    }

    /**
     * 相对移动鼠标
     *
     * @param dx X方向偏移量
     * @param dy Y方向偏移量
     */
    public void moveRelative(int dx, int dy) {
        Point current = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(current.x + dx, current.y + dy);
    }

    /**
     * 左键点击
     */
    public void leftClick() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    /**
     * 右键点击
     */
    public void rightClick() {
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    /**
     * 中键点击
     */
    public void middleClick() {
        robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
    }

    /**
     * 双击左键
     */
    public void doubleClick() {
        leftClick();
        leftClick();
    }

    /**
     * 按下左键
     */
    public void pressLeft() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    }

    /**
     * 释放左键
     */
    public void releaseLeft() {
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    /**
     * 按下右键
     */
    public void pressRight() {
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
    }

    /**
     * 释放右键
     */
    public void releaseRight() {
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    /**
     * 滚动鼠标滚轮
     *
     * @param amount 滚动量，正数向上，负数向下
     */
    public void scroll(int amount) {
        robot.mouseWheel(amount);
    }

    /**
     * 拖拽操作
     *
     * @param fromX 起始X坐标
     * @param fromY 起始Y坐标
     * @param toX   结束X坐标
     * @param toY   结束Y坐标
     */
    public void drag(int fromX, int fromY, int toX, int toY) {
        move(fromX, fromY);
        pressLeft();
        move(toX, toY);
        releaseLeft();
    }

    /**
     * 获取当前鼠标位置
     *
     * @return 鼠标位置
     */
    public Point getCurrentPosition() {
        return MouseInfo.getPointerInfo().getLocation();
    }
}