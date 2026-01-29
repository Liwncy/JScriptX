package me.liwncy.jscriptx.plugin.helloword;

import me.liwncy.jscriptx.core.manager.command.CommandExecutor;
import me.liwncy.jscriptx.core.manager.event.EventListener;
import me.liwncy.jscriptx.core.manager.plugin.Plugin;

/**
 * 插件入口
 *
 * @author liwncy
 */
public class HelloWordPlugin extends Plugin {

    @Override
    public void onLoad() {
        this.saveDefaultConfig();
    }

    @Override
    public EventListener<?, ?> getEventListener() {
        return new HelloWordListener(this);
    }

    @Override
    public CommandExecutor getCommandExecutor() {
        return new HelloWordExecutor(this);
    }
}
