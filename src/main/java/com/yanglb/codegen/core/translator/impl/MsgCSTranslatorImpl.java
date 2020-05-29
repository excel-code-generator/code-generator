/**
 * Copyright 2020 yanglb.com
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

import java.util.HashMap;
import java.util.Map;

import com.yanglb.codegen.core.model.TableModel;
import com.yanglb.codegen.core.translator.BaseMsgTranslator;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.utils.MsgUtility;
import com.yanglb.codegen.utils.StringUtility;

public class MsgCSTranslatorImpl extends BaseMsgTranslator {
	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();
		
		this.writableModel.setExtension("resx");
		this.writableModel.setFilePath("msg/resx");
	}

	@Override
	protected void onTranslate() throws CodeGenException {
		super.onTranslate();
		StringBuilder sb = this.writableModel.getData();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + 
				this.settingMap.get("head") +
				"<root>\r\n");
		
		if (this.isDefaultLanguage()) {
			sb.append(this.settingMap.get("schema"));
		}
		
		// 添加 resheader 
		sb.append(this.settingMap.get("resheader"));
		
		// 替换标记
		String s = this.replaceFlags(sb.toString(), null);
		sb = new StringBuilder(s);
		
		// 用于检查相同的key
		Map<String, Boolean> keys = new HashMap<String, Boolean>();
		for(TableModel tblModel : this.model) {
			// 添加Sheet注释
			sb.append(String.format("    \r\n"));
			sb.append(String.format("    <!-- %s -->\r\n", tblModel.getSheetName()));
			
			for(Map<String, String> itm : tblModel.toList()) {
				String id = itm.get("id");
				if(StringUtility.isNullOrEmpty(id)) continue;
				if (keys.containsKey(id)) throw new CodeGenException(String.format(MsgUtility.getString("E_013"), id));
				keys.put(id, true);
				
				// 对字符串进行转换
				String value = this.convert2CSCode(itm.get(this.msgLang));
				sb.append(String.format("    <data name=\"%s\">\r\n", id));
				sb.append(String.format("        <value>%s</value>\r\n", value));
				sb.append(String.format("    </data>\r\n"));
			}
		}
		
		int idx = sb.lastIndexOf(",");
		if(idx != -1) {
			sb.deleteCharAt(idx);
		}
		sb.append("</root>\r\n");
		
		this.writableModel.setData(sb);
	}
	
	private String convert2CSCode(String value) {
		if(value == null) return null;
		
		// 先替换\r\n，防止有文档只有\r或\n 后面再替换一次
//		value = value.replaceAll("\r\n", "\\\\r\\\\n");
//		value = value.replaceAll("\r", "\\\\r\\\\n");
//		value = value.replaceAll("\n", "\\\\r\\\\n");
		return value;
	}
}
