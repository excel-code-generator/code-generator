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
package com.yanglb.codegen.core.model;

import java.util.HashMap;
import java.util.Map;

import com.yanglb.codegen.support.SupportLang;
import com.yanglb.codegen.support.SupportType;

public class ParamaModel {
	// 生成类型
	private SupportType type;
	// 生成语言
	private SupportLang lang;
	
	// 输入Excel文件
	private String in;
	
	// 输出目录
	private String out;
		
	// 要处理的Sheet列表（空时处理所有Sheet）
	private String[] sheets;
	
	// 选项
	Map<String, String> options = new HashMap<String, String>();
	
	public String getOut() {
		return out;
	}
	public void setOut(String out) {
		this.out = out;
	}
	public String getIn() {
		return in;
	}
	public void setIn(String in) {
		this.in = in;
	}
	public String[] getSheets() {
		return sheets;
	}
	public void setSheets(String[] sheets) {
		this.sheets = sheets;
	}
	public SupportType getType() {
		return type;
	}
	public void setType(SupportType type) {
		this.type = type;
	}
	public SupportLang getLang() {
		return lang;
	}
	public void setLang(SupportLang lang) {
		this.lang = lang;
	}
	public Map<String, String> getOptions() {
		return options;
	}
	public void setOptions(Map<String, String> options) {
		this.options = options;
	}
}
