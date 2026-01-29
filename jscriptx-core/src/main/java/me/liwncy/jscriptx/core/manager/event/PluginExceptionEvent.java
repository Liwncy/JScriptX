package me.liwncy.jscriptx.core.manager.event;

import lombok.Getter;
import me.liwncy.jscriptx.core.manager.plugin.Plugin;

/**
 * 插件异常事件
 *
 * @author liwncy 
 */
@Getter
public class PluginExceptionEvent extends ExceptionEvent {

    private final Plugin plugin;

    public PluginExceptionEvent(Plugin plugin, Throwable data) {
        super(data);
        this.plugin = plugin;
    }

    public static PluginExceptionEvent of(Plugin plugin, Throwable data) {
        return new PluginExceptionEvent(plugin, data);
    }
}
