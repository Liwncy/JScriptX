package me.liwncy.jscriptx.core.exception;

import lombok.experimental.StandardException;

/**
 * 未知枚举异常
 *
 * @author liwncy 
 */
@StandardException
public class UnknownEnumException extends RuntimeException {

    public UnknownEnumException(String type, Object o) {
        super("未知的 [" + type + "] 类型: " + o);
    }

}
