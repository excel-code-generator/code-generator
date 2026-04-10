/**
 * Copyright 2015-2023 yanglb.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanglb.codegen.core.translator.impl;

import com.google.gson.FormattingStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.yanglb.codegen.core.translator.BaseMsgTranslator;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.model.TableModel;
import com.yanglb.codegen.model.WritableModel;
import com.yanglb.codegen.utils.StringUtil;

import java.util.Map;


public class MsgJsonTranslatorImpl extends BaseMsgTranslator {
    @Override
    protected void onBeforeTranslate() throws CodeGenException {
        super.onBeforeTranslate();
        this.writableModel.get(0).setExtension("json");
    }

    private void tblModel2Json(JsonObject json, TableModel tblModel) {
        for (Map<String, String> itm : tblModel.toList()) {
            String id = itm.get("id");
            String value = itm.get(this.msgLang);
            if (StringUtil.isNullOrEmpty(id)) continue;

            json.addProperty(id, value);
        }
    }

    @Override
    protected void onTranslate(WritableModel writableModel) throws CodeGenException {
        super.onTranslate(writableModel);
        JsonObject json = new JsonObject();
        StringBuilder sb = writableModel.getData();

        if (this.parameterModel.getOptions().hasOption("combine")) {
            // 合并输出
            for (TableModel tblModel : this.model) {
                tblModel2Json(json, tblModel);
            }
        } else {
            // 分组输出
            for (TableModel tblModel : this.model) {
                JsonObject sub = new JsonObject();
                tblModel2Json(sub, tblModel);

                String sheetName = tblModel.getSheetName();
                json.add(sheetName, sub);
            }
        }

        // to JSON string
        FormattingStyle formattingStyle = FormattingStyle.PRETTY;
        if (parameterModel.getOptions().hasOption("minify")) formattingStyle = FormattingStyle.COMPACT;
        Gson gson = new GsonBuilder().setFormattingStyle(formattingStyle).create();
        sb.append(gson.toJson(json));
    }
}
