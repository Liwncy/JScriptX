package me.liwncy.jscriptx.core.strategy;

import cn.hutool.core.util.StrUtil;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import me.liwncy.jscriptx.core.exception.FunctionException;

/**
 * 功能模块工厂接口
 *
 * @author Liwncy
 */
public interface FunctionServiceStrategy {

    String name();

    Future<Void> start();

    Future<Void> onInit(JsonObject config);

    Future<Void> onDestroy();

    default void init(JsonObject config) {
        this.onInit(config).onFailure(e -> {
            throw new FunctionException(StrUtil.format("功能[{}]初始化失败", name()), e);
        }).await();
    }

    default void destroy() {
        this.onDestroy().onFailure(e -> {
            throw new FunctionException(StrUtil.format("功能[{}]关闭失败", name()), e);
        }).await();
    }

}
