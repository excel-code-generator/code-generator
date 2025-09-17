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

import com.yanglb.codegen.core.translator.BaseMsgTranslator;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.model.TableModel;
import com.yanglb.codegen.utils.Infos;
import com.yanglb.codegen.utils.StringUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;


public class MsgAndroidTranslatorImpl extends BaseMsgTranslator {
    @Override
    protected void onBeforeTranslate() throws CodeGenException {
        super.onBeforeTranslate();
        this.writableModel.setExtension("xml");

        // Android 资源输出目录结构为: strings/strings.xml、strings-zh/strings.xml 等
        String path = "values";
        if (!this.isDefaultLanguage()) {
            path = String.format("values-%s", this.msgLang);
        }

        // 文件名，未设置时默认为strings
        String fileName = writableModel.getFileName();
        if (StringUtil.isNullOrEmpty(this.parameterModel.getFileName())) {
            fileName = "strings";
        }

        fileName = path + "/" + fileName;
        writableModel.setFileName(fileName);
    }

    @Override
    protected String getSplitString() {
        return "-";
    }

    @Override
    protected void onTranslate() throws CodeGenException {
        super.onTranslate();
        StringBuilder sb = this.writableModel.getData();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                Infos.xmlHeader() +
                "<resources>\n");

        // 用于检查相同的key
        Map<String, Boolean> keys = new HashMap<String, Boolean>();
        Map<String, List<String>> arrays = new HashMap<String, List<String>>();
        for (TableModel tblModel : this.model) {
            for (Map<String, String> itm : tblModel.toList()) {
                String id = escape(itm.get("id"));
                if (StringUtil.isNullOrEmpty(id)) continue;
                if (!arrays.containsKey(id)) {
                    arrays.put(id, new ArrayList<String>());
                }

                // 对字符串进行转换
                String value = escape(itm.get(this.msgLang));
                List<String> items = arrays.get(id);
                items.add(value);
            }
        }

        for (String key : arrays.keySet()) {
            List<String> items = arrays.get(key);

            if (items.size() > 1) {
                // list
                sb.append(String.format("    <string-array name=\"%s\">\n", key));
                for (String value : items) {
                    sb.append(String.format("        <item>%s</item>\n", value));
                }
                sb.append("    </string-array>\n");
            } else {
                sb.append(String.format("    <string name=\"%s\">%s</string>\n", key, items.get(0)));
            }
        }

        sb.append("</resources>\n");

        this.writableModel.setData(sb);
    }

    private String escape(String value) {
        if (value == null) return null;

        value = StringEscapeUtils.escapeXml10(value);
        return value;
    }
}
