package me.liwncy.jscriptx.core.manager;

import io.vertx.core.Future;

/**
 * 管理器生命周期
 *
 * @author liwncy 
 */
public interface ManagerLifeCycle {

    default Future<Void> onInit() throws Exception {
        return Future.succeededFuture();
    }

    default Future<Void> onDestroy() throws Exception {
        return Future.succeededFuture();
    }
}
