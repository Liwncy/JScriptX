package me.liwncy.jscriptx.core.manager.command;

import cn.hutool.core.util.StrUtil;
import lombok.Value;
import me.liwncy.jscriptx.core.Context;

/**
 * 命令
 *
 * @author ovo created on 2025/02/18.
 */
@Value
public class Command {

    /** 命令 */
    String command;
    /** 参数 */
    String[] args;

    /**
     * 通过文本消息创建一个命令
     *
     * @param message 消息
     * @return {@link Command }
     */
    // public static Command of(Message message) {
    //     return new Command(message);
    // }

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
