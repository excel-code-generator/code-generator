/**
 * Copyright 2015-2020 yanglb.com
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

import com.yanglb.codegen.model.TableModel;
import com.yanglb.codegen.core.translator.BaseMsgTranslator;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.utils.Infos;
import com.yanglb.codegen.utils.Resources;
import com.yanglb.codegen.utils.StringUtil;

public class MsgIOSTranslatorImpl extends BaseMsgTranslator {
	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();
		
		this.writableModel.setExtension("strings");
		String path = (this.isDefaultLanguage() ? "Base" : this.msgLang) + ".lproj";

		// 文件名
		String fileName = path + "/" + getFileName();
		this.writableModel.setFileName(fileName);
	}

	@Override
	protected void onTranslate() throws CodeGenException {
		super.onTranslate();
		StringBuilder sb = this.writableModel.getData();
		
		sb.append(Infos.cHeader());
		sb.append("\n");

		// 用于检查相同的key
		Map<String, Boolean> keys = new HashMap<String, Boolean>();
		for(TableModel tblModel : this.model) {
			for(Map<String, String> itm : tblModel.toList()) {
				String id = itm.get("id");
				if(StringUtil.isNullOrEmpty(id)) continue;
				if (keys.containsKey(id)) throw new CodeGenException(String.format(Resources.getString("E_013"), id));
				keys.put(id, true);
				
				// 对字符串进行转换
				id = escape(id);
				String value = this.escape(itm.get(this.msgLang));
				sb.append(String.format("\"%s\" = \"%s\";\r\n", id, value));
			}
		}
		
		this.writableModel.setData(sb);
	}
	
	private String escape(String value) {
		if(value == null) return null;

		value = StringUtil.escapeIOSString(value);
		return value;
	}
}