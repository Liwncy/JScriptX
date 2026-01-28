package me.liwncy.jscriptx.boot.config;

import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.core.Context;

@Slf4j
public class Banner {

    public static void print() {
        Context.vertx.fileSystem()
                .readFile("banner.txt")
                .onSuccess(buffer -> log.info(buffer.toString()))
                .await();
    }

}
