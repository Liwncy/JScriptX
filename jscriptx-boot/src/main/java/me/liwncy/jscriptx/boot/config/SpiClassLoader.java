package me.liwncy.jscriptx.boot.config;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.core.constant.FileConstants;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Spi 类加载器
 *
 * @author Liwncy
 */
@Slf4j
public class SpiClassLoader extends URLClassLoader {

    public SpiClassLoader() {
        super(new URL[0], ClassLoader.getSystemClassLoader());
        loadFuncJars();
    }

    private void loadFuncJars() {
        FileUtil.loopFiles(FileConstants.ADAPTER_DIR, (file) -> {
            if (file.getName().endsWith(".jar")) {
                try {
                    this.addURL(file.toURI().toURL());
                } catch (Exception e) {
                    log.warn("加载适配器失败", e);
                }
            }
            return true;
        });
    }
}
