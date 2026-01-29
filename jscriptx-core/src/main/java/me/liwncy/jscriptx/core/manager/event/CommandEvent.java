package me.liwncy.jscriptx.core.manager.event;


/**
 * 命令事件
 *
 * @author ovo created on 2025/02/25.
 */
public class CommandEvent<S> extends Event<String> {
    public CommandEvent(String data) {
        super(data);
    }

    public CommandEvent<S> of(String data) {
        return new CommandEvent<S>(data);
    }
}
