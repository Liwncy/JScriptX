package me.liwncy.jscriptx.boot.manager.command;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import me.liwncy.jscriptx.core.Context;
import me.liwncy.jscriptx.core.constant.Constants;
import me.liwncy.jscriptx.core.manager.command.CommandExecutor;
import me.liwncy.jscriptx.core.manager.plugin.Plugin;
import me.liwncy.jscriptx.core.manager.plugin.PluginDescription;
import me.liwncy.jscriptx.core.manager.plugin.PluginManager;
import picocli.CommandLine;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * 插件管理相关命令执行器
 *
 * @author liwncy
 */
@CommandLine.Command(name = "plugin-manager", aliases = {"pm"}, description = "插件管理")
public class PmExecutor extends CommandExecutor implements Callable<String> {

    @CommandLine.Parameters(paramLabel = "operator", index = "0", description = "操作方式 [enable|disable|load|unload|reload|list|info]")
    private String operator;

    @CommandLine.Parameters(paramLabel = "plugins", index = "1..*", description = "需要操作的插件名称")
    private String[] plugins;

    private PluginManager pm;

    public PmExecutor() {
        super();
    }

    private PluginManager getPm() {
        if (Objects.isNull(this.pm)) this.pm = Context.get().getPluginManager();
        return this.pm;
    }

    @Override
    public String call() {
        return switch (operator) {
            case "enable" -> this.enable();
            case "disable" -> this.disable();
            case "load" -> this.load();
            case "unload" -> this.unload();
            case "reload" -> this.reload();
            case "list" -> this.list();
            case "info" -> this.info();
            default -> this.spec.commandLine().getUsageMessage();
        };
    }

    private String enable() {
        List<String> error = new ArrayList<>();
        List<String> success = new ArrayList<>();
        Arrays.stream(plugins)
                .distinct()
                .forEach(name -> Optional.ofNullable(this.getPm().get(name)).ifPresentOrElse(p ->
                        this.getPm().enable(name).onSuccess(v -> success.add(name)).onFailure(v -> error.add(name)).await(), () -> error.add(name)
                ));
        return (success.isEmpty() ? "" : "插件 " + success + " 已成功启用") +
                (error.isEmpty() ? "" : "，插件 " + error + " 不存在");
    }

    private String disable() {
        List<String> error = new ArrayList<>(), success = new ArrayList<>();
        Arrays.stream(plugins)
                .distinct()
                .forEach(name -> Optional.ofNullable(this.getPm().get(name)).ifPresentOrElse(p ->
                        this.getPm().disable(name).onSuccess(v -> success.add(name)).onFailure(v -> error.add(name)).await(), () -> error.add(name)
                ));
        return
                (success.isEmpty() ? "" : "插件 " + success + " 已成功禁用") +
                        (error.isEmpty() ? "" : "，插件 " + error + " 不存在");
    }

    private String load() {
        List<String> error = new ArrayList<>(), success = new ArrayList<>();
        Arrays.stream(this.plugins).forEach(name -> this.getPm().load(name)
                .onSuccess(v -> success.add(name))
                .onFailure(v -> error.add(StrUtil.format("插件 [{}] 加载失败：{}", name, v.getMessage())))
                .await()
        );

        String successStr = CollUtil.isEmpty(success) ? "" : StrUtil.format("插件 {} 已成功加载", success);
        String errorStr = CollUtil.isEmpty(error) ? "" : String.join("\n", error);
        return StrUtil.join("\n", successStr, errorStr);
    }

    private String unload() {
        List<String> error = new ArrayList<>(), success = new ArrayList<>();
        Arrays.stream(this.plugins).forEach(name -> {
            try {
                this.getPm().unload(name);
                success.add(name);
            } catch (Exception e) {
                error.add(StrUtil.format("插件 [{}] 卸载失败：{}", name, e.getMessage()));
            }
        });
        String successStr = CollUtil.isEmpty(success) ? "" : StrUtil.format("插件 {} 已成功卸载", success);
        String errorStr = CollUtil.isEmpty(error) ? "" : String.join("\n", error);
        return StrUtil.join("\n", successStr, errorStr);
    }

    private String reload() {
        Arrays.stream(this.plugins).forEach(name -> this.getPm().unload(name).compose(v -> this.getPm().load(name)).await());
        return "插件 " + Arrays.toString(this.plugins) + " 已成功重载";
    }

    private String list() {
        Collection<String> plugins = this.getPm().list().stream().map(plugin -> (plugin.isEnabled() ? "✔️ " : "❌ ") + plugin.getDescription().getName() + " " + plugin.getDescription().getDescription()).toList();
        return StrUtil.format("插件列表 [共 {} 个]", plugins.size()) + Constants.DELIMITER + String.join("\n", plugins);
    }

    private String info() {
        String name = this.plugins[0];
        Plugin plugin = this.getPm().get(name);
        if (Objects.isNull(plugin)) return "插件 [" + name + "] 不存在";

        PluginDescription desc = plugin.getDescription();
        StringBuilder sb = new StringBuilder();
        sb.append("插件名称：").append(desc.getName()).append("\n");
        sb.append("插件版本：").append(desc.getVersion()).append("\n");
        sb.append("插件作者：").append(String.join(",", desc.getAuthors())).append("\n");
        sb.append("插件主类：").append(desc.getMain()).append("\n");
        sb.append("插件描述：").append(desc.getDescription()).append("\n");
        CommandExecutor excutor = plugin.getCommandExecutor();
        if (Objects.nonNull(excutor)) {
            List<String> list = new ArrayList<>();
            CommandLine.Model.CommandSpec spec = new CommandLine(excutor).getCommandSpec();
            String command = spec.name();
            String[] aliases = spec.aliases();
            list.add(command);
            list.addAll(Arrays.asList(aliases));
            sb.append("插件指令：").append(String.join(",", String.join(", ", list))).append("\n");
        }
        return "插件 [" + name + "] 信息" + Constants.DELIMITER + sb;
    }
}
