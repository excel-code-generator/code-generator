/**
 * Copyright 2015-2023 yanglb.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanglb.codegen.core.translator.impl;

import com.yanglb.codegen.core.translator.BaseDdlTranslator;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.model.*;
import com.yanglb.codegen.utils.Conf;
import com.yanglb.codegen.utils.Infos;
import com.yanglb.codegen.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * MySQL 的DDL生成翻译器（单文件）
 *
 * @author yanglibing
 */
public class DdlMysqlTranslatorImpl extends BaseDdlTranslator {

    public DdlMysqlTranslatorImpl() {
        this.sqlColumnStart = "`";
        this.sqlColumnEnd = "`";
    }

    @Override
    protected void onTranslate(WritableModel writableModel) throws CodeGenException {
        super.onTranslate(writableModel);
        StringBuilder sb = writableModel.getData();

        // 添加文件头
        sb.append(Infos.sqlHeader());
        sb.append("\n");

        // 逐个添加内容
        for (DdlModel itm : this.model) {
            sb.append(this.genDdl(itm));
        }

        // 添加外键
        sb.append(this.genForeignKey());
    }

    /**
     * 生成MySQL的DDL
     *
     * @param model 原始数据
     * @return 生成结果
     * @throws CodeGenException 出错信息
     */
    public String genDdl(DdlModel model) throws CodeGenException {
        StringBuilder sb = new StringBuilder();

        // 主键、约束、索引等
        String primaryKey = "";
        HashMap<String, List<DdlDetail>> indexMap = new HashMap<String, List<DdlDetail>>();
        HashMap<String, List<DdlDetail>> uniqueMap = new HashMap<String, List<DdlDetail>>();

        // 　表名
        String tableName = this.genFullTableName(model);
        sb.append(String.format("-- %s \n", model.getSheetName()));
        sb.append(String.format("-- version %s \n", model.getVersion()));
        sb.append(String.format("CREATE TABLE %s (\n", tableName));

        Integer autoIncrement = null;
        for (DdlDetail detail : model.getDetail()) {
            // 主键
            if (detail.isColKey()) {
                if (!StringUtil.isNullOrEmpty(primaryKey)) {
                    primaryKey += ", ";
                }
                primaryKey += String.format("%s%s%s", this.sqlColumnStart,
                        detail.getColName(), this.sqlColumnEnd);
            }

            // 自增初始值
            if (detail.getColAutoIncrement() != null)
                autoIncrement = detail.getColAutoIncrement();

            // 索引
            this.updateIndexUniqueMap(indexMap, detail.getColIndex(), detail);

            // 约束
            this.updateIndexUniqueMap(uniqueMap, detail.getColUnique(), detail);

            // 列明细
            sb.append(this.genDdlDetail(detail));
        }

        // 主键
        if (!StringUtil.isNullOrEmpty(primaryKey)) {
            sb.append(String.format("    PRIMARY KEY (%s),\n", primaryKey));
        }

        // 索引
        if (!indexMap.isEmpty()) {
            for (List<DdlDetail> list : indexMap.values()) {
                sb.append(String.format("    %s,\n", this.indexUniqueSql(list, true)));
            }
        }

        // 约束
        if (!uniqueMap.isEmpty()) {
            for (List<DdlDetail> list : uniqueMap.values()) {
                sb.append(String.format("    %s,\n", this.indexUniqueSql(list, false)));
            }
        }

        // 删除最后一个 ,号
        sb.deleteCharAt(sb.lastIndexOf(","));

        // 引擎、字符集等其它信息
        StringBuilder info = new StringBuilder();
        info.append(String.format("ENGINE=%s ", engine()));
        info.append(String.format("DEFAULT CHARSET=%s ", charset()));
        if (autoIncrement != null) {
            info.append(String.format("AUTO_INCREMENT=%d ", autoIncrement));
        }

        // 注释
        info.append(String.format("COMMENT='%s'", model.getSheetName()));

        // 结束
        sb.append(String.format(") %s;\n\n", info.toString().trim()));

        return sb.toString();
    }

    private String engine() {
        return parameterModel.getOptions().getOptionValue("engine", Conf.getSetting("mysql.engine"));
    }

    private String charset() {
        return parameterModel.getOptions().getOptionValue("charset", Conf.getSetting("mysql.charset"));
    }

    private void updateIndexUniqueMap(HashMap<String, List<DdlDetail>> map, String names, DdlDetail detail) {
        if (!StringUtil.isNullOrEmpty(names)) {
            String[] keys = names.split(",");
            for (String key : keys) {
                key = key.trim();
                List<DdlDetail> value = map.computeIfAbsent(key, k -> new ArrayList<DdlDetail>());
                value.add(detail);
            }
        }
    }

    private String indexUniqueSql(List<DdlDetail> list, boolean isIndex) {
        StringBuilder name = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for (DdlDetail detail : list) {
            name.append(detail.getColName()).append("_");

            sb.append(String.format("%s%s%s ASC, ", this.sqlColumnStart,
                    detail.getColName(), this.sqlColumnEnd));
        }

        // 名字加后缀
        name.append(isIndex ? "INDEX" : "UNIQUE");
        sb.deleteCharAt(sb.lastIndexOf(","));

        return String.format("%s %s(%s)", isIndex ? "INDEX" : "UNIQUE INDEX", name.toString(), sb.toString());
    }

    private String genDdlDetail(DdlDetail detail) {
        StringBuilder sb = new StringBuilder();
        String type = detail.getColType();
        if (detail.getColLength() != null) {
            if (detail.getColPrecision() != null) {
                // 长度及精度都有 NUMBER(10,2)
                type = String.format("%s(%d, %d)", type, detail.getColLength(), detail.getColPrecision());
            } else {
                // 只有长度 VARCHAR2(10)
                type = String.format("%s(%d)", type, detail.getColLength());
            }
//        } else {
//            // 暂时忽略这种情况
//            if (detail.getColPrecision() == null) {
//                // 长度及精度都没有 VARCHAR
//            } else {
//                // 只有精度 NUMBER(2,2) ? 错误？
//            }
        }
        sb.append(String.format("    %s%s%s %s", this.sqlColumnStart,
                detail.getColName(), this.sqlColumnEnd, type));

        if (!detail.isColNullable()) {
            sb.append(" NOT NULL");
        }

        // 自增长列
        if (detail.getColAutoIncrement() != null) {
            sb.append(" AUTO_INCREMENT");
        }

        // 默认值、只有text/char 需要加引号
        if (!StringUtil.isNullOrEmpty(detail.getColDefault())) {
            String colType = detail.getColType().toLowerCase();
            if (colType.contains("text") || colType.contains("char")) {
                sb.append(String.format(" DEFAULT '%s'", detail.getColDefault()));
            } else {
                String def = detail.getColDefault();
                if (!StringUtil.isNullOrEmpty(def) && def.toUpperCase().startsWith("ON ")) {
                    sb.append(" ").append(detail.getColDefault());
                } else {
                    sb.append(String.format(" DEFAULT %s", detail.getColDefault()));
                }
            }
        }

        // 注释
        if (!StringUtil.isNullOrEmpty(detail.getFieldName())) {
            String name = detail.getFieldName();
            name = name.replaceAll("'", "");
            name = name.replaceAll("\r", "");
            name = name.replaceAll("\n", "");
            sb.append(String.format(" COMMENT '%s'", name));
        }

        sb.append(",\n");
        return sb.toString();
    }

    /**
     * 添加外键
     */
    private String genForeignKey() throws CodeGenException {
        StringBuilder sb = new StringBuilder();
        if (!this.foreignKeyList.isEmpty()) {
            sb.append("\n");
            sb.append("-- -------------------------------\n");
            sb.append("-- foreign key list\n");
            sb.append("-- -------------------------------\n");
        }

        // 所有表的外键放在最后处理
        for (ForeignModel model : this.foreignKeyList) {
            // 取得主、外键的列名
            String columnName = "", referenceColumnName = "";

            for (ForeignDetailModel foreignColumns : model.getForeignColumns()) {
                if (!StringUtil.isNullOrEmpty(columnName)) {
                    columnName += ", ";
                }
                if (!StringUtil.isNullOrEmpty(referenceColumnName)) {
                    referenceColumnName += ", ";
                }

                // 外键列，如：[AddressBizId], [AddressRev]
                columnName += String.format("%s%s%s", this.sqlColumnStart,
                        foreignColumns.getDdlDetail().getColName(),
                        this.sqlColumnEnd);

                // 主键列，如：[BizId], [Rev]
                referenceColumnName += String.format("%s%s%s",
                        this.sqlColumnStart, foreignColumns
                                .getForeignDdlDetail().getColName(),
                        this.sqlColumnEnd);
            }

            // 表名
            String tableName = genFullTableName(model.getDdlModel());
            String referenceTableName = genFullTableName(model
                    .getForeignColumns().get(0).getForeignDdlModel());
            sb.append(String.format("ALTER TABLE %s ADD FOREIGN KEY(%s) \n"
                            + "REFERENCES  %s (%s) \n"
                            + "ON DELETE CASCADE ON UPDATE CASCADE; \n\n",
                    tableName, columnName, referenceTableName,
                    referenceColumnName));
        }
        return sb.toString();
    }

}
