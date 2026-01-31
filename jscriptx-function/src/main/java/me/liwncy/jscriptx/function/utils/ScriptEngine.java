package me.liwncy.jscriptx.function.utils;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * 脚本执行引擎，支持 JavaScript 脚本执行
 */
public class ScriptEngine {
    private Context context;
    private Map<String, Object> bindings;

    public ScriptEngine() {
        init();
    }

    /**
     * 初始化脚本引擎
     */
    private void init() {
        context = Context.newBuilder("js")
                .allowAllAccess(true)
                .build();

        bindings = new HashMap<>();

        // 绑定工具类
        bindings.put("mouse", new MouseController());
        bindings.put("keyboard", new KeyboardController());
        bindings.put("screen", new ScreenRecognizer());
        bindings.put("color", new ColorRecognizer());
        bindings.put("window", WindowUtils.class);
        bindings.put("delay", Delay.class);
        bindings.put("timer", Timer.class);

        // 绑定到脚本上下文
        for (Map.Entry<String, Object> entry : bindings.entrySet()) {
            context.getBindings("js").putMember(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 执行 JavaScript 脚本
     *
     * @param script 脚本代码
     * @return 执行结果
     */
    public Object execute(String script) {
        try {
            Value result = context.eval("js", script);
            return result.isNull() ? null : result.as(Object.class);
        } catch (Exception e) {
            throw new RuntimeException("脚本执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 执行 JavaScript 文件
     *
     * @param filePath 文件路径
     * @return 执行结果
     */
    public Object executeFile(String filePath) {
       try {
           // 读取文件内容为字符串
           String script = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));
           Value result = context.eval("js", script);
           return result.isNull() ? null : result.as(Object.class);
       } catch (Exception e) {
           throw new RuntimeException("脚本文件执行失败: " + e.getMessage(), e);
       }
   }


    /**
     * 绑定变量到脚本上下文
     *
     * @param name  变量名
     * @param value 变量值
     */
    public void bind(String name, Object value) {
        bindings.put(name, value);
        context.getBindings("js").putMember(name, value);
    }

    /**
     * 获取绑定的变量
     *
     * @param name 变量名
     * @return 变量值
     */
    public Object getBinding(String name) {
        return bindings.get(name);
    }

    /**
     * 关闭脚本引擎
     */
    public void close() {
        if (context != null) {
            context.close();
        }
    }
}