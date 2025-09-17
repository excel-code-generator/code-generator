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
import com.yanglb.codegen.model.DdlDetail;
import com.yanglb.codegen.model.DdlModel;
import com.yanglb.codegen.model.ForeignDetailModel;
import com.yanglb.codegen.model.ForeignModel;
import com.yanglb.codegen.utils.Infos;
import com.yanglb.codegen.utils.StringUtil;


public class DdlSqliteTranslatorImpl extends BaseDdlTranslator {

    public DdlSqliteTranslatorImpl() {
        this.sqlColumnStart = "\"";
        this.sqlColumnEnd = "\"";
    }

    @Override
    protected void onTranslate() throws CodeGenException {
        super.onTranslate();
        StringBuilder sb = this.writableModel.getData();

        // 添加文件头
        sb.append(Infos.sqlHeader());
        sb.append("\n");

        // 逐个添加内容
        for (DdlModel itm : this.model) {
            sb.append(this.genDdl(itm));
        }
    }

    /**
     * 生成MySQL的DDL
     *
     * @param model 原始数据
     * @return 生成结果
     * @throws CodeGenException 出错信息
     */
    public String genDdl(DdlModel model)
            throws CodeGenException {
        StringBuilder sb = new StringBuilder();
        String primaryKey = "";

        String tableName = this.genFullTableName(model);
        sb.append(String.format("-- %s \n", model.getSheetName()));
        sb.append(String.format("-- DROP TABLE %s; \n", tableName));
        sb.append(String.format("CREATE TABLE %s (\n", tableName));
        for (DdlDetail detail : model.getDetail()) {
            if (detail.isColKey()) {
                if (!StringUtil.isNullOrEmpty(primaryKey)) {
                    primaryKey += ", ";
                }
                primaryKey += String.format("%s%s%s",
                        this.sqlColumnStart,
                        detail.getColName(),
                        this.sqlColumnEnd);
            }

            sb.append(this.genDdlDetail(detail));
        }

        // 主键
        if (!StringUtil.isNullOrEmpty(primaryKey)) {
            sb.append(String.format("    PRIMARY KEY (%s),\n", primaryKey));
        }

        // 外键
        sb.append(this.genForeignKey(model));

        // 删除最后一个 ,号
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(");\n");

        sb.append("\n");

        return sb.toString();
    }

    private String genDdlDetail(DdlDetail detail) {
        StringBuilder sb = new StringBuilder();
        String type = detail.getColType();
        if (detail.getColLength() != null) {
            if (detail.getColPrecision() != null) {
                // 长度及精度都有 NUMBER(10,2)
                type = String.format("%s(%d, %d)", type
                        , detail.getColLength()
                        , detail.getColPrecision());
            } else {
                // 只有长度 VARCHAR2(10)
                type = String.format("%s(%d)", type
                        , detail.getColLength());
            }
        } else {
            // 暂时忽略这种情况
            if (detail.getColPrecision() == null) {
                // 长度及精度都没有 VARCHAR
            } else {
                // 只有精度 NUMBER(2,2) ? 错误？
            }
        }
        sb.append(String.format("    %s%s%s %s"
                , this.sqlColumnStart
                , detail.getColName()
                , this.sqlColumnEnd
                , type));

        if (!detail.isColNullable()) {
            sb.append(" NOT NULL");
        }

        // TODO: 默认值还有问题
        if (!StringUtil.isNullOrEmpty(detail.getColDefault())) {
            // 如果是函数类型的默认值则不添加引号
            if (detail.getColDefault().trim().endsWith(")")) {
                sb.append(String.format(" DEFAULT %s", detail.getColDefault()));
            } else {
                sb.append(String.format(" DEFAULT '%s'", detail.getColDefault()));
            }
        }

        sb.append(",\n");
        return sb.toString();
    }

    /**
     * 添加外键
     */
    private String genForeignKey(DdlModel currentTableModel) throws CodeGenException {
        StringBuilder sb = new StringBuilder();

        // 所有表的外键放在最后处理
        for (ForeignModel model : this.foreignKeyList) {
            // 只生成当前表的外键
            if (!currentTableModel.equals(model.getDdlModel())) {
                continue;
            }

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
                columnName += String.format("%s%s%s",
                        this.sqlColumnStart,
                        foreignColumns.getDdlDetail().getColName(),
                        this.sqlColumnEnd);

                // 主键列，如：[BizId], [Rev]
                referenceColumnName += String.format("%s%s%s",
                        this.sqlColumnStart,
                        foreignColumns.getForeignDdlDetail().getColName(),
                        this.sqlColumnEnd);
            }

            // 表名
            //String tableName = genFullTableName(model.getDdlModel());
            String referenceTableName = genFullTableName(model.getForeignColumns().get(0).getForeignDdlModel());
            sb.append(String.format("    FOREIGN KEY(%s) REFERENCES %s(%s),\n",
                    columnName,
                    referenceTableName,
                    referenceColumnName));
        }
        return sb.toString();
    }
}
