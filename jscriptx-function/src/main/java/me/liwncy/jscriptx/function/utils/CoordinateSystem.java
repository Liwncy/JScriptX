package me.liwncy.jscriptx.function.utils;

import java.awt.*;

/**
 * 坐标系统工具，用于屏幕坐标和相对坐标转换
 */
public class CoordinateSystem {

    /**
     * 将相对坐标转换为屏幕坐标
     *
     * @param relativeX 相对X坐标 (0-1)
     * @param relativeY 相对Y坐标 (0-1)
     * @return 屏幕坐标
     */
    public static Point relativeToScreen(double relativeX, double relativeY) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (relativeX * screenSize.width);
        int y = (int) (relativeY * screenSize.height);
        return new Point(x, y);
    }

    /**
     * 将屏幕坐标转换为相对坐标
     *
     * @param screenX 屏幕X坐标
     * @param screenY 屏幕Y坐标
     * @return 相对坐标 (0-1)
     */
    public static Point screenToRelative(int screenX, int screenY) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double x = (double) screenX / screenSize.width;
        double y = (double) screenY / screenSize.height;
        return new Point((int) (x * 100), (int) (y * 100)); // 转换为百分比
    }

    /**
     * 将窗口相对坐标转换为屏幕坐标
     *
     * @param windowRect 窗口矩形
     * @param relativeX  相对X坐标 (0-1)
     * @param relativeY  相对Y坐标 (0-1)
     * @return 屏幕坐标
     */
    public static Point windowRelativeToScreen(Rectangle windowRect, double relativeX, double relativeY) {
        int x = (int) (windowRect.x + relativeX * windowRect.width);
        int y = (int) (windowRect.y + relativeY * windowRect.height);
        return new Point(x, y);
    }

    /**
     * 检查坐标是否在屏幕范围内
     *
     * @param x X坐标
     * @param y Y坐标
     * @return 是否在屏幕范围内
     */
    public static boolean isInScreenBounds(int x, int y) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return x >= 0 && x < screenSize.width && y >= 0 && y < screenSize.height;
    }

    /**
     * 获取屏幕中心坐标
     *
     * @return 屏幕中心坐标
     */
    public static Point getScreenCenter() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point(screenSize.width / 2, screenSize.height / 2);
    }
}