# Code Generator
此工具主要用于将Excel模板文档转换为数据库结构、初始数据的sql及多语言资源代码。

## 主要功能
* 生成Mysql/SQLServer/SQLite的数据库结构代码（ddl）
* 生成初始数据的sql代码（dml）
* 生成js/json/java格式的国际化资源代码

## 参数说明
```
用法：
cg -type 生成类型 -lang 生成语言 -in 输入文件 [-sheets 要生成的Sheet名1[,2]] [-out 输出目录]

选项：
   -type: ddl_mysql|ddl_sqlserver|ddl_sqlite|dml|msg
   -lang: java|sql|js|json

默认值：
   -type: 无
   -lang: java (type为 ddl/dml时默认值为sql)
     -in: 无
 -sheets: 所有
    -out: ./out
```

## 用法示例
```sh
java -jar cg.jar -type ddl_mysql -lang sql -in xxx.xlsx -out ./out --type mysql

```

> 详细请参考示例 [example](example)

## License

Copyright (c) Copyright 2015-2019 yanglb.com. All rights reserved.

Licensed under the [Apache License 2.0](LICENSE) license.
