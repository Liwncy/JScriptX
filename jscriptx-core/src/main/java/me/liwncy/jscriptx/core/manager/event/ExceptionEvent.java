package me.liwncy.jscriptx.core.manager.event;

/**
 * 异常事件
 *
 * @author liwncy 
 */
public class ExceptionEvent extends Event<Throwable> {
    public ExceptionEvent(Throwable data) {
        super(data);
    }

    public static ExceptionEvent of(Throwable data) {
        return new ExceptionEvent(data);
    }
}
