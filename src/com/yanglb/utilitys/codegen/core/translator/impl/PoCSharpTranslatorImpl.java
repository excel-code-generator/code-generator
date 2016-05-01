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

import java.util.Map;

import com.yanglb.utilitys.codegen.core.model.DdlDetail;
import com.yanglb.utilitys.codegen.core.model.DdlModel;
import com.yanglb.utilitys.codegen.core.translator.BaseTranslator;
import com.yanglb.utilitys.codegen.exceptions.CodeGenException;
import com.yanglb.utilitys.codegen.support.SupportLang;
import com.yanglb.utilitys.codegen.utility.StringUtility;

public class PoCSharpTranslatorImpl extends BaseTranslator<DdlModel> {
	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();
		this.writableModel.setExtension("cs");
		this.writableModel.setFilePath("po");
		this.writableModel.setLang(SupportLang.cs);
		
		// 设置文件名（同表名）
		this.writableModel.setFileName(this.model.getName());
	}

	@Override
	protected void onTranslate() throws CodeGenException {
		super.onTranslate();		
		StringBuilder sb = this.writableModel.getData();
		
		// 添加文件头（注释）
		sb.append(this.settingMap.get("head"));
		
		// 添加类名
		sb.append(this.genHead());
		
		// 添加内容
		for(DdlDetail detail : this.model.getDetail()) {
			sb.append(this.genProperty(detail));
		}
		
		// 添加结束符号
		sb.append("    }\r\n");
		sb.append("}\r\n");
	}
	
	/**
	 * 生成头部信息
	 * @return
	 */
	private String genHead() {
		StringBuilder sb = new StringBuilder();
		Map<String, String> opt = this.paramaModel.getOptions();
		
		// 引用
		if(opt.containsKey("using")) {
			String[] usings = opt.get("using").split("\\|");
			for(String using:usings) {
				sb.append("using "+ using +";\r\n");
			}
			sb.append("\r\n");
		}
		
		// 名空间
		String ns = this.settingMap.get("namespace");
		if(opt.containsKey("ns")) {
			ns = opt.get("ns");
		}
		sb.append("namespace "+ns+"\r\n{\r\n");
		
		// 类名
		if(!StringUtility.isNullOrEmpty(this.model.getDescription())) {
			sb.append("    /// <summary>\r\n");
			String[] descriptions = this.model.getDescription().split("\\n");
			for(String itm:descriptions) {
				sb.append("    /// " + itm.trim() + "\r\n");
			}
			sb.append("    /// </summary>\r\n");
		}
		sb.append("    public class " + this.model.getName() + "\r\n");
		sb.append("    {\r\n");
		
		// 默认构造函数
		sb.append("        public " + this.model.getName() + "()\r\n");
		sb.append("        {\r\n\r\n");
		sb.append("        }\r\n\r\n");
		
		return sb.toString();
	}
	
	/**
	 * 生成每一个属性
	 * @param detail 明细
	 * @return
	 */
	private String genProperty(DdlDetail detail) {
		StringBuilder sb = new StringBuilder();
		
		// 注释
		sb.append("        /// <summary>\r\n");
		sb.append("        /// "+ detail.getFieldName() +"\r\n");

		// 说明部分
		if(!StringUtility.isNullOrEmpty(detail.getDescription())) {
			String[] descriptions = this.model.getDescription().split("\\n");
			for(String itm:descriptions) {
				sb.append("        /// " + itm.trim() + "\r\n");
			}
		}
		sb.append("        /// </summary>\r\n");
		
		// 注解
		String com = "";
		
		// Required
		if(!detail.isColNullable()) {
			com = "Required";
		}
		
		// StringLength
		if(detail.getColLength() != null) {
			if(!StringUtility.isNullOrEmpty(com)) {
				com += ", ";
			}
			com += String.format("StringLength(%s)", detail.getColLength());
		}
		if(!StringUtility.isNullOrEmpty(com)){
			sb.append("        ["+com+"]\r\n");
		}
		
		// 类型
		sb.append("        public "
				+ this.getDataType(detail)
				+ " " + detail.getPoName()
				+ " { get; set; }\r\n\r\n");
		return sb.toString();
	}
	
	/**
	 * 获取属性的数据类型
	 * @param detail 属性
	 * @return .NET的数据类型
	 */
	private String getDataType(DdlDetail detail) {
		String type = detail.getPoType();
		
		// 如果Excel中没填写PO属性则根据设置计算
		if(StringUtility.isNullOrEmpty(type)) {
			String typeKey = null, typeValue = null;
			String nullableKey = null, nullableValue = null;
			boolean nullable = true;
			
			// 类型映射
			typeKey = "data_type_"+detail.getColType();
			if(this.settingMap.containsKey(typeKey)) {
				typeValue = this.settingMap.get(typeKey);
			}
			
			// .NET中是否可为空
			nullableKey = "data_nullable_"+detail.getColType();
			if(this.settingMap.containsKey(nullableKey)) {
				nullableValue = this.settingMap.get(nullableKey);
				if(nullableValue.toLowerCase().equals("false")) {
					nullable = false;
				}
			}
			
			// 生成type
			if(StringUtility.isNullOrEmpty(typeValue)) {
				// 没有设置好的类型则使用DB类型
				typeValue = detail.getColType();
			}
			
			// 代码中不能为null且DB中可以为null时需要添加Nullable<>
			if(!nullable && detail.isColNullable()) {
				type = String.format("Nullable<%s>", typeValue);
			} else {
				type = typeValue;
			}
		}
		return type;
	}
}
