package me.liwncy.jscriptx.function.utils;

/**
 * 延迟工具类，用于在游戏脚本中添加延迟操作
 */
public class Delay {
    /**
     * 延迟指定的毫秒数
     * @param milliseconds 延迟的毫秒数
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 延迟指定的秒数
     * @param seconds 延迟的秒数
     */
    public static void sleepSeconds(double seconds) {
        sleep((long) (seconds * 1000));
    }
}