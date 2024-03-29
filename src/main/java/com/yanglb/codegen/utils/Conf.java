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

import com.yanglb.codegen.model.CmdModel;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;


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

		Map<String, Map<String, String>> values = ObjectUtil.cast(settings.get("command"));
		Map<String, String> map = values.get(cmd);
		if (map == null) return null;

		return new CmdModel(map);
	}

	public static final String CATEGORY_READER = "reader";
	public static final String CATEGORY_WRITER = "writer";
	public static final String CATEGORY_SETTINGS = "settings";

	public static String getString(String category, String key) {
		// 初始化
		init();

		Map<String, Object> values = ObjectUtil.cast(settings.get(category));
		return values.get(key).toString();
	}

	public static String getSetting(String key) {
		return getString(CATEGORY_SETTINGS, key);
	}

	public static List<String> supportCommands() {
		init();

		Map<String, Map<String, String>> values = ObjectUtil.cast(settings.get("command"));
		return new ArrayList<>(values.keySet());
	}
}
