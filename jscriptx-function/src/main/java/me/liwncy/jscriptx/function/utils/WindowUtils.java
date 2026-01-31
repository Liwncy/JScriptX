package me.liwncy.jscriptx.function.utils;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 窗口操作工具
 */
public class WindowUtils {

    /**
     * 获取所有窗口
     *
     * @return 窗口列表
     */
    public static List<WindowInfo> getAllWindows() {
        List<WindowInfo> windows = new ArrayList<>();

        User32.INSTANCE.EnumWindows((hWnd, lParam) -> {
            if (User32.INSTANCE.IsWindowVisible(hWnd)) {
                char[] title = new char[512];
                User32.INSTANCE.GetWindowText(hWnd, title, 512);
                String windowTitle = Native.toString(title);

                if (!windowTitle.isEmpty()) {
                    WinDef.RECT rect = new WinDef.RECT();
                    User32.INSTANCE.GetWindowRect(hWnd, rect);

                    WindowInfo windowInfo = new WindowInfo();
                    windowInfo.hWnd = hWnd;
                    windowInfo.title = windowTitle;
                    windowInfo.x = rect.left;
                    windowInfo.y = rect.top;
                    windowInfo.width = rect.right - rect.left;
                    windowInfo.height = rect.bottom - rect.top;

                    windows.add(windowInfo);
                }
            }
            return true;
        }, null);

        return windows;
    }

    /**
     * 根据标题查找窗口
     *
     * @param title 窗口标题
     * @return 窗口信息，未找到返回null
     */
    public static WindowInfo findWindowByTitle(String title) {
        for (WindowInfo window : getAllWindows()) {
            if (window.title.contains(title)) {
                return window;
            }
        }
        return null;
    }

    /**
     * 激活窗口
     *
     * @param hWnd 窗口句柄
     * @return 是否成功
     */
    public static boolean activateWindow(WinDef.HWND hWnd) {
        return User32.INSTANCE.SetForegroundWindow(hWnd);
    }

    /**
     * 根据标题激活窗口
     *
     * @param title 窗口标题
     * @return 是否成功
     */
    public static boolean activateWindow(String title) {
        WindowInfo window = findWindowByTitle(title);
        return window != null && activateWindow(window.hWnd);
    }

    /**
     * 获取窗口位置和大小
     *
     * @param hWnd 窗口句柄
     * @return 窗口信息
     */
    public static Rectangle getWindowRect(WinDef.HWND hWnd) {
        WinDef.RECT rect = new WinDef.RECT();
        User32.INSTANCE.GetWindowRect(hWnd, rect);
        return new Rectangle(rect.left, rect.top,
                rect.right - rect.left,
                rect.bottom - rect.top);
    }

    /**
     * 窗口信息类
     */
    public static class WindowInfo {
        public WinDef.HWND hWnd;
        public String title;
        public int x;
        public int y;
        public int width;
        public int height;

        @Override
        public String toString() {
            return title + " (" + x + "," + y + "," + width + "," + height + ")";
        }
    }
}