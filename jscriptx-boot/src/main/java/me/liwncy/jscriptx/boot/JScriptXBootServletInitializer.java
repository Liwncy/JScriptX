package me.liwncy.jscriptx.boot;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer;
import io.vertx.core.Future;
import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.boot.config.SpiClassLoader;
import me.liwncy.jscriptx.boot.manager.command.DefaultCommandManager;
import me.liwncy.jscriptx.boot.manager.event.DefaultEventManager;
import me.liwncy.jscriptx.boot.manager.plugin.DebugPluginManager;
import me.liwncy.jscriptx.boot.manager.plugin.DefaultPluginManager;
import me.liwncy.jscriptx.core.BootServletInitializer;
import me.liwncy.jscriptx.core.Config;
import me.liwncy.jscriptx.core.Context;
import me.liwncy.jscriptx.core.constant.FileConstants;
import me.liwncy.jscriptx.core.listener.GlobalKeyListener;
import me.liwncy.jscriptx.core.strategy.FunctionServiceStrategy;
import me.liwncy.jscriptx.function.ScriptFunctionStrategyImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;


@Slf4j
public class JScriptXBootServletInitializer implements BootServletInitializer {

    // 加载配置文件
    Config config = Config.load().await();
    private final Map<String, FunctionServiceStrategy> functionServices = new HashMap<>();

    {
        // 通过SPI加载适配器
        var it = ServiceLoader.load(FunctionServiceStrategy.class, new SpiClassLoader()).iterator();
        while (it.hasNext()) {
            FunctionServiceStrategy strategy = it.next();
            strategy.init(config.getFuncBaseConfig());
            // 处理每个策略实例
            Context.get().addFunctionService(strategy);
        }

    }

    // 在 start() 方法中
    @Override
    public Future<Void> start() {
        log.info("JScriptX 启动中...");
        Context context = Context.get();
        context.getPluginManager().reInit();

        // 启动所有功能服务
        for (var entry : context.getFunctionServices()) {
            entry.getValue().start()
                    .onSuccess(v -> log.info("功能服务 {} 启动成功", entry.getKey()))
                    .onFailure(t -> log.warn("功能服务 {} 启动时出现异常：{}", entry.getKey(), t.getMessage()));
        }
        // 全局键盘监听
        new GlobalKeyListener().startListening();
        return Future.succeededFuture();
    }

    public Future<Void> stop() {
        log.info("JScriptX 停止中...");
        Context context = Context.get();

        // 关闭所有功能服务
        for (var entry : context.getFunctionServices()) {
            entry.getValue().onDestroy().onSuccess(v -> log.info("功能服务 {} 关闭成功", entry.getKey())).onFailure(t -> log.warn("功能服务 {} 关闭时出现异常：{}", entry.getKey(), t.getMessage()));
        }
        context.getEventManager().close().onSuccess(v -> log.info("事件管理器关闭成功")).onFailure(t -> log.warn("事件管理器关闭时出现异常：{}", t.getMessage()));
        context.getPluginManager().close().onSuccess(v -> log.info("插件管理器关闭成功")).onFailure(t -> log.warn("插件管理器关闭时出现异常：{}", t.getMessage()));
        context.getCommandManager().close().onSuccess(v -> log.info("命令管理器关闭成功")).onFailure(t -> log.warn("命令管理器关闭时出现异常：{}", t.getMessage()));
        // 全局键盘监听关闭
        GlobalKeyListener.stopListening();
        return Context.vertx.close().onSuccess(v -> log.info("vertx关闭成功")).onFailure(t -> log.warn("vertx关闭时出现异常：{}", t.getMessage()));
    }

    /**
     * 初始化管理器
     */
    public Future<Void> initManager() {
        return Future.succeededFuture()
                .compose(v -> new DefaultEventManager().init())
                .compose(v -> new DefaultCommandManager().init())
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
