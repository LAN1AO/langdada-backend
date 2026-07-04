package com.langdada.backend;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.sql.Types;
import java.util.Collections;

/**
 * MyBatis-Plus 代码生成器
 * <p>
 * 基于 MyBatis-Plus 3.5.16 新 API，使用 FastAutoGenerator + Velocity 模板引擎，
 * 一键生成 entity、mapper、service、controller 等代码。
 * </p>
 * <p>
 * 参考文档：<a href="https://baomidou.com/guides/new-code-generator/">新代码生成器</a>
 * </p>
 *
 * @author langxiao
 */
public class CodeGenerator {

    // ==================== 数据库连接信息 ====================
    // remarks=true&useInformationSchema=true 用于读取 MySQL 表/字段注释
    private static final String URL = "jdbc:mysql://localhost:3306/langdada"
            + "?useSSL=false"
            + "&serverTimezone=Asia/Shanghai"
            + "&characterEncoding=utf8"
            + "&remarks=true"
            + "&useInformationSchema=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    // ==================== 项目信息 ====================
    private static final String AUTHOR = "langxiao";
    private static final String PARENT_PACKAGE = "com.langdada.backend";
    private static final String JAVA_OUTPUT_DIR = System.getProperty("user.dir") + "/src/main/java";
    private static final String XML_OUTPUT_DIR = System.getProperty("user.dir") + "/src/main/resources/mapper";

    /**
     * 需要生成代码的数据库表名（全部小写）
     */
    private static final String[] TABLES = {"user", "app", "question", "scoring_result", "user_answer"};

    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                // ==================== 1. 全局配置 ====================
                .globalConfig(builder -> builder
                        .author(AUTHOR)                         // 作者名，生成 @author 注释
                        .outputDir(JAVA_OUTPUT_DIR)             // Java 文件输出目录
                        .commentDate("yyyy-MM-dd")              // 注释日期格式
                )

                // ==================== 2. 数据源配置 ====================
                .dataSourceConfig(builder -> builder
                        .typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            // 修复 MySQL tinyint 类型映射：
                            // - tinyint(1) → Boolean（默认行为，符合预期）
                            // - tinyint(n>1) → Integer（而非 Byte）
                            if (typeCode == Types.TINYINT && metaInfo.getLength() > 1) {
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )

                // ==================== 3. 包配置 ====================
                .packageConfig(builder -> builder
                        .parent(PARENT_PACKAGE)                 // 父包名
                        .entity("model.entity")                 // entity 包
                        .mapper("mapper")                       // mapper 接口包
                        .service("service")                     // service 接口包
                        .serviceImpl("service.impl")            // service 实现类包
                        .controller("controller")               // controller 包
                        .xml("mapper.xml")                      // XML 包（仅影响包路径）
                        .pathInfo(Collections.singletonMap(
                                OutputFile.xml, XML_OUTPUT_DIR  // XML 实际输出目录
                        ))
                )

                // ==================== 4. 策略配置 ====================
                .strategyConfig(builder -> builder
                        // 指定要生成代码的表
                        .addInclude(TABLES)

                        // --- Entity 策略 ---
                        .entityBuilder()
                        .enableLombok()                         // 使用 Lombok 注解（@Data 等）
                        // .enableRemoveIsPrefix()                 // 去掉 isDelete → delete（可选）
                        // .logicDeleteColumnName("isDelete")   // 逻辑删除字段（配置在 application.yml 全局 db-config 中）
                        .enableTableFieldAnnotation()           // 生成 @TableField 注解

                        // --- Mapper 策略 ---
                        .mapperBuilder()
                        .enableBaseResultMap()                  // 生成 <resultMap>
                        .enableBaseColumnList()                 // 生成 <sql id="Base_Column_List">

                        // --- Service 策略 ---
                        .serviceBuilder()

                        // --- Controller 策略 ---
                        .controllerBuilder()
                        .enableRestStyle()                      // 生成 @RestController
                )

                // ==================== 5. 模板引擎 ====================
                .templateEngine(new VelocityTemplateEngine())

                // 执行生成
                .execute();

        printResult();
    }

    private static void printResult() {
        System.out.println("\n======================================");
        System.out.println("  MyBatis-Plus 代码生成完成！");
        System.out.println("======================================");
        System.out.println("  Java 文件: " + JAVA_OUTPUT_DIR);
        System.out.println("  XML  文件: " + XML_OUTPUT_DIR);
        System.out.println("  生成表  : " + String.join(", ", TABLES));
        System.out.println("======================================\n");
    }
}
