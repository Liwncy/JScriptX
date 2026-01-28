package me.liwncy.jscriptx.core.manager.plugin;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class PluginConfig implements Serializable {

    /** 是否启用 */
    private Boolean enabled;
    /** 优先级 */
    private Integer priority;
    /** 执行下一个 */
    private Boolean next;

    public Map<String, Object> toMap() {
        return Map.of(
                "enabled", enabled,
                "priority", priority,
                "next", next
        );
    }


}
