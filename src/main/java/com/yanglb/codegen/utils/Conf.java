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
package com.yanglb.codegen.utils;

import com.yanglb.codegen.model.CmdModel;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Conf {
	private static Map<String, Object> settings = null;

	private static void init() {
		if(settings != null) return;
		Yaml yaml = new Yaml();

		InputStream inputStream = Conf.class
				.getClassLoader()
				.getResourceAsStream("conf.yaml");
		settings = yaml.load(inputStream);
	}

	public static CmdModel getCmdModel(String cmd) {
		init();

		Map<String, Map<String, String>> values = (Map<String, Map<String, String>>) settings.get("command");
		Map<String, String> map = values.get(cmd);
		if (map == null) return null;

		CmdModel model = new CmdModel(map);
		return model;
	}

	public static String CATEGORY_READER = "reader";
	public static String CATEGORY_WRITER = "writer";

	public static String getString(String category, String key) {
		// 初始化
		init();

		Map<String, Object> values = (Map<String, Object>) settings.get(category);
		String value = values.get(key).toString();
		return value;
	}

	public static List<String> supportCommands() {
		init();

		Map<String, Map<String, String>> values = (Map<String, Map<String, String>>) settings.get("command");
		return new ArrayList<>(values.keySet());
	}
}
