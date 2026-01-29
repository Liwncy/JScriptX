package me.liwncy.jscriptx.core.manager.command;

import cn.hutool.core.util.StrUtil;
import lombok.Value;
import me.liwncy.jscriptx.core.Context;

/**
 * 命令
 *
 * @author liwncy 
 */
@Value
public class Command {

    /** 命令 */
    String command;
    /** 参数 */
    String[] args;

    /**
     * 通过控制台输入创建命令
     *
     * @param input 输入文本
     * @return {@link Command }
     */
    public static Command of(String input) {
        String command = StrUtil.subBefore(input, ' ', false);
        String argsPart = StrUtil.subAfter(input, ' ', false);
        String[] args = StrUtil.isBlank(argsPart) ? new String[0] : argsPart.split(" ");
        return new Command(command, args);
    }

    /**
     *
     * @param message 消息
     */
    // private Command(Message message) {
    //     this.message = message;
    //     this.command = StrUtil.subBefore(message.getContent(), ' ', false);
    //     this.args = StrUtil.subAfter(message.getContent(), ' ', false).split(" ");
    //     this.from = message.getSender().getType() == ContactType.GROUP ? message.getMember() : message.getSender();
    // }

    /**
     * 执行
     */
    public void execute() {
        Context.get().getCommandManager().execute(this);
    }

    /**
     * 检查消息是否是一条命令
     *
     * @param message 消息
     * @return boolean
     */
    public static boolean isCommand(String message) {
        return StrUtil.isNotBlank(message) && message.startsWith("/") && Context.get().getCommandManager().names().contains(StrUtil.subBefore(message, ' ', false));
    }

}
