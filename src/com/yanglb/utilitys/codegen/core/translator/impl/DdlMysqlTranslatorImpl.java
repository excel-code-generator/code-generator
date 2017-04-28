/**
 * Copyright 2015 yanglb.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanglb.utilitys.codegen.core.translator.impl;

import java.io.File;

import com.yanglb.utilitys.codegen.core.model.DdlDetail;
import com.yanglb.utilitys.codegen.core.model.DdlModel;
import com.yanglb.utilitys.codegen.core.model.ForeignDetailModel;
import com.yanglb.utilitys.codegen.core.model.ForeignModel;
import com.yanglb.utilitys.codegen.core.translator.BaseDdlTranslator;
import com.yanglb.utilitys.codegen.exceptions.CodeGenException;
import com.yanglb.utilitys.codegen.support.SupportLang;
import com.yanglb.utilitys.codegen.utility.StringUtility;

/**
 * MySQL 的DDL生成翻译器（单文件）
 * @author yanglibing
 */
public class DdlMysqlTranslatorImpl extends BaseDdlTranslator {

	public DdlMysqlTranslatorImpl() {
		this.sqlColumnStart = "`";
		this.sqlColumnEnd = "`";
	}
	
	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();
		this.writableModel.setExtension("ddl");
		this.writableModel.setFilePath("ddl_mysql");
		this.writableModel.setLang(SupportLang.sql);
		
		// 设置文件名（同Excel名）
		String fileName = this.model.get(0).getExcelFileName();
		File file = new File(fileName);
		fileName = file.getName();
		
		int index = fileName.lastIndexOf(".");
		if(index != -1) {
			fileName = fileName.substring(0, index);
		}
		this.writableModel.setFileName(fileName);
	}

	@Override
	protected void onTranslate() throws CodeGenException {
		super.onTranslate();		
		StringBuilder sb = this.writableModel.getData();
		
		// 添加文件头
		sb.append(this.settingMap.get("head"));
		
		// 逐个添加内容
		for(DdlModel itm : this.model) {
			sb.append(this.genDdl(itm));
		}
		
		// 添加外键
		sb.append(this.genForeignKey());
	}

	/**
	 * 取得用于替换的Model
	 */
	@Override
	protected Object getReplaceModel() {
		if(this.model == null || this.model.size() <= 0) return null;
		
		// 只使用第一个Sheet替换
		return this.model.get(0);
	}

	/**
	 * 生成MySQL的DDL
	 * @param model 原始数据
	 * @return 生成结果
	 * @throws CodeGenException 出错信息
	 */
	public String genDdl(DdlModel model)
	throws CodeGenException{
		StringBuilder sb = new StringBuilder();
		
		String primaryKey = "";
		//　表名
		String tableName = this.genFullTableName(model);
		sb.append(String.format("-- %s \r\n", model.getSheetName()));
		sb.append(String.format("-- DROP TABLE %s; \r\n", tableName));
		sb.append(String.format("CREATE TABLE %s (\r\n", tableName));
		
		for(DdlDetail detail:model.getDetail()) {
			if(detail.isColKey()) {
				if(!StringUtility.isNullOrEmpty(primaryKey)) {
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
		if(!StringUtility.isNullOrEmpty(primaryKey)) {
			sb.append(String.format("    PRIMARY KEY (%s),\r\n", primaryKey));
		}
		
		// 删除最后一个 ,号
		sb.deleteCharAt(sb.lastIndexOf(","));		
		sb.append(") ENGINE={my_sql_engine} DEFAULT CHARSET={my_sql_defaultCharSet};\r\n");
		
		sb.append("\r\n");
		
		return sb.toString();
	}
	
	private String genDdlDetail(DdlDetail detail) {
		StringBuilder sb = new StringBuilder();
		String type = detail.getColType();
		if(detail.getColLength() != null) {
			if(detail.getColPrecision() != null) {
				// 长度及精度都有 NUMBER(10,2)
				type = String.format("%s(%d, %d)", type
						, detail.getColLength().intValue()
						, detail.getColPrecision().intValue());
			} else {
				// 只有长度 VARCHAR2(10)
				type = String.format("%s(%d)", type
						, detail.getColLength().intValue());
			}
		} else {
			// 暂时忽略这种情况
			if(detail.getColPrecision() == null) {
				// 长度及精度都没有 VARCHAR
			} else {
				// 只有精度 NUMBER(2,2) ? 错误？
			}
		}
		sb.append(String.format("    %s%s%s %s"
				,this.sqlColumnStart
				,detail.getColName()
				,this.sqlColumnEnd
				,type));
		
		if(!detail.isColNullable()) {
			sb.append(" NOT NULL");
		}
		
		// 自增长列
		if(detail.getColAutoIncrement() != null) {
			sb.append(" AUTO_INCREMENT");
		}
		
		// TODO: 默认值还有问题
		if(!StringUtility.isNullOrEmpty(detail.getColDefault())) {
			sb.append(String.format(" DEFAULT '%s'", detail.getColDefault()));
		}
		
	    sb.append(",\r\n");
		return sb.toString();
	}
	
	/**
	 * 添加外键
	 * @throws CodeGenException
	 */
	private String genForeignKey() throws CodeGenException {
		StringBuilder sb = new StringBuilder();
		if(this.foreignKeyList.size()>0) {
			sb.append("\r\n");
			sb.append("-- ----------------------------- --\r\n");
			sb.append("-- foreign key list\r\n");
			sb.append("-- ----------------------------- --\r\n");
		}
		
		// 所有表的外键放在最后处理
		for(ForeignModel model : this.foreignKeyList) {
			// 取得主、外键的列名
			String columnName="",referenceColumnName="";
			
			for(ForeignDetailModel foreignColumns:model.getForeignColumns()) {
				if(!StringUtility.isNullOrEmpty(columnName)) {
					columnName+=", ";
				}
				if(!StringUtility.isNullOrEmpty(referenceColumnName)) {
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
			String tableName = genFullTableName(model.getDdlModel());
			String referenceTableName = genFullTableName(model.getForeignColumns().get(0).getForeignDdlModel());
			sb.append(String.format("ALTER TABLE %s ADD FOREIGN KEY(%s) \r\n"
					+ "REFERENCES  %s (%s) \r\n"
					+ "ON DELETE CASCADE ON UPDATE CASCADE; \r\n\r\n",
					tableName,
					columnName,
					referenceTableName,
					referenceColumnName));
		}
		return sb.toString();
	}

}
