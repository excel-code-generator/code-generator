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
package com.yanglb.codegen.support;

import java.util.HashMap;

public class SupportCmd {
	private static HashMap<String, SupportCmd> supportCmds;
	private static void put(String cmd, String description) {
		supportCmds.put(cmd, new SupportCmd(cmd, description));
	}
	public static HashMap<String, SupportCmd> SupportCmds() {
		if (supportCmds == null) {
			supportCmds = new HashMap<String, SupportCmd>();
			supportCmds.put("ddl.mssql",   new SupportCmd("ddl.mssql",   "生成SqlServer数据库结构SQL脚本（.ddl）。"));
			supportCmds.put("ddl.mysql",   new SupportCmd("ddl.mysql",   "生成MySql数据库结构SQL脚本（.ddl）。"));
			supportCmds.put("ddl.sqlite",  new SupportCmd("ddl.sqlite",  "生成SQLite数据库结构SQL脚本（.ddl）。"));
			supportCmds.put("dml",         new SupportCmd("dml",         "生成数据库初始 数据SQL脚本（.dml）。"));
			supportCmds.put("msg.resx",    new SupportCmd("msg.resx",    "生成.NET国际化资源文件（.resx）。"));
			supportCmds.put("msg.json",    new SupportCmd("msg.json",    "生成JSON国际化资源文件（.json）。"));
			supportCmds.put("msg.ios",     new SupportCmd("msg.ios",     "生成IOS国际化资源文件（.strings）。"));
			supportCmds.put("msg.android", new SupportCmd("msg.android", "生成Android国际化资源文件（strings.xml）。"));
			supportCmds.put("msg.prop",    new SupportCmd("msg.prop",    "生成Java国际化资源文件（.properties）。"));
		}
		return supportCmds;
	}

	private SupportCmd(String cmd, String description)
	{
		this.cmd = cmd;
		this.description = description;
	}

	private String cmd;
	private String description;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
