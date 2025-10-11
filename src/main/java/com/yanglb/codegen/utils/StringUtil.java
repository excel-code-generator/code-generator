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
package com.yanglb.codegen.utils;

import java.lang.reflect.Field;

public class StringUtil {

    /**
     * 判断某个字符串是否为“空”字符串
     */
    public static boolean isNullOrEmpty(String value) {
        return (value == null || "".equals(value));
    }

    public static String enumToString(Class<?> en) {
        StringBuilder sb = new StringBuilder();
        for (Field f : en.getFields()) {
            if (sb.length() != 0) {
                sb.append("|");
            }
            sb.append(f.getName());
        }
        return sb.toString();
    }

    /**
     * 处理IOS字符串
     */
    public static String escapeIOSString(String value) {
        if (value == null) return null;

        value = value.replaceAll("\\\\", "\\\\\\\\");
        value = value.replaceAll("\"", "\\\\\"");

        value = value.replaceAll("\r", "");
        value = value.replaceAll("\n", "\\\\n");
        return value;
    }

    public static String toValidPropertyName(String input) {
        if (input == null || input.isEmpty()) {
            return "_";
        }

        StringBuilder sb = new StringBuilder();

        // .NET 属性名必须以字母或下划线开头
        char first = input.charAt(0);
        if (Character.isLetter(first) || first == '_') {
            sb.append(first);
        } else {
            sb.append('_');
        }

        // 处理剩余字符：字母 / 数字 / 下划线 保留，其它替换成 "_"
        for (int i = 1; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isLetterOrDigit(c) || c == '_') {
                sb.append(c);
            } else {
                sb.append('_');
            }
        }

        return sb.toString();
    }


    /**
     * 生成 C# XML doc <summary> 注释块。
     * <p>
     * 格式固定为：
     * <pre>
     *     /// <summary>
     *     /// line1
     *     /// line2
     *     /// </summary>
     * </pre>
     *
     * @param summary 要放到 <summary> 内的文本，可能包含换行符；为 null 时等同于空串
     * @param padLeft 每行前面额外的空格数（缩进）
     * @return 完整的多行注释字符串（每行以 '\n' 结尾）
     */
    public static String generateCSharpSummary(String summary, int padLeft) {
        if (summary == null) summary = "";
        if (padLeft < 0) padLeft = 0;

        String indent = " ".repeat(padLeft);
        String linePrefix = indent + "/// ";
        StringBuilder sb = new StringBuilder();

        // 简单的 XML 实体转义
        java.util.function.Function<String, String> escapeXml = s -> {
            if (s == null || s.isEmpty()) return s;
            return s
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
        };

        // 按行分割（支持 \r\n, \r, \n）
        String[] lines = summary.split("\\r?\\n|\\r", -1); // 保留尾随空行

        sb.append(linePrefix).append("<summary>").append("\n");
        for (String line : lines) {
            sb.append(linePrefix).append(escapeXml.apply(line)).append("\n");
        }
        sb.append(linePrefix).append("</summary>");

        return sb.toString();
    }

    // 简单测试
    public static void main(String[] args) {
        System.out.println(toValidPropertyName("123Name"));     // _23Name
        System.out.println(toValidPropertyName("user-name"));   // user_name
        System.out.println(toValidPropertyName("name@domain")); // name_domain
        System.out.println(toValidPropertyName("正常属性"));       // 正常属性（中文是合法的 Unicode 字母）
    }
}
