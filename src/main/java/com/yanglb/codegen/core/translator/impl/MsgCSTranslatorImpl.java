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
import com.yanglb.codegen.model.WritableModel;
import com.yanglb.codegen.utils.Conf;
import com.yanglb.codegen.utils.Infos;
import com.yanglb.codegen.utils.Resources;
import com.yanglb.codegen.utils.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.tools.ant.types.Commandline;
import org.yaml.snakeyaml.reader.UnicodeReader;


public class MsgCSTranslatorImpl extends BaseMsgTranslator {

    private String designerNamespace;
    private String designerAccessibility;

    @Override
    protected void onBeforeTranslate() throws CodeGenException {
        super.onBeforeTranslate();
        WritableModel resx = this.writableModel.get(0);
        resx.setExtension("resx");

        // 是否生成 Designer 文件？
        CommandLine opts = parameterModel.getOptions();
        if (opts.hasOption("designer") && isDefaultLanguage()) {
            String namespace = opts.getOptionValue("namespace");
            String accessibility = opts.getOptionValue("accessibility");
            if (StringUtil.isNullOrEmpty(accessibility)) accessibility = "public";
            if (StringUtil.isNullOrEmpty(namespace)) throw new CodeGenException("请通过 --namespace 设置名空间。");

            designerNamespace = namespace;
            designerAccessibility = accessibility;

            WritableModel designer = new WritableModel();
            designer.setFileName(resx.getFileName());
            designer.setExtension("Designer.cs");
            designer.setOutputDir(resx.getOutputDir());
            designer.setEncode(resx.getEncode());
            designer.setData(new StringBuilder());
            this.writableModel.add(designer);
        }
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
    protected void onTranslate(WritableModel writableModel) throws CodeGenException {
        super.onTranslate(writableModel);
        if (writableModel.getExtension().equals("Designer.cs")) {
            translateDesigner(writableModel);
        } else {
            translateResx(writableModel);
        }
    }

    private void translateResx(WritableModel writableModel) throws CodeGenException {
        StringBuilder sb = writableModel.getData();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                Infos.xmlHeader() +
                "<root>\n");

        if (this.isDefaultLanguage()) {
            sb.append(readResource("msg/resx/schema.txt"));
        }

        // 添加 resheader
        sb.append(readResource("msg/resx/resheader.txt"));

        // 用于检查相同的key
        Map<String, Boolean> keys = new HashMap<String, Boolean>();
        for (TableModel tblModel : this.model) {
            // 添加Sheet注释
            sb.append(String.format("    \n"));
            sb.append(String.format("    <!-- %s -->\n", tblModel.getSheetName()));

            for (Map<String, String> itm : tblModel.toList()) {
                String id = itm.get("id");
                if (StringUtil.isNullOrEmpty(id)) continue;
                if (keys.containsKey(id)) throw new CodeGenException(String.format(Resources.getString("E_013"), id));
                keys.put(id, true);

                // 对字符串进行转换
                id = escape(id);
                String value = this.escape(itm.get(this.msgLang));
                sb.append(String.format("    <data name=\"%s\">\n", id));
                sb.append(String.format("        <value>%s</value>\n", value));
                sb.append(String.format("    </data>\n"));
            }
        }

        int idx = sb.lastIndexOf(",");
        if (idx != -1) {
            sb.deleteCharAt(idx);
        }
        sb.append("</root>\n");
    }

    private void translateDesigner(WritableModel writableModel) throws CodeGenException {
        StringBuilder code = new StringBuilder();

        for (TableModel tblModel : this.model) {
            // 添加Sheet注释
            code.append(String.format("        // %s\n", tblModel.getSheetName()));

            for (Map<String, String> itm : tblModel.toList()) {
                String id = itm.get("id");
                if (StringUtil.isNullOrEmpty(id)) continue;

                // 对字符串进行转换
                id = escape(id);
                String value = escape(itm.get(this.msgLang));

                String propertyName = StringUtil.toValidPropertyName(id);
                code.append(StringUtil.generateCSharpSummary(value, 8));
                code.append("\n");
                code.append(String.format("        %s static string %s {\n", designerAccessibility, propertyName));
                code.append(String.format("            get {\n"));
                code.append(String.format("                return ResourceManager.GetString(\"%s\", resourceCulture);\n", id));
                code.append(String.format("            }\n"));
                code.append(String.format("        }\n\n"));
            }
        }

        String template = readResource("msg/resx/designer.cs.txt");
        Map<String, String> values = new HashMap<>();
        values.put("namespace", designerNamespace);
        values.put("className", StringUtil.toValidPropertyName(writableModel.getFileName()));
        values.put("accessibility", designerAccessibility);
        values.put("code", code.toString());

        StringBuilder sb = writableModel.getData();
        sb.append(StringSubstitutor.replace(template, values));
    }

    private String escape(String value) {
        if (value == null) return null;

        value = StringEscapeUtils.escapeXml10(value);
        return value;
    }
}
