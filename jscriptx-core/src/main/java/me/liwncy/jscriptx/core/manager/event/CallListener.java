package me.liwncy.jscriptx.core.manager.event;

import io.vertx.core.json.JsonObject;
import lombok.RequiredArgsConstructor;
import me.liwncy.jscriptx.core.Context;
import me.liwncy.jscriptx.core.manager.plugin.Plugin;

/**
 * 调用监听器
 *
 * @author liwncy
 */
@RequiredArgsConstructor
public abstract class CallListener {

    protected final Plugin plugin;

    public abstract JsonObject onCall(JsonObject data);

    public void register() {
        Context.vertx.eventBus().<JsonObject>consumer(this.plugin.getDescription().getName(), msg -> this.plugin.getVertx().executeBlocking(() -> {
            var data = msg.body();
            var res = this.onCall(data);
            if (res != null) {
                msg.reply(res);
            }
            return null;
        }));
    }

    public static JsonObject call(String name, JsonObject data) {
        return Context.vertx.eventBus().<JsonObject>request(name, data).await().body();
    }

}
