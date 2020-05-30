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
package com.yanglb.codegen.shell;

import com.yanglb.codegen.core.GenFactory;
import com.yanglb.codegen.core.generator.IGenerator;
import com.yanglb.codegen.model.ParamaModel;
import com.yanglb.codegen.core.parser.IParser;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.exceptions.ParamaCheckException;

public class CGShell {
	public boolean invoke(String[] args) {

		// test data
		args = new String[] {"msg.prop", "/Users/yanglibing/Work/cg/tests/msg.xlsx"};
//		args = new String[] {"ddl.sqlite", "/Users/yanglibing/Work/cg/tests/ddl.xlsx"};
//		args = new String[] {"dml", "/Users/yanglibing/Work/cg/tests/dml.xlsx"};
//		args = new String[] {"", "-h"};
//		args = new String[] {"msg.prop", "message.xlsx", "-out", "src/main/resources"};

		IParser parser = IParser.parserByArgs(args);
		ParamaModel model;
		try {
			model = parser.parsing();
			if (model == null) return true;
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			parser.printHelp();
			return false;
		}

		// 执行
		try {
			IGenerator generator = GenFactory.createByName(model.getCmdModel().getGenerator());
			generator.invoke(model);
		}
		catch (ParamaCheckException | CodeGenException e) {
			System.out.println("生成失败!");
			System.out.println(e.getMessage());
			return false;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

		System.out.println("代码生成成功！");
		return true;
	}
}
