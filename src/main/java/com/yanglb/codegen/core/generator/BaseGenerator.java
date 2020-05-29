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
package com.yanglb.codegen.core.generator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.yanglb.codegen.core.model.OptionModel;
import com.yanglb.codegen.core.model.ParamaModel;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.exceptions.ParamaCheckException;

public class BaseGenerator implements IGenerator{
	protected ParamaModel paramaModel;
	protected List<OptionModel> supportOptions;
	protected HashMap<String, String> settingMap;
	
	protected void printInfo() {
		System.out.println("生成信息:");
		for(Field field:this.paramaModel.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				String val = null;
				Object value = field.get(paramaModel);
				if(field.getName().equals("sheets")) {
					// TODO: paramaModel
//					val = (paramaModel.getSheets() == null) ? "All" : Arrays.toString(paramaModel.getSheets());
				} else {
					if(value != null) {
						val = value.toString();
					}
				}
				System.out.printf("%8s: %s\r\n", field.getName(), val);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println();
		if(this.supportOptions.size()>0) {
			System.out.println("可用的选项:");
			for(OptionModel itm : this.supportOptions) {
				System.out.println(String.format("  --%-10s [%s]", 
						itm.getName(),
						itm.isNecessary()? "必要" : "可选"));
			}
			System.out.println();
		}
	}

	/**
	 * 初始化
	 * @param paramaModel
	 */
	@Override
	public void init(ParamaModel paramaModel) {
		this.paramaModel = paramaModel;
		this.supportOptions = new ArrayList<OptionModel>();
		this.settingMap = new HashMap<String, String>();
		
		this.settingMap.put("generationDate", new Date().toString());
	}

	@Override
	public final void invoke() throws CodeGenException, ParamaCheckException {
		this.onCheck();
		
		this.printInfo();
		
		// 生成代码
		this.onGeneration();
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
