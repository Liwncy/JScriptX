package me.liwncy.jscriptx.function.utils;

import lombok.Getter;

/**
 * 定时器工具类，用于游戏脚本中的定时任务
 */
public class Timer {
    private long lastTime;
    private long currentTime;
    /**
     * -- GETTER --
     *  获取经过的时间（毫秒）
     *
     * @return 经过的时间
     */
    @Getter
    private long elapsedTime;
    
    public Timer() {
        reset();
    }
    
    /**
     * 重置定时器
     */
    public void reset() {
        lastTime = System.currentTimeMillis();
        currentTime = lastTime;
        elapsedTime = 0;
    }
    
    /**
     * 更新定时器
     */
    public void update() {
        currentTime = System.currentTimeMillis();
        elapsedTime = currentTime - lastTime;
        lastTime = currentTime;
    }

    /**
     * 检查是否经过了指定的时间
     * @param milliseconds 指定的时间（毫秒）
     * @return 是否经过了指定的时间
     */
    public boolean hasElapsed(long milliseconds) {
        return getElapsedTime() >= milliseconds;
    }
}