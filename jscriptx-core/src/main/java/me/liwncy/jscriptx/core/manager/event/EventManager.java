package me.liwncy.jscriptx.core.manager.event;

import io.vertx.core.Future;
import me.liwncy.jscriptx.core.Context;
import me.liwncy.jscriptx.core.manager.Manager;
import me.liwncy.jscriptx.core.manager.ManagerLifeCycle;
import me.liwncy.jscriptx.core.manager.plugin.Plugin;

/**
 * 事件管理器
 *
 * @author liwncy 
 */
public interface EventManager extends Manager, ManagerLifeCycle {

    /**
     * 注册插件中的事件监听器
     *
     * @param plugin 插件
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> register(Plugin plugin);

    /**
     * 卸载插件中的事件监听器
     *
     * @param plugin 插件
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> unregister(Plugin plugin);

    /**
     * 发布事件
     *
     * @param event 事件
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> publish(Event<?> event);

    @Override
    default Future<Void> init() {
        return Future.future(promise -> {
            try {
                this.onInit().onFailure(promise::fail).onSuccess(v -> Context.get().setEventManager(this));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }

    @Override
    default Future<Void> close() {
        return Future.future(promise -> {
            try {
                this.onDestroy().onFailure(promise::fail).onSuccess(v -> Context.get().setEventManager(null));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }
}
