package me.liwncy.jscriptx.plugin.autodialog;

import cn.hutool.core.util.StrUtil;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.core.manager.event.*;
import me.liwncy.jscriptx.core.manager.plugin.Plugin;
import me.liwncy.jscriptx.function.utils.KeyboardController;

import java.awt.event.KeyEvent;


/**
 * 自动对话监听命令
 * 按0键启动/停止，自动按F键
 *
 * @author 插件监听
 */
@Slf4j(topic = "AutoDialogListener")
public class AutoDialogListener extends EventListener<KeyboardEvent, KeyboardEvent.KeyData> {
    private boolean isRunning = false;
    private Thread autoPressThread;
    private final KeyboardController keyboardController;

    public AutoDialogListener(Plugin plugin) {
        super(plugin);
        this.keyboardController = new KeyboardController();
    }

    @Override
    public boolean support(@NonNull KeyboardEvent event, KeyboardEvent.KeyData source) {
        // 按键=触发插件
        if (StrUtil.equals("等号", source.getKeyText())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onEvent(@NonNull KeyboardEvent event, KeyboardEvent.KeyData source) {
        // 切换运行状态
        isRunning = !isRunning;

        if (isRunning) {
            // 启动自动按F键的线程
            startAutoPressThread();
            log.info("自动对话已启动，持续按F键");
        } else {
            // 停止自动按F键的线程
            stopAutoPressThread();
            log.info("自动对话已停止");
        }

        return false;
    }

    /**
     * 启动自动按F键的线程
     */
    private void startAutoPressThread() {
        if (autoPressThread != null && autoPressThread.isAlive()) {
            return;
        }

        autoPressThread = new Thread(() -> {
            while (isRunning) {
                // 模拟按F键
                keyboardController.press(KeyEvent.VK_F);

                // 等待一段时间，避免按键过快
                try {
                    Thread.sleep(500); // 500毫秒按一次F键
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        autoPressThread.setDaemon(true);
        autoPressThread.start();
    }

    /**
     * 停止自动按F键的线程
     */
    private void stopAutoPressThread() {
        isRunning = false;
        if (autoPressThread != null) {
            autoPressThread.interrupt();
            try {
                autoPressThread.join(1000); // 等待线程结束
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            autoPressThread = null;
        }
    }

}
