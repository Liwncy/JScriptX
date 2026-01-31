package me.liwncy.jscriptx.core;

import io.vertx.core.Vertx;
import lombok.Getter;
import lombok.Setter;
import me.liwncy.jscriptx.core.manager.command.CommandManager;
import me.liwncy.jscriptx.core.manager.event.EventManager;
import me.liwncy.jscriptx.core.manager.plugin.PluginManager;
import me.liwncy.jscriptx.core.strategy.FunctionServiceStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 上下文
 *
 * @author liwncy
 */
@Setter
@Getter
public final class Context {

    public static final Vertx vertx = Vertx.vertx();
    private static final Context INSTANCE = new Context();

    static {
        vertx.exceptionHandler(Throwable::printStackTrace);
    }

    private Config config;

    private EventManager eventManager;
    private PluginManager pluginManager;
    private CommandManager commandManager;

    // 功能服务映射
    private final Map<String, FunctionServiceStrategy> functionServices = new HashMap<>();

    /**
     * 添加功能服务
     *
     * @param functionService 功能服务
     */
    public void addFunctionService(FunctionServiceStrategy functionService) {
        functionServices.put(functionService.name(), functionService);
    }

    /**
     * 获取功能服务
     *
     * @param name 功能服务名称
     * @return 功能服务
     */
    public FunctionServiceStrategy getFunctionService(String name) {
        return functionServices.get(name);
    }

    /**
     * 获取所有功能服务
     *
     * @return 功能服务集合
     */
    public Set<Map.Entry<String, FunctionServiceStrategy>> getFunctionServices() {
        return functionServices.entrySet();
    }

    /**
     * 移除功能服务
     *
     * @param name 功能服务名称
     */
    public void removeFunctionService(String name) {
        functionServices.remove(name);
    }

    public static Context get() {
        return INSTANCE;
    }

}
