# JScriptX

**插件式游戏脚本框架** - 基于Java和GraalVM的高性能游戏自动化解决方案

## 📋 项目简介

JScriptX是一个现代化的插件式游戏脚本框架，旨在提供灵活、高效、可扩展的游戏自动化能力。通过插件化架构，开发者可以快速构建和部署各种游戏脚本，实现自动化操作、辅助功能等。

## ✨ 核心特性

- **插件化架构**：模块化设计，支持热插拔插件
- **多语言支持**：基于GraalVM，支持JavaScript等脚本语言
- **高性能**：使用Vert.x作为异步框架，提供高性能事件驱动架构
- **丰富的API**：提供游戏自动化所需的核心API
- **配置灵活**：支持JSON配置文件，易于定制
- **完善的日志**：集成SLF4J和Logback，提供详细的日志记录
- **命令系统**：内置命令行系统，支持插件命令和系统命令
- **事件驱动**：基于Vert.x的事件总线，实现组件间通信

## 🛠️ 技术栈

- **核心框架**：Java 17, Vert.x 5.0.0
- **脚本引擎**：GraalVM Polyglot
- **命令行**：Picocli
- **工具库**：Hutool
- **JSON处理**：Jackson
- **日志系统**：SLF4J + Logback
- **构建工具**：Maven

## 📁 项目结构

```
JScriptX/
├── jscriptx-boot/        # 启动模块
│   ├── src/main/java/    # 源代码
│   └── target/           # 构建输出
├── jscriptx-core/        # 核心引擎
│   ├── src/main/java/    # 源代码
│   └── target/           # 构建输出
├── jscriptx-plugins/      # 插件模块
│   ├── jscriptx-plugin-helloword/  # 示例插件
│   └── target/           # 构建输出
├── plugins/              # 插件目录
├── logs/                 # 日志目录
├── config/               # 配置目录
└── README.md             # 项目说明
```

- **jscriptx-boot**：包含启动类和初始化逻辑
- **jscriptx-core**：提供核心API和基础功能
- **jscriptx-plugins**：插件管理和插件实现
- **plugins/**：存放插件文件的目录
- **logs/**：日志文件输出目录
- **config/**：配置文件目录

## 🚀 快速开始

### 环境要求

- JDK 17 或更高版本
- Maven 3.8.1 或更高版本

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/Liwncy/JScriptX.git
   cd JScriptX
   ```

2. **构建项目**
   ```bash
   mvn clean package
   ```

3. **运行项目**
   ```bash
   java -jar jscriptx-boot/target/jscriptx-boot-0.0.1-jar-with-dependencies.jar
   ```

## ⚙️ 配置说明

### 配置文件

项目使用 `config.json5` 作为配置文件，位于 `config` 目录下。主要配置项包括：

- **插件配置**：管理插件的启用状态和参数
- **系统配置**：框架的基本设置
- **日志配置**：日志级别和输出设置

### 插件管理

1. **插件目录**：默认位于 `plugins` 目录
2. **插件格式**：支持 `.jar` 和 `.zip` 格式的插件包
3. **插件配置**：在 `config.json5` 中配置插件参数
4. **插件加载**：启动时自动加载插件目录中的插件

## 📖 使用指南

### 命令系统

JScriptX内置了命令行系统，支持通过控制台输入命令来操作框架：

#### 命令格式
```
/命令 参数1 参数2 ...
```

#### 内置命令
- **/plugin-manager** (别名：/pm)：插件管理命令
  - `enable <插件名>`：启用插件
  - `disable <插件名>`：禁用插件
  - `load <插件名>`：加载插件
  - `unload <插件名>`：卸载插件
  - `reload <插件名>`：重载插件
  - `list`：列出所有插件
  - `info <插件名>`：查看插件信息

#### 示例
```bash
# 列出所有插件
/plugin-manager list

# 启用插件
/plugin-manager enable helloword

# 查看插件信息
/plugin-manager info helloword
```

### 编写插件

1. **创建插件项目**：新建Maven项目，添加JScriptX核心依赖
2. **实现插件接口**：继承 `Plugin` 接口，实现必要的方法
3. **配置插件**：创建 `plugin.json` 描述文件
4. **打包插件**：构建为jar文件，放入plugins目录

### 插件结构

```
my-plugin/
├── src/main/java/
│   └── com/example/MyPlugin.java  # 插件主类
├── src/main/resources/
│   └── plugin.json                # 插件描述文件
└── pom.xml                        # Maven配置
```

### 插件示例

#### Java插件示例
```java
public class HelloWordPlugin extends Plugin {
    @Override
    public void onLoad() {
        this.saveDefaultConfig();
    }

    @Override
    public EventListener<?, ?> getEventListener() {
        return new HelloWordListener(this);
    }

    @Override
    public CommandExecutor getCommandExecutor() {
        return new HelloWordExecutor(this);
    }
}
```

#### 命令执行器示例
```java
@CommandLine.Command(name = "hello", aliases = {"hi"}, description = "Hello World 命令")
public class HelloWordExecutor extends CommandExecutor implements Callable<String> {
    @Option(names = {"-n", "--name"}, description = "名称")
    private String name;

    public HelloWordExecutor(Plugin plugin) {
        super(plugin);
    }

    @Override
    public String call() {
        return "Hello, " + (name != null ? name : "World") + "!";
    }
}
```

## 🛡️ 错误处理

### 常见问题

1. **依赖缺失**：确保所有依赖都正确配置，特别是运行时依赖
2. **插件加载失败**：检查插件格式和依赖是否正确
3. **配置错误**：验证配置文件格式和内容
4. **命令执行失败**：检查命令格式和参数是否正确

### 日志查看

日志文件默认位于 `logs` 目录，可通过 `logback.xml` 配置日志级别和格式。

## 🔧 开发指南

### 开发环境搭建

1. **IDE推荐**：IntelliJ IDEA 或 Eclipse
2. **代码风格**：遵循Java标准代码规范
3. **提交规范**：使用清晰的提交信息，遵循GitFlow工作流

### 测试

```bash
mvn test
```

### 构建插件

1. **编译插件**
   ```bash
   mvn clean package -pl jscriptx-plugins/jscriptx-plugin-helloword -am
   ```

2. **安装插件**
   将生成的jar文件复制到 `plugins` 目录

## 🤝 贡献

欢迎各位开发者贡献代码、报告问题或提出建议！

1. **Fork 项目**
2. **创建分支**：`git checkout -b feature/AmazingFeature`
3. **提交更改**：`git commit -m 'Add some AmazingFeature'`
4. **推送到分支**：`git push origin feature/AmazingFeature`
5. **打开Pull Request**

## 📄 许可证

本项目采用 MIT 许可证 - 详情请参阅 [LICENSE](LICENSE) 文件

## 📞 联系方式

- **GitHub**：[https://github.com/liwncy/JScriptX](https://github.com/yourusername/JScriptX)
- **Email**：liwncy@qq.com

---

**享受编程的乐趣！** 🎮✨
