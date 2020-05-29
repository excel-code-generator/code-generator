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
package com.yanglb.codegen.core.translator.impl;

import java.io.File;
import java.util.Map;

import com.yanglb.codegen.core.model.DmlModel;
import com.yanglb.codegen.core.model.TableModel;
import com.yanglb.codegen.core.translator.BaseSqlTranslator;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.support.SupportLang;
import com.yanglb.codegen.utils.StringUtility;

public class DmlTranslatorImpl extends BaseSqlTranslator<DmlModel> {
	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();
		this.writableModel.setExtension("dml");
		this.writableModel.setFilePath("dml");
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

		// TODO: paramaModel
//		Map<String, String> options = this.paramaModel.getOptions();
//		if (options.containsKey("type")) {
//			String type = options.get("type");
//			if (type.equals("mysql")) {
//				this.sqlColumnEnd = this.sqlColumnStart = "`";
//			}
//		}
	}

	@Override
	protected void onTranslate() throws CodeGenException {
		super.onTranslate();		
		StringBuilder sb = this.writableModel.getData();
		
		// 添加文件头
		sb.append(this.settingMap.get("head"));
		
		// 逐个添加内容
		for(DmlModel itm : this.model) {
			sb.append(this.genDml(itm));
		}
	}

	private String genDml(DmlModel model) {
		StringBuilder sb = new StringBuilder();
		
		// 头部信息
		sb.append("\r\n");
		sb.append(String.format("-- %s\r\n", model.getSheetName()));
		sb.append(String.format("-- %s %s\r\n", model.getAuthor(), model.getRenewDate()));
		
		// 每一行dml语句
		String columns = "";
		String space = "";
		if(!StringUtility.isNullOrEmpty(model.getNameSpace()) && !"-".equals(model.getNameSpace())) {
			space = String.format("%s.", model.getNameSpace());
		}
		TableModel table = model.getDmlData();
		for(Map<String, String> itm : table.toList()) {
			// 如果模板为空则先生成模板
			if(StringUtility.isNullOrEmpty(columns)) {
				columns = this.genColumns(table.getColumns());
			}
			
			// 生成完整语句并添加到sb中
			sb.append(String.format("INSERT INTO %s%s(%s) VALUES (%s);\r\n", 
					space,
					model.getName(),
					columns, 
					this.genValues(table.getColumns(), itm)));
		}
		return sb.toString();
	}
	
	/**
	 * 生成dml的values部分
	 * @param data dml数据
	 * @return values
	 */
	private String genValues(String[] columns, Map<String, String> data) {
		String result = "";
		
		for(String col : columns) {
			if(!StringUtility.isNullOrEmpty(result)) {
				result += ", ";
			}
			String value = data.get(col);
			if(value == null) {
				result += "null";
			} else if(value.startsWith("#")) {
				// #开头表示为公式
				result += value.substring(1);
			} else if(value.startsWith("'") && value.endsWith("'")){
				// 如果已经使用 ' 引号则不添加'
				result += value;
			} else {
				result += String.format("'%s'", data.get(col));
			}
		}
		return result;
	}

	/**
	 * 生成dml的column部分
	 * @param data dml数据
	 * @return 'col1', 'col2'
	 */
	private String genColumns(String[] data) {
		String columns = "";
		
		for(String col : data) {
			if(!StringUtility.isNullOrEmpty(columns)) {
				columns += ", ";
			}
			columns += String.format("%s%s%s", this.sqlColumnStart, col, this.sqlColumnEnd);
		}
		return columns;
	}
}
