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

import org.json.JSONObject;

import com.yanglb.utilitys.codegen.core.model.TableModel;
import com.yanglb.utilitys.codegen.core.translator.BaseMsgTranslator;
import com.yanglb.utilitys.codegen.exceptions.CodeGenException;
import com.yanglb.utilitys.codegen.utility.StringUtility;

public class MsgJsonTranslatorImpl extends BaseMsgTranslator {
	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();
		
		this.writableModel.setExtension("json");
		this.writableModel.setFilePath("msg/json");
	}

	@Override
	protected void onTranslate() throws CodeGenException {
		super.onTranslate();
		JSONObject json = new JSONObject();
		StringBuilder sb = this.writableModel.getData();
		
		// 分组输出
		if(this.paramaModel.getOptions().get("group") != null) {
			for(TableModel tblModel : this.model) {
				JSONObject sub = new JSONObject();
				for(Map<String, String> itm : tblModel.toList()) {
					String id = itm.get("id");
					String value = itm.get(this.msgLang);
					if(StringUtility.isNullOrEmpty(id)) continue;
					
					sub.put(id, value);
				}
				String sheetName = tblModel.getSheetName();
				json.put(sheetName, sub);
			}
		} else {
			// 合并输出
			for(TableModel tblModel : this.model) {
				for(Map<String, String> itm : tblModel.toList()) {
					String id = itm.get("id");
					String value = itm.get(this.msgLang);
					if(StringUtility.isNullOrEmpty(id)) continue;
					
					json.put(id, value);
				}
			}
		}
		
		// to JSON string
		sb.append(json.toString(4));
	}
}
