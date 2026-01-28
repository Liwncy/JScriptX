package me.liwncy.jscriptx.core;

import io.vertx.core.Vertx;
import lombok.Getter;
import lombok.Setter;
import me.liwncy.jscriptx.core.manager.command.CommandManager;
import me.liwncy.jscriptx.core.manager.plugin.PluginManager;

/**
 * 上下文
 *
 * @author ovo created on 2025/02/17.
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

    private PluginManager pluginManager;
    private CommandManager commandManager;


    public static Context get() {
        return INSTANCE;
    }

}
