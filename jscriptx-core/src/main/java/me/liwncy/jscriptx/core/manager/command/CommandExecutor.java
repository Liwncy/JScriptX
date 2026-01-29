package me.liwncy.jscriptx.core.manager.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.liwncy.jscriptx.core.manager.plugin.Plugin;
import picocli.CommandLine;

/**
 * 命令执行器
 * @author liwncy 
 */
// @RequiredArgsConstructor
@CommandLine.Command(sortOptions = false, resourceBundle = "i18n", mixinStandardHelpOptions = true)
public abstract class CommandExecutor {

    protected @Setter Command command;
    /** 命令执行器所属的插件 */
    protected @Getter final Plugin plugin;
    /** 命令的相关元数据，由运行时CommandLine注入，在子类实现中可能会用到 */
    protected @CommandLine.Spec CommandLine.Model.CommandSpec spec;

    public CommandExecutor() {
        this.plugin = null;
    }

    public CommandExecutor(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 获取无执行权限提示
     *
     * @return {@link String }
     */
    public String getNoPermissionTip() {
        return null;
    }

    class PluginVersionProvider implements CommandLine.IVersionProvider {
        public PluginVersionProvider() {
        }

        @Override
        public String[] getVersion() {
            return new String[]{plugin.getDescription().getVersion()};
        }
    }

}
