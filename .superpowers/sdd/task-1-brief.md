# Task 1: 后端父 POM 与目录结构

**Goal:** 创建后端 Maven 多模块结构，包含14个模块（父POM + 13个子模块）

**Files:**
- Create: `backend/pom.xml` - 父POM
- Create: `backend/common/pom.xml` - 公共模块
- Create: `backend/system-core/pom.xml` - 系统核心模块
- Create: `backend/bootstrap/pom.xml` - 启动模块
- Create: `backend/project-core/pom.xml` - 项目核心（占位）
- Create: `backend/member-core/pom.xml` - 成员管理（占位）
- Create: `backend/version-core/pom.xml` - 版本管理（占位）
- Create: `backend/template-core/pom.xml` - 模板市场（占位）
- Create: `backend/plugin-datasource/pom.xml` - 数据源插件（占位）
- Create: `backend/plugin-form/pom.xml` - 表单插件（占位）
- Create: `backend/plugin-bi/pom.xml` - BI插件（占位）
- Create: `backend/plugin-flow/pom.xml` - 流程插件（占位）
- Create: `backend/plugin-api/pom.xml` - 接口插件（占位）
- Create: `backend/sandbox-engine/pom.xml` - 沙箱引擎（占位）
- Create: `backend/code-generator/pom.xml` - 代码生成器（占位）

**父 POM (backend/pom.xml):**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.lc</groupId>
    <artifactId>lc-platform</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>
    <name>lc-platform</name>
    <description>Low Code Platform - Parent</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version>
        <relativePath/>
    </parent>

    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <mapstruct.version>1.5.5.Final</mapstruct.version>
        <jjwt.version>0.12.5</jjwt.version>
    </properties>

    <modules>
        <module>common</module>
        <module>system-core</module>
        <module>project-core</module>
        <module>member-core</module>
        <module>version-core</module>
        <module>template-core</module>
        <module>plugin-datasource</module>
        <module>plugin-form</module>
        <module>plugin-bi</module>
        <module>plugin-flow</module>
        <module>plugin-api</module>
        <module>sandbox-engine</module>
        <module>code-generator</module>
        <module>bootstrap</module>
    </modules>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>common</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>system-core</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>project-core</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>member-core</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>version-core</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>template-core</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>plugin-datasource</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>plugin-form</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>plugin-bi</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>plugin-flow</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>plugin-api</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>sandbox-engine</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>code-generator</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.lc</groupId>
                <artifactId>bootstrap</artifactId>
                <version>${project.version}</version>
            </dependency>

            <dependency>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct</artifactId>
                <version>${mapstruct.version}</version>
            </dependency>
            <dependency>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>${mapstruct.version}</version>
            </dependency>

            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-api</artifactId>
                <version>${jjwt.version}</version>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-impl</artifactId>
                <version>${jjwt.version}</version>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-jackson</artifactId>
                <version>${jjwt.version}</version>
                <scope>runtime</scope>
            </dependency>

            <dependency>
                <groupId>com.mysql</groupId>
                <artifactId>mysql-connector-j</artifactId>
                <version>8.3.0</version>
            </dependency>

            <dependency>
                <groupId>org.flywaydb</groupId>
                <artifactId>flyway-core</artifactId>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                    <encoding>UTF-8</encoding>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${mapstruct.version}</version>
                        </path>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

**common 模块 pom.xml (backend/common/pom.xml):**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.lc</groupId>
        <artifactId>lc-platform</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>common</artifactId>
    <packaging>jar</packaging>
    <name>common</name>
    <description>Common utilities and shared components</description>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
        </dependency>
    </dependencies>
</project>
```

**system-core 模块 pom.xml (backend/system-core/pom.xml):**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.lc</groupId>
        <artifactId>lc-platform</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>system-core</artifactId>
    <packaging>jar</packaging>
    <name>system-core</name>
    <description>System core module - tenant, user, role, permission, authentication</description>

    <dependencies>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>common</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
        </dependency>
    </dependencies>
</project>
```

**bootstrap 模块 pom.xml (backend/bootstrap/pom.xml):**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.lc</groupId>
        <artifactId>lc-platform</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>bootstrap</artifactId>
    <packaging>jar</packaging>
    <name>bootstrap</name>
    <description>Bootstrap module - Spring Boot application entry point</description>

    <dependencies>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>common</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>system-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>project-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>member-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>version-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>template-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>plugin-datasource</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>plugin-form</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>plugin-bi</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>plugin-flow</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>plugin-api</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>sandbox-engine</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>code-generator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

**占位模块 pom.xml 模板（project-core, member-core, version-core, template-core, plugin-datasource, plugin-form, plugin-bi, plugin-flow, plugin-api, sandbox-engine, code-generator）:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.lc</groupId>
        <artifactId>lc-platform</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>{module-name}</artifactId>
    <packaging>jar</packaging>
    <name>{module-name}</name>
    <description>{module-name} module (placeholder)</description>

    <dependencies>
        <dependency>
            <groupId>com.lc</groupId>
            <artifactId>common</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
</project>
```

**Steps:**
1. 创建父 POM
2. 创建 common 模块 pom.xml
3. 创建 system-core 模块 pom.xml
4. 创建 bootstrap 模块 pom.xml
5. 创建 11 个占位模块 pom.xml
6. 编译验证：`cd backend && mvn clean compile -q`
7. Commit

**Global Constraints:**
- Java: 17
- Spring Boot: 3.2.5
- Maven: 3.9.x

__tr_native_ec=$?; pwd -P >| '/var/log/tool/jobs/job-23bb1f25e8c84c08a78723ab4bfe64db/cwd.txt'; exit "$__tr_native_ec"