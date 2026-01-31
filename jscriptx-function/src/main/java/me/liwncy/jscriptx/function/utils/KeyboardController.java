package me.liwncy.jscriptx.function.utils;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 键盘控制器，用于模拟键盘操作
 */
public class KeyboardController {
    private Robot robot;

    public KeyboardController() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("创建 Robot 实例失败", e);
        }
    }

    /**
     * 按下并释放指定键
     *
     * @param keyCode 键码
     */
    public void press(int keyCode) {
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
    }

    /**
     * 按下指定键
     *
     * @param keyCode 键码
     */
    public void keyDown(int keyCode) {
        robot.keyPress(keyCode);
    }

    /**
     * 释放指定键
     *
     * @param keyCode 键码
     */
    public void keyUp(int keyCode) {
        robot.keyRelease(keyCode);
    }

    /**
     * 输入字符串
     *
     * @param text 要输入的文本
     */
    public void type(String text) {
        for (char c : text.toCharArray()) {
            type(c);
        }
    }

    /**
     * 输入单个字符
     *
     * @param c 要输入的字符
     */
    public void type(char c) {
        // 处理特殊字符
        if (c == ' ') {
            press(KeyEvent.VK_SPACE);
            return;
        }

        // 处理大写字母
        if (Character.isUpperCase(c)) {
            keyDown(KeyEvent.VK_SHIFT);
            press(Character.toUpperCase(c));
            keyUp(KeyEvent.VK_SHIFT);
            return;
        }

        // 处理其他字符
        int keyCode = getKeyCode(c);
        if (keyCode != -1) {
            press(keyCode);
        }
    }

    /**
     * 按下组合键
     *
     * @param keyCodes 键码数组，第一个是修饰键
     */
    public void pressCombo(int... keyCodes) {
        if (keyCodes.length == 0) return;

        // 按下所有键
        for (int keyCode : keyCodes) {
            robot.keyPress(keyCode);
        }

        // 释放所有键
        for (int i = keyCodes.length - 1; i >= 0; i--) {
            robot.keyRelease(keyCodes[i]);
        }
    }

    /**
     * 获取字符对应的键码
     *
     * @param c 字符
     * @return 键码，-1 表示未找到
     */
    private int getKeyCode(char c) {
        // 简单的字符到键码映射
        switch (Character.toLowerCase(c)) {
            case 'a':
                return KeyEvent.VK_A;
            case 'b':
                return KeyEvent.VK_B;
            case 'c':
                return KeyEvent.VK_C;
            case 'd':
                return KeyEvent.VK_D;
            case 'e':
                return KeyEvent.VK_E;
            case 'f':
                return KeyEvent.VK_F;
            case 'g':
                return KeyEvent.VK_G;
            case 'h':
                return KeyEvent.VK_H;
            case 'i':
                return KeyEvent.VK_I;
            case 'j':
                return KeyEvent.VK_J;
            case 'k':
                return KeyEvent.VK_K;
            case 'l':
                return KeyEvent.VK_L;
            case 'm':
                return KeyEvent.VK_M;
            case 'n':
                return KeyEvent.VK_N;
            case 'o':
                return KeyEvent.VK_O;
            case 'p':
                return KeyEvent.VK_P;
            case 'q':
                return KeyEvent.VK_Q;
            case 'r':
                return KeyEvent.VK_R;
            case 's':
                return KeyEvent.VK_S;
            case 't':
                return KeyEvent.VK_T;
            case 'u':
                return KeyEvent.VK_U;
            case 'v':
                return KeyEvent.VK_V;
            case 'w':
                return KeyEvent.VK_W;
            case 'x':
                return KeyEvent.VK_X;
            case 'y':
                return KeyEvent.VK_Y;
            case 'z':
                return KeyEvent.VK_Z;
            case '0':
                return KeyEvent.VK_0;
            case '1':
                return KeyEvent.VK_1;
            case '2':
                return KeyEvent.VK_2;
            case '3':
                return KeyEvent.VK_3;
            case '4':
                return KeyEvent.VK_4;
            case '5':
                return KeyEvent.VK_5;
            case '6':
                return KeyEvent.VK_6;
            case '7':
                return KeyEvent.VK_7;
            case '8':
                return KeyEvent.VK_8;
            case '9':
                return KeyEvent.VK_9;
            case '!':
                return KeyEvent.VK_1; // 需要配合Shift
            case '@':
                return KeyEvent.VK_2; // 需要配合Shift
            case '#':
                return KeyEvent.VK_3; // 需要配合Shift
            case '$':
                return KeyEvent.VK_4; // 需要配合Shift
            case '%':
                return KeyEvent.VK_5; // 需要配合Shift
            case '^':
                return KeyEvent.VK_6; // 需要配合Shift
            case '&':
                return KeyEvent.VK_7; // 需要配合Shift
            case '*':
                return KeyEvent.VK_8; // 需要配合Shift
            case '(':
                return KeyEvent.VK_9; // 需要配合Shift
            case ')':
                return KeyEvent.VK_0; // 需要配合Shift
            case '-':
                return KeyEvent.VK_MINUS;
            case '_':
                return KeyEvent.VK_MINUS; // 需要配合Shift
            case '=':
                return KeyEvent.VK_EQUALS;
            case '+':
                return KeyEvent.VK_EQUALS; // 需要配合Shift
            case '[':
                return KeyEvent.VK_OPEN_BRACKET;
            case '{':
                return KeyEvent.VK_OPEN_BRACKET; // 需要配合Shift
            case ']':
                return KeyEvent.VK_CLOSE_BRACKET;
            case '}':
                return KeyEvent.VK_CLOSE_BRACKET; // 需要配合Shift
            case '\\':
                return KeyEvent.VK_BACK_SLASH;
            case '|':
                return KeyEvent.VK_BACK_SLASH; // 需要配合Shift
            case ';':
                return KeyEvent.VK_SEMICOLON;
            case ':':
                return KeyEvent.VK_SEMICOLON; // 需要配合Shift
            case '\'':
                return KeyEvent.VK_QUOTE;
            case '"':
                return KeyEvent.VK_QUOTE; // 需要配合Shift
            case '`':
                return KeyEvent.VK_BACK_QUOTE;
            case '~':
                return KeyEvent.VK_BACK_QUOTE; // 需要配合Shift
            case ',':
                return KeyEvent.VK_COMMA;
            case '<':
                return KeyEvent.VK_COMMA; // 需要配合Shift
            case '.':
                return KeyEvent.VK_PERIOD;
            case '>':
                return KeyEvent.VK_PERIOD; // 需要配合Shift
            case '/':
                return KeyEvent.VK_SLASH;
            case '?':
                return KeyEvent.VK_SLASH; // 需要配合Shift
            default:
                return -1;
        }
    }
}