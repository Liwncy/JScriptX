package me.liwncy.jscriptx.core.manager.event;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.core.Context;

import java.util.Set;

/**
 * 键盘事件
 *
 * @author liwncy
 */
@Slf4j
@Getter
public class KeyEvent extends Event<KeyEvent.KeyData> {

    private final int keyCode;
    private final String keyText;
    private final KeyEventType type;
    // 当前所有按下的按键
    private final Set<Integer> pressedKeys;

    public KeyEvent(int keyCode, String keyText, KeyEventType type, Set<Integer> pressedKeys) {
        super(new KeyData(keyCode, keyText, type, pressedKeys));
        this.keyCode = keyCode;
        this.keyText = keyText;
        this.type = type;
        this.pressedKeys = pressedKeys;
    }

    public enum KeyEventType {
        PRESSED, RELEASED, TYPED
    }

    /**
     * 键盘事件数据
     */
    @Getter
    public static class KeyData {
        private final int keyCode;
        private final String keyText;
        private final KeyEventType type;
        private final Set<Integer> pressedKeys; // 当前所有按下的按键

        public KeyData(int keyCode, String keyText, KeyEventType type, Set<Integer> pressedKeys) {
            this.keyCode = keyCode;
            this.keyText = keyText;
            this.type = type;
            this.pressedKeys = pressedKeys;
        }
    }

    /**
     * 创建并发布按键按下事件
     */
    public static void pressed(int keyCode, String keyText, Set<Integer> pressedKeys) {
        new KeyEvent(keyCode, keyText, KeyEventType.PRESSED, pressedKeys);
    }

    /**
     * 创建并发布按键释放事件
     */
    public static void released(int keyCode, String keyText, Set<Integer> pressedKeys) {
        new KeyEvent(keyCode, keyText, KeyEventType.RELEASED, pressedKeys);
    }

    /**
     * 创建并发布按键输入事件
     */
    public static void typed(int keyCode, String keyText, Set<Integer> pressedKeys) {
        new KeyEvent(keyCode, keyText, KeyEventType.TYPED, pressedKeys).publish();
    }
}
