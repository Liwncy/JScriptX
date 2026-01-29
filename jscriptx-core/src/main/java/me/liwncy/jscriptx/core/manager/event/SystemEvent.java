package me.liwncy.jscriptx.core.manager.event;

/**
 * 系统事件
 *
 * @author liwncy 
 */
public class SystemEvent extends Event<String> {
    public SystemEvent(String data) {
        super(data);
    }

    public static SystemEvent of(String data) {
        return new SystemEvent(data);
    }
}
