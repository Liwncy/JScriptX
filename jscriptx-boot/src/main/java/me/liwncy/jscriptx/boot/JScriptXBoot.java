package me.liwncy.jscriptx.boot;

import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.boot.config.Banner;

@Slf4j
public class JScriptXBoot {

    public static void main(String[] args) {
        Banner.print();

        try {
            var boot = new JScriptXBootServletInitializer();
            boot.mkdir().await();
            boot.initManager().await();
            boot.start()
                    .onSuccess(v -> log.info("JScriptXBoot 启动成功"))
                    .onFailure(e -> log.error("JScriptXBoot 启动失败：{}", e.getMessage(), e));
        } catch (Exception e) {
            log.error("JScriptXBoot 启动失败：{}", e.getMessage(), e);
            System.exit(1);
        }
    }

}
