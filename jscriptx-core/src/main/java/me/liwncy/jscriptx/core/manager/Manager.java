package me.liwncy.jscriptx.core.manager;

import io.vertx.core.Future;

/**
 * 管理器接口
 *
 * @author liwncy 
 */
public interface Manager {

    Future<Void> init();

    Future<Void> close();

}
