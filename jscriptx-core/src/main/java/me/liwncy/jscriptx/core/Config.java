package me.liwncy.jscriptx.core;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.liwncy.jscriptx.core.constant.FileConstants;

/**
 * jscriptx 配置
 *
 * @author liwncy 
 */
@Data
@Slf4j
public class Config {

    private JConfig base = new JConfig();
    // private LoginConfig login = new LoginConfig();
    // private CommandConfig command = new CommandConfig();
    // private JsonObject adapter = new JsonObject();


    public static @Data class JConfig {
        private Boolean debug = false;
        private JsonObject debugConfig = new JsonObject();
        // private Boolean saveMedia = false;
        // private String owner = "";
        private String redis = "";
    }

    // public static @Data class LoginConfig {
    //     private Boolean autoLogin = true;
    //     private Boolean printQrcode = true;
    //     private Integer retryCount = 3;
    //     private Boolean encryptLoginInfo = true;
    //     private String encryptKey = "jbot";
    // }
    //
    // public static @Data class CommandConfig {
    //     private Boolean showTip = true;
    //     private String noPermissionTip = "你没有权限执行此命令";
    // }

    public static Future<Config> load() {
        var fs = Context.vertx.fileSystem();
        var exists = fs.existsBlocking(FileConstants.CONFIG_FILE.getPath());
        if (!exists) {
            saveDefault().await();
        }
        return Future.future(p -> fs.readFile(FileConstants.CONFIG_FILE.getPath())
                .onFailure(p::fail)
                .onSuccess(buffer -> {
                    log.info("读取配置文件成功");
                    var config = buffer.toJsonObject().mapTo(Config.class);
                    log.trace(config.toString());
                    Context.get().setConfig(config);
                    p.complete(config);
                })
        );
    }

    private static Future<Void> saveDefault() {
        log.debug("配置文件不存在，输出默认配置文件");
        var fs = Context.vertx.fileSystem();
        // 输出默认配置文件
        return fs.mkdirs(FileConstants.CONFIG_DIR.getPath())
                .compose(v -> fs.readFile(FileConstants.CONFIG_FILE.getName()))
                .compose(buffer -> fs.writeFile(FileConstants.CONFIG_FILE.getPath(), buffer))
                .onFailure(t -> log.warn("输出默认配置文件时出现异常：{}", t.getMessage()))
                .onSuccess(v -> log.debug("输出默认配置文件成功"));
    }

}
