package me.liwncy.jscriptx.core.manager.plugin;

import io.vertx.core.Future;
import me.liwncy.jscriptx.core.Context;
import me.liwncy.jscriptx.core.manager.Manager;
import me.liwncy.jscriptx.core.manager.ManagerLifeCycle;

import java.io.File;
import java.util.Collection;

/**
 * 插件管理器
 *
 * @author liwncy 
 */
public interface PluginManager extends Manager, ManagerLifeCycle {

    void reInit();

    /**
     * 加载插件目录下的所有插件，存放在内部容器中
     */
    void loadPlugins();

    /**
     * 获取插件
     *
     * @param name 插件名称
     * @return {@link Plugin }
     */
    Plugin get(String name);

    /**
     * 获取配置
     *
     * @param name 插件名称
     * @return {@link PluginConfig }
     */
    PluginConfig getConfig(String name);

    /**
     * 根据名称加载插件
     *
     * @param name 名字
     * @return {@link Future }<{@link Plugin }>
     */
    Future<Plugin> load(String name);

    /**
     * 根据文件加载插件
     *
     * @param file 文件
     * @return {@link Future }<{@link Plugin }>
     */
    Future<Plugin> load(File file);

    /**
     * 卸载插件
     *
     * @param name 名字
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> unload(String name);

    /**
     * 卸载插件
     *
     * @param plugin 插件
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> unload(Plugin plugin);

    /**
     * 启用插件
     *
     * @param name   名字
     // * @param target 目标
     * @return {@link Future }<{@link Void }>
     */
    Future<String> enable(String name);

    /**
     * 禁用插件
     *
     * @param name   名字
     // * @param target 目标
     * @return {@link Future }<{@link Void }>
     */
    Future<String> disable(String name);

    /**
     * 获取所有已加载的插件列表
     *
     * @return {@link Collection }<{@link Plugin }>
     */
    Collection<Plugin> list();

    // /**
    //  * 获取指定来目标的所有可用的已加载的插件
    //  *
    //  * @return {@link Collection }<{@link Plugin }>
    //  */
    // Collection<Plugin> availableList();
    //
    // /**
    //  * 获取限制模式
    //  *
    //  * @return {@link LimitMode }
    //  */
    // LimitMode getLimitMode();
    //
    // /**
    //  * 检查消息来源是否在插件限制名单内
    //  *
    //  * @param plugin 插件
    //  * @param target 目标
    //  * @return boolean
    //  */
    // boolean isLimited(Plugin plugin, Contactable target);

    /**
     * 保存配置
     *
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> saveConfig();

    @Override
    default Future<Void> init() {
        return Future.future(promise -> {
            try {
                this.onInit().onFailure(promise::fail).onSuccess(v -> Context.get().setPluginManager(this)).await();
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }

    @Override
    default Future<Void> close() {
        return Future.future(promise -> {
            try {
                this.onDestroy().onFailure(promise::fail).onSuccess(v -> Context.get().setPluginManager(null));
                promise.complete();
            } catch (Exception e) {promise.fail(e);}
        });
    }
}
