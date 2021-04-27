# Code Generator
此工具主要用于将[Excel模板](template)文档转换为数据库结构、初始数据的sql脚本及多语言资源代码。

## 主要功能
* DDL - 生成数据库结构代码，支持 Mysql/SQLServer/SQLite
* DML - 生成初始数据的sql代码
* MSG - 生成多语言资源代码，支持 Android/IOS/JSON/Java/.NET

## 安装
**Mac/Linux 用户**
```sh
#!/usr/bin/env sh
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/excel-code-generator/code-generator/master/install.sh)"
```
> 将以上命令粘贴至终端。

**Windows 用户**
```ps1
# PowerShell
iex ((New-Object System.Net.WebClient).DownloadString('https://raw.githubusercontent.com/excel-code-generator/code-generator/master/install.ps1'))
```
> 将以上命令粘贴至PowerShell。

**更新**
```sh
cg update
```
> Windows/Mac/Linux 均相同。

## 在Docker中使用
```sh
docker pull yanglibing/code-generator
docker run --rm -it yanglibing/code-generator
```

## 参数说明
```
用法：cg command file [options]
Commands: 
 update                 检查并更新到最新版本
 ddl.mssql              生成SqlServer数据库结构SQL脚本（.ddl）
 ddl.mysql              生成MySql数据库结构SQL脚本（.ddl）
 ddl.sqlite             生成SQLite数据库结构SQL脚本（.ddl）
 dml                    生成数据库初始 数据SQL脚本（.dml）
 msg.android            生成Android国际化资源文件（strings.xml）
 msg.ios                生成IOS国际化资源文件（.strings）
 msg.json               生成JSON国际化资源文件（.json）
 msg.prop               生成Java国际化资源文件（.properties）
 msg.resx               生成.NET国际化资源文件（.resx）

Options:
 -fn,--file-name <fn>   生成的文件名，默认为Excel名，可为空值。
 -h,--help              显示帮助信息
 -o,--out <dir>         输出目录，默认输出到out目录
 -s,--sheets <names>    要处理的Excel Sheet名，默认除"#"开头外的全部Sheet
 -v,--version           打印版本信息

示例: 
 cg msg.json 001.xlsx
 cg msg.resx 001.xlsx --sheets Sheet1 Sheet2 Sheet5

帮助: 
 cg ddl.mysql --help    显示数生成据库结构的更多帮助信息
 cg dml --help          显示生成初始数据的更多帮助信息
 cg msg.json --help     显示生成国际化资源的更多帮助信息

通过 cg command --help 查看指定命令的详细说明。

---
Code Generator v4.1.1
By https://yanglb.com
```

## 用法示例
```sh
# 生成 MySql 数据库结构脚本
cg ddl.mysql database.xlsx --engine myisam

# 生成 JSON 多语言资源
cg msg.json msg.xlsx
```
> Excel模板请参考 [template](template)

## 升级说明
4.x版本不兼容老版本的命令行参数，但Excel模板在所有版本下均可正常使用。

## License

Copyright (c) 2015-2020 yanglb.com. All rights reserved.

Licensed under the [Apache License 2.0](LICENSE) license.
