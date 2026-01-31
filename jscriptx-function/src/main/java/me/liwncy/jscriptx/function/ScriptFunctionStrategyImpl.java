package me.liwncy.jscriptx.function;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.core.Context;
import me.liwncy.jscriptx.core.constant.FileConstants;
import me.liwncy.jscriptx.core.strategy.FunctionServiceStrategy;
import me.liwncy.jscriptx.function.utils.*;

import java.io.File;

/**
 * 默认功能服务实现
 */
@Slf4j
public class ScriptFunctionStrategyImpl implements FunctionServiceStrategy {

    private static @Getter JsonObject config;
    public static final String NAME = "script-function";
    private static final String CONFIG_PATH = new File(FileConstants.ADAPTER_DIR, NAME + File.separator + "config.json").getPath();

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Future<Void> start() {
        return Future.succeededFuture();
    }

    @Override
    public Future<Void> onInit(JsonObject config) {
        FileSystem fs = Context.vertx.fileSystem();
        return fs.mkdirs(new File(FileConstants.ADAPTER_DIR, NAME).getPath())
                .onSuccess(v -> {
                    // 如果存在配置文件，则合并配置
                    if (fs.existsBlocking(CONFIG_PATH)) config.mergeIn(fs.readFileBlocking(CONFIG_PATH).toJsonObject());
                    log.debug("adapter config: {}", config.encodePrettily());
                    // 启动协议服务还是使用现有服务？
                    ScriptFunctionStrategyImpl.config = config;
                    // if (config.getBoolean("start_server", true)) {
                    //     Util.startServer(config);
                    // }
                    log.info(NAME + " 初始化完成");
                });
    }


    @Override
    public Future<Void> onDestroy() {
        return saveConfig();
    }

    public static Future<Void> saveConfig() {
        return Context.vertx.fileSystem()
                .writeFile(CONFIG_PATH, Buffer.buffer(config.encodePrettily()))
                .onSuccess(v -> log.debug("配置文件保存成功"));
    }
}