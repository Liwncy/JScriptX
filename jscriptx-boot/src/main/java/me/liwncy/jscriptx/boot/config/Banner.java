package me.liwncy.jscriptx.boot.config;

import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.core.Context;

@Slf4j
public class Banner {

    public static void print() {
        try {
            // 确保 Context 类已经被加载和初始化
            Context.get();
            if (Context.vertx == null) {
                log.warn("Context.vertx 尚未初始化，跳过 banner 打印");
                return;
            }
            Context.vertx.fileSystem()
                    .readFile("banner.txt")
                    .onSuccess(buffer -> log.info(buffer.toString()))
                    .onFailure(t -> log.warn("读取 banner.txt 失败: {}", t.getMessage()))
                    .await();
        } catch (Exception e) {
            log.warn("打印 banner 时出现异常: {}", e.getMessage());
        }
    }

}
