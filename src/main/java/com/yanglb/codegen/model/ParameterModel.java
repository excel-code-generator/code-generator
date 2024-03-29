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
package com.yanglb.codegen.model;

import org.apache.commons.cli.CommandLine;

public class ParameterModel {
	private String cmd;
	private String file;
	CommandLine options;
	CmdModel cmdModel;

	public String[] getSheets() {
		return options.getOptionValues("sheets");
	}
	public String getOptDir() {
		return options.getOptionValue("out", "out");
	}
	public String getFileName() {
		if (!options.hasOption("fn")) return null;
		return options.getOptionValue("fn", "");
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public CommandLine getOptions() {
		return options;
	}

	public void setOptions(CommandLine options) {
		this.options = options;
	}

	public CmdModel getCmdModel() {
		return cmdModel;
	}

	public void setCmdModel(CmdModel cmdModel) {
		this.cmdModel = cmdModel;
	}
}
