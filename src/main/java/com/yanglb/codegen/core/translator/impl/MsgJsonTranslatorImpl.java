/**
 * Copyright 2015-2021 yanglb.com
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

import com.yanglb.codegen.core.translator.BaseMsgTranslator;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.model.TableModel;
import com.yanglb.codegen.utils.StringUtil;
import java.util.Map;
import org.json.JSONObject;


public class MsgJsonTranslatorImpl extends BaseMsgTranslator {
	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();
		this.writableModel.setExtension("json");
	}

	private void tblModel2Json(JSONObject json, TableModel tblModel) {
		for(Map<String, String> itm : tblModel.toList()) {
			String id = itm.get("id");
			String value = itm.get(this.msgLang);
			if(StringUtil.isNullOrEmpty(id)) continue;

			json.put(id, value);
		}
	}

	@Override
	protected void onTranslate() throws CodeGenException {
		super.onTranslate();
		JSONObject json = new JSONObject();
		StringBuilder sb = this.writableModel.getData();

		if(this.parameterModel.getOptions().hasOption("combine")) {
			// 合并输出
			for(TableModel tblModel : this.model) {
				tblModel2Json(json, tblModel);
			}
		} else {
			// 分组输出
			for(TableModel tblModel : this.model) {
				JSONObject sub = new JSONObject();
				tblModel2Json(sub, tblModel);

				String sheetName = tblModel.getSheetName();
				json.put(sheetName, sub);
			}
		}

		// to JSON string
		int indentFactor = 4;
		if (parameterModel.getOptions().hasOption("minify")) indentFactor = 0;
		sb.append(json.toString(indentFactor));
	}
}
