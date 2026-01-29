package me.liwncy.jscriptx.plugin.helloword;

import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.core.manager.command.CommandExecutor;
import me.liwncy.jscriptx.core.manager.plugin.Plugin;
import picocli.CommandLine;

/**
 * 自定义插件命令执行器
 *
 * @author liwncy
 */
@SuppressWarnings("unused")
@Slf4j(topic = "HelloWordExecutor")
@CommandLine.Command(name = "hello-word", aliases = "hello", description = "hello word!")
public class HelloWordExecutor extends CommandExecutor {

    public HelloWordExecutor(Plugin plugin) {
        super(plugin);
    }

    @CommandLine.Command(name = "help", description = "显示插件帮助信息")
    public String help() {
        log.info("插件帮助信息:你好你好你好！");
        return "hello help me！";
    }
}
