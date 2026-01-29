package me.liwncy.jscriptx.core.manager.command;

import io.vertx.core.Future;
import me.liwncy.jscriptx.core.Context;
import me.liwncy.jscriptx.core.manager.Manager;
import me.liwncy.jscriptx.core.manager.ManagerLifeCycle;
import me.liwncy.jscriptx.core.manager.plugin.Plugin;

import java.util.Collection;
import java.util.Map;

/**
 * 命令管理器
 *
 * @author liwncy 
 */
public interface CommandManager extends Manager, ManagerLifeCycle {

    /**
     * 注册命令执行器
     *
     * @param plugin 插件
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> register(Plugin plugin);

    /**
     * 卸载命令执行器
     *
     * @param plugin 插件
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> unregister(Plugin plugin);

    /**
     * 执行命令行
     *
     * @param commandLine 命令行
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> execute(String commandLine);

    /**
     * 执行命令
     *
     * @param command 命令
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> execute(Command command);

    /**
     * 获取所有命令名称（包含别名）
     *
     * @return {@link Collection }<{@link String }>
     */
    Collection<String> names();

    /**
     * 获取所有命令执行器
     *
     * @return {@link Collection }<{@link CommandExecutor }>
     */
    Collection<CommandExecutor> list();

    @Override
    default Future<Void> init() {
        return Future.future(promise -> {
            try {
                this.onInit().onFailure(promise::fail).onSuccess(v -> Context.get().setCommandManager(this));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }

    @Override
    default Future<Void> close() {
        return Future.future(promise -> {
            try {
                this.onDestroy().onFailure(promise::fail).onSuccess(v -> Context.get().setCommandManager(null));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }
}
