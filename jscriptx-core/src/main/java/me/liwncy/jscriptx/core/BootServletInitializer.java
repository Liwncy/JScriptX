package me.liwncy.jscriptx.core;

import io.vertx.core.Future;

public interface BootServletInitializer {

    Future<Void> start();

    Future<Void> stop();

    Future<Void> initManager();

    Future<Void> mkdir();
}
