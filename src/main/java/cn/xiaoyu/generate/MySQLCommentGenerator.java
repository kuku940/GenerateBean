package cn.xiaoyu.generate;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

public class MySQLCommentGenerator extends EmptyCommentGenerator {
    private final Properties properties;

    public MySQLCommentGenerator() {
        properties = new Properties();
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        // 获取自定义的 properties
        this.properties.putAll(properties);
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String author = "01399268";
        String dateFormat = properties.getProperty("dateFormat", "yyyy-MM-dd");
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);

        // 获取表注释
        String remarks = introspectedTable.getRemarks();
        topLevelClass.addJavaDocLine("");
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + remarks);
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * @author " + author);
        topLevelClass.addJavaDocLine(" * @date " + dateFormatter.format(new Date()));
        topLevelClass.addJavaDocLine(" */");

        // 添加实体类的注解
        // 因为MBG没有给普通实体类生成注解，所以这里手动调用一下
        // BaseRecordGenerator类没有调用addClassAnnotation()
        addClassAnnotation(topLevelClass, introspectedTable, null);
    }

    @Override
    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {
        if (innerClass instanceof TopLevelClass) {
            TopLevelClass topLevelClass = (TopLevelClass) innerClass;
            topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.Data"));
            topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.Builder"));
            topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.NoArgsConstructor"));
            topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.AllArgsConstructor"));

            topLevelClass.addImportedType(new FullyQualifiedJavaType("com.baomidou.mybatisplus.annotation.TableField"));
            topLevelClass.addImportedType(new FullyQualifiedJavaType("com.baomidou.mybatisplus.annotation.TableName"));
            topLevelClass.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.ApiModel"));
            topLevelClass.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.ApiModelProperty"));
            topLevelClass.addAnnotation("@Data");
            topLevelClass.addAnnotation("@Builder");
            topLevelClass.addAnnotation("@TableName(\"" + introspectedTable.getTableConfiguration().getTableName() + "\")");
            topLevelClass.addAnnotation("@NoArgsConstructor");
            topLevelClass.addAnnotation("@AllArgsConstructor");
            topLevelClass.addAnnotation("@ApiModel");
        }
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        // 获取列注释
        String remarks = introspectedColumn.getRemarks();

        field.addJavaDocLine("@TableField(\"" + introspectedColumn.getActualColumnName() + "\")");
        field.addJavaDocLine("@ApiModelProperty(value = \"" + remarks + "\")");
    }
}
