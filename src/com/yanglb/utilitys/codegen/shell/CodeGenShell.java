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
package com.yanglb.utilitys.codegen.shell;

import java.io.File;
import java.lang.reflect.Field;

import com.yanglb.utilitys.codegen.core.GenFactory;
import com.yanglb.utilitys.codegen.core.generator.IGenerator;
import com.yanglb.utilitys.codegen.core.model.ParamaModel;
import com.yanglb.utilitys.codegen.exceptions.CodeGenException;
import com.yanglb.utilitys.codegen.exceptions.ParamaCheckException;
import com.yanglb.utilitys.codegen.support.SupportLang;
import com.yanglb.utilitys.codegen.support.SupportType;
import com.yanglb.utilitys.codegen.utility.StringUtility;

public class CodeGenShell {

	private final String PAR_HELP   = "--help";
//	private final String PAR_TYPE   = "-type";
//	private final String PAR_LANG   = "-lang";
//	private final String PAR_IN     = "-in";
//	private final String PAR_OUT    = "-out";
//	private final String PAR_SHEETS = "-sheets";
		
	public boolean invoke(String[] args) {
		if(args.length == 0 || this.PAR_HELP.equals(args[0])) {
			this.showHelp();
			return true;
		}
		
		// 根据命令行参数生成 model
		ParamaModel model = this.convert2Model(args);
		if(model == null 
				|| StringUtility.isNullOrEmpty(model.getIn())
				|| StringUtility.isNullOrEmpty(model.getOut())
				|| model.getType() == null
				|| model.getLang() == null) {
			System.out.println("您输入的参数不正确，请使用 --help 命令查看用法。");
			return false;
		}
		
		// 检查参数是否正确
		File f = new File(model.getIn());
		if(!(f.exists() && f.isFile())) {
			System.out.printf("指定的文件(%s)不存在或者是一个目录。\r\n", model.getIn());
			return false;
		}
		
		// 执行
		try {
			IGenerator generator = GenFactory.createGenerator(model);
			generator.invoke();
			
			// 不出错时就成功
			System.out.println("恭喜你，代码生成成功！");
		} catch (CodeGenException | ParamaCheckException e) {
			System.out.println("生成失败!");
			System.out.println(e.getMessage());
			return false;
		}
		
		// 最终生成成功
		return true;
	}
	
	/**
	 * 显示帮助信息
	 */
	private void showHelp() {
		System.out.println("代码生成器 v2.0.1 使用说明");
		System.out.println("Copyright 2015-2016 yanglb.com All Rights Reserved.");
		System.out.println();
		System.out.println("用法：");
		System.out.println("cg -type 生成类型 -lang 生成语言 -in 输入文件 [-sheets 要生成的Sheet名1[,2]] [-out 输出目录]");
		
		System.out.println();
		System.out.println("选项：");
		System.out.printf("   -type: %s\r\n", StringUtility.enumToString(SupportType.class));
		System.out.printf("   -lang: %s\r\n", StringUtility.enumToString(SupportLang.class));
		
		System.out.println();
		System.out.println("默认值：");
		System.out.println("   -type: 无");
		System.out.println("   -lang: java (type为 ddl/dml时默认值为sql)");
		System.out.println("     -in: 无");
		System.out.println(" -sheets: 所有");
		System.out.println("    -out: ./out");
		
		System.out.println();
		System.out.println("用例：");
		System.out.println(" cg --help");
//		System.out.println(" cg -type db -lang java -in 001.xlsx -sheets S001_配置表,S002_配置明细表 -out ./out");
		System.out.println(" cg -type dml -in 001.xlsx -sheets 初始数据");
		System.out.println(" cg -type ddl_mysql -in 001.xlsx");
		System.out.println(" cg -type msg -lang js -in 001.xlsx");
		System.out.println(" cg -type msg -lang java -in 001.xlsx");
		System.out.println();
	}
	
	private ParamaModel convert2Model(String[] args) {
		ParamaModel model = new ParamaModel();
		
		// 默认值
		model.setOut("./out");
		model.setLang(SupportLang.java);
		
		// 根据参数设置
		for(int i=0; i<args.length; i++) {
			String key = args[i];
			if(key.matches("-[a-zA-Z0-9]+")) {
				key = key.substring(1, key.length());
				
				// 不可指定 -options 
				if(key.equals("options")) return null;
				
				try {
					String parValue = args[++i];
					Object value = parValue;
					Field field = ParamaModel.class.getDeclaredField(key);
					if(field.getType().equals(SupportType.class)) {
						value = SupportType.valueOf(parValue);
					} else if(field.getType().equals(SupportLang.class)) {
						value = SupportLang.valueOf(parValue);
					} else if(field.getType().equals(String[].class)) {
						value = parValue.split(",");
					}
					
					field.setAccessible(true);
					field.set(model, value);
				} catch (SecurityException | IllegalArgumentException | IllegalAccessException | 
						NoSuchFieldException |ArrayIndexOutOfBoundsException e) {
					return null;
				}
			} else if (key.matches("--[a-zA-Z0-9]+")) {
				key = key.substring(2, key.length());
				model.getOptions().put(key, args[++i]);
			}
		}
		
		// DDL/DML时只可能是SQL
		if(model.getType() == SupportType.ddl_mysql 
				|| model.getType() == SupportType.ddl_sqlite
				|| model.getType() == SupportType.ddl_sqlserver
				|| model.getType() == SupportType.dml) {
			model.setLang(SupportLang.sql);
		}
		return model;
	}
}
