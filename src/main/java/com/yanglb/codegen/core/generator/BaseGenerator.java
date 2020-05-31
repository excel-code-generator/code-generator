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
package com.yanglb.codegen.core.generator;

import com.yanglb.codegen.model.OptionModel;
import com.yanglb.codegen.model.ParamaModel;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.exceptions.ParamaCheckException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BaseGenerator implements IGenerator{
	protected ParamaModel paramaModel;
	protected List<OptionModel> supportOptions;
	protected HashMap<String, String> settingMap;
	
	protected void printInfo() {
		System.out.println("生成信息:");
		System.out.printf("%8s: %s\n", "cmd", paramaModel.getCmd());
		System.out.printf("%8s: %s\n", "file", paramaModel.getFile());
		System.out.printf("%8s: \n", "options");
		CommandLine cl = paramaModel.getOptions();
		for(Option opt : cl.getOptions()) {
			String s = opt.getLongOpt();
			if (s == null) s = opt.getOpt();
			if (opt.hasArgs()) {
				System.out.printf("%8s-%s=%s\n", "", s, opt.getValuesList());
			} else if (opt.hasArg()) {
				System.out.printf("%8s-%s=%s\n", "", s, opt.getValue());
			} else {
				System.out.printf("%8s-%s\n", "", s);
			}
		}
		System.out.println();
	}

	/**
	 * 初始化
	 * @param paramaModel
	 */
	protected void init(ParamaModel paramaModel) {
		this.paramaModel = paramaModel;
		this.supportOptions = new ArrayList<OptionModel>();
		this.settingMap = new HashMap<String, String>();
		
		this.settingMap.put("generationDate", new Date().toString());
	}

	@Override
	public final void invoke(ParamaModel paramaModel) throws CodeGenException, ParamaCheckException {
		init(paramaModel);

		onCheck();
		
		printInfo();
		
		// 生成代码
		onGeneration();
	}

	@Override
	public List<OptionModel> getSupportOptions() {
		return this.supportOptions;
	}
	
	protected void onCheck() throws ParamaCheckException {
		// 如需要检查请子类自行检查
	}
	
	protected void onGeneration() throws CodeGenException {
		// 子类实现
	}
}
