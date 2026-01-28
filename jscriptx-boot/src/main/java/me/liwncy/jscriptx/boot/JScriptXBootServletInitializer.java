package me.liwncy.jscriptx.boot;

import io.vertx.core.Future;
import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.boot.manager.command.DefaultCommandManager;
import me.liwncy.jscriptx.boot.manager.plugin.DebugPluginManager;
import me.liwncy.jscriptx.boot.manager.plugin.DefaultPluginManager;
import me.liwncy.jscriptx.core.BootServletInitializer;
import me.liwncy.jscriptx.core.Config;
import me.liwncy.jscriptx.core.Context;
import me.liwncy.jscriptx.core.constant.FileConstants;


@Slf4j
public class JScriptXBootServletInitializer implements BootServletInitializer {

    // 加载配置文件
    Config config = Config.load().await();

    public Future<Void> start() {
        log.info("JScriptX 启动中...");
        Context context = Context.get();
        context.getPluginManager().reInit();
        return Future.succeededFuture();
    }

    public Future<Void> stop() {
        log.info("JScriptX 停止中...");
        Context context = Context.get();
        // context.getEventManager().close().onSuccess(v -> log.info("事件管理器关闭成功")).onFailure(t -> log.warn("事件管理器关闭时出现异常：{}", t.getMessage()));
        context.getPluginManager().close().onSuccess(v -> log.info("插件管理器关闭成功")).onFailure(t -> log.warn("插件管理器关闭时出现异常：{}", t.getMessage()));
        context.getCommandManager().close().onSuccess(v -> log.info("命令管理器关闭成功")).onFailure(t -> log.warn("命令管理器关闭时出现异常：{}", t.getMessage()));
        return Context.vertx.close().onSuccess(v -> log.info("vertx关闭成功")).onFailure(t -> log.warn("vertx关闭时出现异常：{}", t.getMessage()));

    }

    /**
     * 初始化管理器
     */
    public Future<Void> initManager() {
        return new DefaultCommandManager().init()
                .compose(v -> Context.get().getConfig().getBase().getDebug() ? new DebugPluginManager().init() : new DefaultPluginManager().init());
    }

    /**
     * 创建目录
     */
    public Future<Void> mkdir() {
        var fs = Context.vertx.fileSystem();
        return fs.mkdirs(FileConstants.CONFIG_DIR.getPath())
                .compose(v -> fs.mkdirs(FileConstants.PLUGIN_DIR.getPath()))
                .compose(v -> fs.mkdirs(FileConstants.LOG_DIR.getPath()))
                .onFailure(t -> {
                    log.warn("创建目录时出现异常：{}", t.getMessage());
                    System.exit(1);
                });
    }
}
