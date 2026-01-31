package me.liwncy.jscriptx.core.listener;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.core.manager.event.KeyEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 全局键盘监听器，用于捕获按键事件
 * 使用 JNativeHook 实现真正的全局键盘监听，即使焦点在其他窗口也能检测到按键
 */
@Slf4j
public class GlobalKeyListener implements NativeKeyListener {
    private Map<Integer, Runnable> keyActions;
    private static boolean listening;
    private Set<Integer> pressedKeys = ConcurrentHashMap.newKeySet();
    // private JFrame statusWindow;
    
    public GlobalKeyListener() {
        keyActions = new HashMap<>();
        listening = false;
        
        // 创建状态窗口
        // createStatusWindow();
        
        // 禁用 JNativeHook 的日志输出
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
    }
    
    /**
     * 创建状态窗口
     */
    // private void createStatusWindow() {
    //     statusWindow = new JFrame("游戏脚本框架 - 全局按键监听");
    //     statusWindow.setSize(400, 300);
    //     statusWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    //     statusWindow.setLayout(new BorderLayout());
    //
    //     JLabel label = new JLabel("<html><center><b>全局按键监听器运行中</b><br/><br/>" +
    //             "按 F1 启动自动点击脚本<br/>" +
    //             "按 F2 停止自动点击脚本<br/>" +
    //             "按 = 键切换自动按F键脚本<br/>" +
    //             "按 - 键切换自动对话脚本<br/>" +
    //             "按 Ctrl+ESC 退出程序<br/><br/>" +
    //             "<font color='red'>注意：此监听器在后台运行，" +
    //             "即使焦点在其他窗口也能检测到按键</font></center></html>",
    //             SwingConstants.CENTER);
    //     statusWindow.add(label, BorderLayout.CENTER);
    // }
    
    /**
     * 注册按键动作
     * @param keyCode 键码
     * @param action 动作
     */
    public void registerKeyAction(int keyCode, Runnable action) {
        keyActions.put(keyCode, action);
    }
    
    /**
     * 开始监听
     */
    public void startListening() {
        if (!listening) {
            try {
                // 注册全局键盘监听器
                GlobalScreen.addNativeKeyListener(this);
                GlobalScreen.registerNativeHook();
                listening = true;
                
                // 显示状态窗口
                // statusWindow.setVisible(true);
                log.info("全局键盘监听器已启动");
            } catch (NativeHookException e) {
                log.error("启动全局键盘监听器失败：{}", e.getMessage(), e);
            }
        }
    }
    
    /**
     * 停止监听
     */
    public static void stopListening() {
        if (listening) {
            try {
                GlobalScreen.unregisterNativeHook();
                listening = false;
                // statusWindow.dispose();
                log.info("全局键盘监听器已停止");
            } catch (NativeHookException e) {
                log.error("停止全局键盘监听器失败：{}", e.getMessage(), e);
            }
        }
    }
    
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (listening) {
            int keyCode = e.getKeyCode();
            String keyText = NativeKeyEvent.getKeyText(keyCode);

            // 添加到当前按下的按键集合
            pressedKeys.add(keyCode);

            // 构建组合按键文本
            String combinationText = buildCombinationText();

            // 通过框架事件系统发布按键按下事件
            KeyEvent.pressed(keyCode, combinationText, pressedKeys);

            // 执行注册的动作
            Runnable action = keyActions.get(keyCode);
            if (action != null) {
                action.run();
            }
        }
    }
    
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (listening) {
            int keyCode = e.getKeyCode();
            String keyText = NativeKeyEvent.getKeyText(keyCode);

            // 从当前按下的按键集合中移除
            pressedKeys.remove(keyCode);

            // 构建组合按键文本
            String combinationText = buildCombinationText();

            // 通过框架事件系统发布按键释放事件
            KeyEvent.released(keyCode, combinationText, pressedKeys);
        }
    }

    /**
     * 处理按键输入事件（当按键被按下并释放时触发）
     * @param e
     */
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        if (listening) {
            int keyCode = e.getKeyCode();
            String keyText = NativeKeyEvent.getKeyText(keyCode);

            // 构建组合按键文本（使用当前所有按下的按键）
            String combinationText = buildCombinationText();

            // 通过框架事件系统发布按键输入事件
            KeyEvent.typed(keyCode, combinationText, pressedKeys);
        }
    }
    
    /**
     * 将 JNativeHook 的键码转换为 AWT 的键码
     * @param jnhKeyCode JNativeHook 键码
     * @return AWT 键码
     */
    private int convertJNativeHookKeyCode(int jnhKeyCode) {
        // 大多数键码是相同的，直接返回
        return jnhKeyCode;
    }

    /**
     * 构建组合按键文本，如 "Ctrl+Shift+A"
     */
    private String buildCombinationText() {
        List<String> keys = new ArrayList<>();

        // 按优先级排序：Ctrl > Alt > Shift > 其他按键
        if (pressedKeys.contains(NativeKeyEvent.VC_CONTROL)) {
            keys.add("Ctrl");
        }
        if (pressedKeys.contains(NativeKeyEvent.VC_ALT)) {
            keys.add("Alt");
        }
        if (pressedKeys.contains(NativeKeyEvent.VC_SHIFT)) {
            keys.add("Shift");
        }

        // 添加其他按键
        for (int keyCode : pressedKeys) {
            if (!isModifierKey(keyCode)) {
                keys.add(NativeKeyEvent.getKeyText(keyCode));
            }
        }

        return String.join("+", keys);
    }

    /**
     * 判断是否为修饰键（Ctrl、Alt、Shift等）
     */
    private boolean isModifierKey(int keyCode) {
        return keyCode == NativeKeyEvent.VC_CONTROL ||
                keyCode == NativeKeyEvent.VC_ALT ||
                keyCode == NativeKeyEvent.VC_SHIFT ||
                keyCode == NativeKeyEvent.VC_META;
    }
}