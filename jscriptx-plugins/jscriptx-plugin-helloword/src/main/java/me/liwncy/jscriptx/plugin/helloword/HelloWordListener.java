package me.liwncy.jscriptx.plugin.helloword;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.core.manager.event.*;
import me.liwncy.jscriptx.core.manager.plugin.Plugin;

/**
 * 监听命令
 *
 * @author 插件监听
 */
@Slf4j(topic = "HelloWordListener")
public class HelloWordListener extends EventListener<KeyboardEvent, KeyboardEvent.KeyData> {
    public HelloWordListener(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean support(@NonNull KeyboardEvent event, KeyboardEvent.KeyData source) {
        log.debug("[{}] 插件收到按键：{}", source.getType(), source.getKeyText());
        log.debug("[{}] 插件收到按键：{}", plugin.getDescription().getName(), source.getKeyText());
        return false;
    }

    @Override
    public boolean onEvent(@NonNull KeyboardEvent event, KeyboardEvent.KeyData source) {
        return false;
    }

    // @Override
    // public boolean support(@NonNull CommandEvent<String> event, String source) {
    //     if (event.getData().equals("hello")) {
    //         log.debug("[{}] 插件收到命令：{}", plugin.getDescription().getName(), event.getData());
    //         return true;
    //     }
    //     return false;
    // }
    //
    // @Override
    // public boolean onEvent(@NonNull CommandEvent<String> event, String source) {
    //     log.debug("[{}] 插件处理命令：{}", plugin.getDescription().getName(), event.getData());
    //     return false;
    // }


}
