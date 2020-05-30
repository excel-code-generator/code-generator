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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.yanglb.codegen.model.TableModel;
import com.yanglb.codegen.core.translator.BaseMsgTranslator;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.utils.Conf;
import com.yanglb.codegen.utils.Infos;
import com.yanglb.codegen.utils.Resources;
import com.yanglb.codegen.utils.StringUtil;
import org.apache.commons.text.StringEscapeUtils;
import org.yaml.snakeyaml.reader.UnicodeReader;

public class MsgCSTranslatorImpl extends BaseMsgTranslator {
	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();
		
		this.writableModel.setExtension("resx");
	}

	protected String readResource(String path) throws CodeGenException {
		InputStream inputStream = Conf.class
				.getClassLoader()
				.getResourceAsStream(path);

		BufferedReader reader = new BufferedReader(new UnicodeReader(inputStream));
		StringBuilder sb = new StringBuilder();
		try {
			String tmp;
			while ((tmp = reader.readLine()) != null) {
				sb.append(tmp);
				sb.append("\n");
			}
			reader.close();
		} catch (IOException ex) {
			throw new CodeGenException(String.format(Resources.getString("E_014"), path));
		}
		return sb.toString();
	}

	@Override
	protected void onTranslate() throws CodeGenException {
		super.onTranslate();
		StringBuilder sb = this.writableModel.getData();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
				Infos.xmlHeader() +
				"<root>\r\n");
		
		if (this.isDefaultLanguage()) {
			sb.append(readResource("msg/resx/schema.txt"));
		}
		
		// 添加 resheader
		sb.append(readResource("msg/resx/resheader.txt"));
		
		// 用于检查相同的key
		Map<String, Boolean> keys = new HashMap<String, Boolean>();
		for(TableModel tblModel : this.model) {
			// 添加Sheet注释
			sb.append(String.format("    \r\n"));
			sb.append(String.format("    <!-- %s -->\r\n", tblModel.getSheetName()));
			
			for(Map<String, String> itm : tblModel.toList()) {
				String id = itm.get("id");
				if(StringUtil.isNullOrEmpty(id)) continue;
				if (keys.containsKey(id)) throw new CodeGenException(String.format(Resources.getString("E_013"), id));
				keys.put(id, true);
				
				// 对字符串进行转换
				id = escape(id);
				String value = this.escape(itm.get(this.msgLang));
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

	private String escape(String value) {
		if(value == null) return null;

		value = StringEscapeUtils.escapeXml10(value);
		return value;
	}
}
