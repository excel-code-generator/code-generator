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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yanglb.codegen.core.model.TableModel;
import com.yanglb.codegen.core.translator.BaseMsgTranslator;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.utils.StringUtility;

public class MsgAndroidTranslatorImpl extends BaseMsgTranslator {
	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();
		
		this.writableModel.setExtension("xml");
		String path =  (this.isDefaultLanguage() ? "" : this.getSplitString() + this.msgLang);
		this.writableModel.setFilePath("msg/android/values" + path);
		
		// 文件名
		this.writableModel.setFileName("strings");
	}
	
	@Override
	protected String getSplitString() {
		return "-";
	}

	@Override
	protected void onTranslate() throws CodeGenException {
		super.onTranslate();
		StringBuilder sb = this.writableModel.getData();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + 
				this.settingMap.get("head") +
				"<resources>\r\n");
		
		// 替换标记
		String s = this.replaceFlags(sb.toString(), null);
		sb = new StringBuilder(s);
		
		// 用于检查相同的key
		Map<String, Boolean> keys = new HashMap<String, Boolean>();
		Map<String, List<String>> arrays = new HashMap<String, List<String>>();
		for(TableModel tblModel : this.model) {
			for(Map<String, String> itm : tblModel.toList()) {
				String id = itm.get("id");
				if(StringUtility.isNullOrEmpty(id)) continue;
				if (!arrays.containsKey(id)) {
					arrays.put(id, new ArrayList<String>());
				}
				
				// 对字符串进行转换
				String value = this.convert2CSCode(itm.get(this.msgLang));
				List<String> items = arrays.get(id);
				items.add(value);
			}
		}
		
		for(String key:arrays.keySet()) {
			List<String> items = arrays.get(key);
			if (items.size() > 1) {
				// list
				sb.append(String.format("    <string-array name=\"%s\">\r\n", key));
				for(String value:items) {
					sb.append(String.format("        <item>%s</item>\r\n", value));
				}
				sb.append("    </string-array>\r\n");
			} else {
				sb.append(String.format("    <string name=\"%s\">%s</string>\r\n", key, items.get(0)));
			}
		}
		
		sb.append("</resources>\r\n");
		
		this.writableModel.setData(sb);
	}
	
	private String convert2CSCode(String value) {
		if(value == null) return null;
		
		// 先替换\r\n，防止有文档只有\r或\n 后面再替换一次
		value = value.replaceAll("\r\n", "\\\\r\\\\n");
		value = value.replaceAll("\r", "\\\\r\\\\n");
		value = value.replaceAll("\n", "\\\\r\\\\n");
		return value;
	}
}
