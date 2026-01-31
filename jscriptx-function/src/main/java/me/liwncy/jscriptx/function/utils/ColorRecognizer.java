package me.liwncy.jscriptx.function.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 颜色识别工具
 */
public class ColorRecognizer {
    private Robot robot;
    private ScreenRecognizer screenRecognizer;

    public ColorRecognizer() {
        try {
            robot = new Robot();
            screenRecognizer = new ScreenRecognizer();
        } catch (AWTException e) {
            throw new RuntimeException("创建 Robot 实例失败", e);
        }
    }

    /**
     * 获取屏幕指定位置的颜色
     *
     * @param x X坐标
     * @param y Y坐标
     * @return 颜色
     */
    public Color getColorAt(int x, int y) {
        BufferedImage image = screenRecognizer.captureRegion(x, y, 1, 1);
        return new Color(image.getRGB(0, 0));
    }

    /**
     * 检查屏幕指定位置的颜色是否匹配
     *
     * @param x           X坐标
     * @param y           Y坐标
     * @param targetColor 目标颜色
     * @param tolerance   容差，0-255
     * @return 是否匹配
     */
    public boolean isColorMatch(int x, int y, Color targetColor, int tolerance) {
        Color actualColor = getColorAt(x, y);
        return getColorDistance(actualColor, targetColor) <= tolerance;
    }

    /**
     * 检查屏幕指定位置的颜色是否在指定范围内
     *
     * @param x        X坐标
     * @param y        Y坐标
     * @param minColor 最小颜色
     * @param maxColor 最大颜色
     * @return 是否在范围内
     */
    public boolean isColorInRange(int x, int y, Color minColor, Color maxColor) {
        Color actualColor = getColorAt(x, y);
        return actualColor.getRed() >= minColor.getRed() &&
                actualColor.getRed() <= maxColor.getRed() &&
                actualColor.getGreen() >= minColor.getGreen() &&
                actualColor.getGreen() <= maxColor.getGreen() &&
                actualColor.getBlue() >= minColor.getBlue() &&
                actualColor.getBlue() <= maxColor.getBlue();
    }

    /**
     * 查找屏幕中指定颜色的位置
     *
     * @param targetColor  目标颜色
     * @param tolerance    容差
     * @param regionX      搜索区域X坐标
     * @param regionY      搜索区域Y坐标
     * @param regionWidth  搜索区域宽度
     * @param regionHeight 搜索区域高度
     * @return 颜色位置列表
     */
    public java.util.List<Point> findColor(Color targetColor, int tolerance,
                                           int regionX, int regionY,
                                           int regionWidth, int regionHeight) {
        java.util.List<Point> positions = new java.util.ArrayList<>();
        BufferedImage image = screenRecognizer.captureRegion(regionX, regionY, regionWidth, regionHeight);

        for (int y = 0; y < regionHeight; y++) {
            for (int x = 0; x < regionWidth; x++) {
                Color color = new Color(image.getRGB(x, y));
                if (getColorDistance(color, targetColor) <= tolerance) {
                    positions.add(new Point(regionX + x, regionY + y));
                }
            }
        }

        return positions;
    }

    /**
     * 计算两个颜色之间的距离
     *
     * @param color1 颜色1
     * @param color2 颜色2
     * @return 颜色距离
     */
    private int getColorDistance(Color color1, Color color2) {
        int rDiff = color1.getRed() - color2.getRed();
        int gDiff = color1.getGreen() - color2.getGreen();
        int bDiff = color1.getBlue() - color2.getBlue();
        return (int) Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }
}