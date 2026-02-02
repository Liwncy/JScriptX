package me.liwncy.jscriptx.plugin.autodialog;

import me.liwncy.jscriptx.core.manager.command.CommandExecutor;
import me.liwncy.jscriptx.core.manager.event.EventListener;
import me.liwncy.jscriptx.core.manager.plugin.Plugin;

/**
 * 插件入口
 *
 * @author liwncy
 */
public class AutoDialogPlugin extends Plugin {

    @Override
    public void onLoad() {
        this.saveDefaultConfig();
    }

    @Override
    public CommandExecutor getCommandExecutor() {
        return null;
    }

    @Override
    public EventListener<?, ?> getEventListener() {
        return new AutoDialogListener(this);
    }

}
