## 简介
easy 脚手架，快速生成spring-boot或spirng-boot-web项目。

## 背景和目标
开发新项目就需要搭建新工程，往往我们会从其他已有的项目中复制粘贴配置，经常不可避免的因为一些细节配置而踩坑，尤其对新人来说在对公司项目结构、基础中间件、配置、jar包版本还不了解的情况下，会浪费很多不必要的时间。基于这些痛点，希望能够达到以下目标：
* 统一规范项目结构、通用配置，约定大于配置。
* 提升工作效率，集成公司基础中间件、基础util、代码生成工具等快速开发组件，并做到开箱即用。
* 降低项目维护成本，易于组内成员相互协助时能快速上手。
* 统一jar包管理，组内成员不用关心通用jar的版本，组内共同维护一个通用 `parent pom`，在jar包需要升级时，统一协调升级。

## 特性
* 常用600+依赖jar包父pom统一管理，项目中无需维护jar包版本([见父pom文档](https://github.com/xiongzhao1217/easy-framework-parent))
* 最佳实践的项目结构（[查看项目结构图](https://github.com/xiongzhao1217/easy-framework-archetype/blob/master/README.md#%E5%A6%82%E4%BD%95%E6%90%AD%E5%BB%BA%E8%84%9A%E6%89%8B%E6%9E%B6)）
* 统一响应结果封装及生成工具（[见文档]()）
* 统一异常处理(web项目)
* 生成代码简洁优雅、扩展性强、利于维护
* 常用基础方法抽象封装，见service层基础方法
* 集成MyBatis plus、PageHelper分页插件，实现单表业务零SQL
* 编码风格统一、表格增减字段对代码的改动量非常小，快速、简单
* 支持生成lombok风格的代码，大大减少代码量和提升简洁性

## 快速开始
### 命令方式
输入以下命令，根据提示输入 `groupId` 和 `artifactId`，其他选项直接按 `Enter` 跳过。<br>
注意: 本地 `maven setting.xml` 文件需要提前配置好 `maven` 私服地址。
```
// spring-boot-web项目,若希望从本地仓库生成项目,可添加-DarchetypeCatalog=internal选项,禁止从远程仓库获取.
mvn archetype:generate -DarchetypeGroupId=com.easy.archetype -DarchetypeArtifactId=spring-boot-web-archetype -DarchetypeVersion=1.0-SNAPSHOT

// spring-boot项目
mvn archetype:generate -DarchetypeGroupId=com.easy.archetype -DarchetypeArtifactId=spring-boot-archetype -DarchetypeVersion=1.0-SNAPSHOT
```

### idea界面方式
1. 依次点击 `File -> New -> Project`，按照下图新增脚手架，填写脚手架 `GroupId、ArtifactId、Version`  ，`Repository` 填写 `maven` 私服地址。
![image.png](https://upload-images.jianshu.io/upload_images/17337828-388b16a32cc010de.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2. 选中第一步新增的脚手架，点击 `Next` 填写自己项目的`GroupId、ArtifactId、Version`，一直点击 `Next` 完成项目生成。
![image.png](https://upload-images.jianshu.io/upload_images/17337828-a5f15ef174438973.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


# 如何搭建脚手架
要搭建脚手架，首先我们需要一个模板工程，这个模板工程会集成一些工具类，通用配置，公司中间件，代码自动生成工具等，并且要有良好的分层结构等，达到开箱即用的标准，以下是按照阿里java开发规范制定的项目结构。
```
.
├── example-common
│   ├── pom.xml
│   └── src
├── example-dao
│   ├── pom.xml
│   └── src
├── example-domain
│   ├── pom.xml
│   └── src
├── example-manager
│   ├── pom.xml
│   └── src
├── example-message
│   ├── pom.xml
│   └── src
├── example-service
│   ├── pom.xml
│   └── src
├── example-web
│   ├── pom.xml
│   └── src
├── README.md
├── .gitignore
└── pom.xml
```
定义脚手架根 `pom` 的 `maven` 坐标
```xml
<!-- 继承团队小组的公共pom，包含常见三方包依赖、公司中间件、常用maven插件等 -->
<parent>
    <groupId>com.company.framework</groupId>
    <artifactId>dependencies-parent</artifactId>
    <version>1.0.0</version>
</parent>
<groupId>com.company</groupId>
<artifactId>example</artifactId>
<version>1.0-SNAPSHOT</version>
```
在父 `pom` 中添加以下插件
```xml
<build>
    <pluginManagement>
        <plugins>
            <!-- 脚手架插件 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-archetype-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <configuration>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <!-- 一个命令更新版本，mvn versions:set -DnewVersion=1.2-SNAPSHOT -->
            <plugin>
		<groupId>org.codehaus.mojo</groupId>
		<artifactId>versions-maven-plugin</artifactId>
		<configuration>
		    <generateBackupPoms>false</generateBackupPoms>
		</configuration>
	    </plugin>
        </plugins>
    </pluginManagement>
</build>
```

重命名 `.gitignore` 文件为 `__gitignore__`，并在模板工程根目录下创建 `archetype.properties` 文件，添加以下内容
```
# 排除打包到脚手架中的文件
excludePatterns=archetype.properties,*.iml,.idea/,logs/,build.sh
# maven脚手架会丢弃.gitignore文件，需要重命名为__gitignore__，脚手架会把__XX__的文件按照配置进行替换
gitignore=.gitignore
```
完成之后在根目录下执行
```
mvn archetype:create-from-project -Darchetype.properties=archetype.properties
```
执行完成后，会发现在 `./target/generated-sources/archetype` 目录下生成了脚手架工程，生成的脚手架工程可以当成是一个独立的项目，目录结构如下
```
.
├── pom.xml
├── src
│   ├── main
│   │   └── resources
│   │       ├── META-INF
│   │       │   └── maven
│   │       │       └── archetype-metadata.xml
│   │       └── archetype-resources
│   │           ├── __rootArtifactId__-common
│   │           │   ├── pom.xml
│   │           │   └── src
│   │           ├── __rootArtifactId__-dao
│   │           │   ├── pom.xml
│   │           │   └── src
│   │           ├── __rootArtifactId__-domain
│   │           │   ├── pom.xml
│   │           │   └── src
│   │           ├── __rootArtifactId__-manager
│   │           │   ├── pom.xml
│   │           │   └── src
│   │           ├── __rootArtifactId__-message
│   │           │   ├── pom.xml
│   │           │   └── src
│   │           ├── __rootArtifactId__-service
│   │           │   ├── pom.xml
│   │           │   └── src
│   │           ├── __rootArtifactId__-web
│   │           │   ├── pom.xml
│   │           │   └── src
│   │           ├── __gitignore__
│   │           ├── README.md
│   │           └── pom.xml
│   └── test
│       └── resources
│           └── projects
│               └── basic
│                   ├── archetype.properties
│                   └── goal.txt
```
在 `./target/generated-sources/archetype` 目录下执行 `mvn install` 就完成了脚手架项目的本地安装，如果需要推送到公司私服，需要在脚手架工程 `pom` 中添加如下内容，并执行 `mvn deploy` 完成私服推送。
```
<!-- id libs-snapshots-local 与 libs-releases-local 需要与本地 maven setting.xml文件中配置的保持一致 -->
<distributionManagement>
    <snapshotRepository>
        <id>libs-snapshots-local</id>
	<name>libs-snapshots</name>
	<url>你的私服快照仓库地址</url>
    </snapshotRepository>
    <repository>
        <id>libs-releases-local</id>
	<name>libs-releases</name>
	<url>你的私服releases仓库地址</url>
    </repository>
</distributionManagement>
```

3. 生成项目结构如下，至此，代码框架已经生成，可以进行业务代码的编写了。
![image.png](https://upload-images.jianshu.io/upload_images/17337828-65920342b76b6057.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
