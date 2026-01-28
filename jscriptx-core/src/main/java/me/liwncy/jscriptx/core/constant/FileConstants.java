package me.liwncy.jscriptx.core.constant;

import java.io.File;

/**
 * 文件常量类
 *
 * @author liwncy.
 */
public interface FileConstants {
    File DATA_DIR = new File(System.getProperty("user.dir"));
    File ADAPTER_DIR = new File(DATA_DIR, "adapter");
    File LOG_DIR = new File(DATA_DIR, "log");
    File CONFIG_DIR = new File(DATA_DIR, "config");
    File PLUGIN_DIR = new File(DATA_DIR, "plugin");
    File IMAGE_DIR = new File(DATA_DIR, "image");
    File VOICE_DIR = new File(DATA_DIR, "voice");
    File VIDEO_DIR = new File(DATA_DIR, "video");

    File DEVICE_FILE = new File(CONFIG_DIR, "device");
    File CONFIG_FILE = new File(CONFIG_DIR, "config.json5");
    File LOGIN_FILE = new File(CONFIG_DIR, "memory.card");
    File PLUGIN_CONFIG_FILE = new File(CONFIG_DIR, "plugin.json5");
}
