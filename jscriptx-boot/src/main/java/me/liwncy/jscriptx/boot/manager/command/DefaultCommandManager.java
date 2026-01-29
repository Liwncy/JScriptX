package me.liwncy.jscriptx.boot.manager.command;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import io.vertx.core.Future;
import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.core.Context;
import me.liwncy.jscriptx.core.manager.command.Command;
import me.liwncy.jscriptx.core.manager.command.CommandExecutor;
import me.liwncy.jscriptx.core.manager.command.CommandManager;
import me.liwncy.jscriptx.core.manager.plugin.Plugin;
import me.liwncy.jscriptx.core.manager.plugin.PluginManager;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认命令管理器
 *
 * @author liwncy 
 */
@Slf4j
public class DefaultCommandManager implements CommandManager {

    private final PluginManager pluginManager = Context.get().getPluginManager();
    private final Map<String, CommandExecutor> container = new HashMap<>();

    @Override
    public Future<Void> onInit() throws Exception {
        // 注册全局 PmExecutor 命令
        register().onSuccess(promise -> {log.info("命令管理器注册 PmExecutor 命令成功");})
                .onFailure(t -> log.warn("命令管理器注册 PmExecutor 命令时出现异常：{}", t.getMessage()));
        log.info("命令管理器初始化完成");
        ThreadUtil.execute(scanner());
        return Future.succeededFuture();
    }

    /**
     * 注册 PmExecutor 命令
     */
    public Future<Void> register() {
        return Future.future(promise -> {
            PmExecutor pmExecutor = new PmExecutor();
            CommandLine pmCommand = new CommandLine(pmExecutor);
            container.put(StrUtil.addPrefixIfNot(pmCommand.getCommandName(), "/"), pmExecutor);
            Optional.ofNullable(pmCommand.getCommandSpec().aliases())
                    .ifPresent(aliases -> Arrays.stream(aliases).forEach(alias -> this.container.put(StrUtil.addPrefixIfNot(alias, "/"), pmExecutor)));
            promise.complete();
        });
    }

    @Override
    public Future<Void> register(Plugin plugin) {
        if (Objects.isNull(plugin.getCommandExecutor())) return Future.succeededFuture();
        return Future.future(promise -> Optional.ofNullable(plugin.getCommandExecutor()).ifPresent(executor -> {
            var command = new CommandLine(executor);
            this.container.put(StrUtil.addPrefixIfNot(command.getCommandName(), "/"), executor);
            Optional.ofNullable(command.getCommandSpec().aliases())
                    .ifPresent(aliases -> Arrays.stream(aliases).forEach(alias -> this.container.put(StrUtil.addPrefixIfNot(alias, "/"), executor)));
            log.debug("插件 [{}] 注册命令执行器成功", plugin.getDescription().getName());
            promise.complete();
        }));
    }

    @Override
    public Future<Void> unregister(Plugin plugin) {
        return Future.future(promise -> Optional.ofNullable(plugin.getCommandExecutor()).ifPresent(executor -> {
            var command = new CommandLine(executor);
            this.container.remove(StrUtil.addPrefixIfNot(command.getCommandName(), "/"));
            Optional.ofNullable(command.getCommandSpec().aliases())
                    .ifPresent(aliases -> Arrays.stream(aliases).forEach(alias -> this.container.remove(StrUtil.addPrefixIfNot(alias, "/"))));
        }));
    }

    /**
     * 扫描控制台输入
     */
    private Runnable scanner() {
        return () -> {
            log.info("开启监听控制台命令输入");

            // 扫描控制台输入
            var scanner = new java.util.Scanner(System.in);
            while (scanner.hasNext()) {
                // 构建文本消息
                var text = scanner.nextLine();
                if (StrUtil.isBlank(text)) {
                    continue;
                }
                execute(text)
                        .onFailure(e -> {
                            System.out.println("命令执行失败：" + e.getMessage());
                            log.error("命令执行失败：{}", e.getMessage());
                        });
            }
        };
    }

    @Override
    public Future<Void> execute(String commandLine) {
        if (StrUtil.isBlank(commandLine)) {
            return Future.succeededFuture();
        }
        // 解析命令行
        Command command = Command.of(commandLine);
        // 执行命令
        return execute(command);
    }

    @Override
    public Future<Void> execute(Command command) {
        // 1、获取命令，如果容器中不存在该命令则返回
        if (!this.container.containsKey(command.getCommand())) return Future.failedFuture("命令不存在");
        // 获取命令消息来源
        // var user = command.getFrom();
        var executor = this.container.get(command.getCommand());
        executor.setCommand(command);
        return Future.<String>future(promise -> {

                    // 构建CommandLine并重定向输出流
                    var cmd = new CommandLine(executor);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos));
                    cmd.setOut(writer);
                    cmd.setErr(writer);
                    cmd.setExecutionExceptionHandler((ex, commandLine1, fullParseResult) -> {
                        writer.print("命令执行出现异常：" + ex.getMessage());
                        return 0;
                    });

                    // 执行命令并获取命令执行的结果
                    try (baos; writer) {
                        cmd.execute(command.getArgs());
                        String result = cmd.getParseResult().asCommandLineList().stream()
                                .map(CommandLine::getExecutionResult)
                                .filter(Objects::nonNull)
                                .map(o -> (String) o)
                                .collect(Collectors.joining("\n"));
                        writer.flush();
                        String res = StrUtil.blankToDefault(result, baos.toString());
                        log.info("命令[{}]执行结果：\n{}", command.getCommand(), res);
                        promise.complete(StrUtil.isBlank(res) ? "命令执行成功，但是没有返回值" : res);
                    } catch (Exception e) {
                        // log.error("执行指令 [{}] 时出现异常: {}", command.getMessage().getContent(), e.getMessage());
                        // log.debug("执行指令 [{}] 时出现异常", command.getMessage().getContent(), e);
                        // new ExceptionEvent(e).publish();
                        // promise.complete(StrUtil.format("执行指令 [{}] 时出现异常: {}", command.getMessage().getContent(), e.getMessage()));
                    }
                })
                // .compose(str -> {
                //     var msg = new TextMessage();
                //     msg.setContent(str);
                //     Contactable sender = command.getMessage().getSender();
                //     if (sender.getType().equals(ContactType.GROUP)) {
                //         msg.setAts(user.getId());
                //     }
                //     return sender.send(msg);
                // })
                .mapEmpty();
    }

    @Override
    public Collection<String> names() {
        return this.container.keySet();
    }

    @Override
    public Collection<CommandExecutor> list() {
        return this.container.values();
    }

}
