/**
 * Copyright 2015-2023 yanglb.com
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
package com.yanglb.codegen.utils;

import java.lang.reflect.Field;

public class StringUtil {
	
	/**
	 * 判断某个字符串是否为“空”字符串
	 * @param value
	 * @return
	 */
	public static boolean isNullOrEmpty(String value) {
		return (value == null || "".equals(value));
	}
	
	public static String enumToString(Class<?> en) {
		StringBuilder sb = new StringBuilder();
		for(Field f : en.getFields()) {
			if(sb.length() != 0) {
				sb.append("|");
			}
			sb.append(f.getName());
		}
		return sb.toString();
	}

	/**
	 * 处理IOS字符串
	 * @param value
	 * @return
	 */
	public static String escapeIOSString(String value) {
		if(value == null) return null;

		value = value.replaceAll("\\\\", "\\\\\\\\");
		value = value.replaceAll("\"", "\\\\\"");

		value = value.replaceAll("\r", "");
		value = value.replaceAll("\n", "\\\\n");
		return value;
	}
}
