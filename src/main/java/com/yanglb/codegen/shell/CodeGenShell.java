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

import java.io.File;
import java.lang.reflect.Field;

import com.yanglb.codegen.core.GenFactory;
import com.yanglb.codegen.core.generator.IGenerator;
import com.yanglb.codegen.core.model.ParamaModel;
import com.yanglb.codegen.core.model.ParamaModel2;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.exceptions.ParamaCheckException;
import com.yanglb.codegen.support.SupportLang;
import com.yanglb.codegen.support.SupportType;
import com.yanglb.codegen.utility.Infos;
import com.yanglb.codegen.utility.StringUtility;
import org.apache.commons.cli.*;
import org.apache.poi.util.StringUtil;

public class CodeGenShell {
	private Options options(String cmd, String type) {
		return options();
	}
	private Options options() {
		return null;
	}
	public boolean invoke(String[] args) {

		args = new String[] {"msg.json", "/Users/yanglibing/Work/ACON/src/ACON/i18n/AppResources11.xlsx", "-s", "test", "test1", "v"};
		ICmdParser parser = ICmdParser.parserByArgs(args);
		try {
			ParamaModel2 pm = parser.parsing();
			if (pm == null) return true;
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			parser.printHelp();
			return false;
		}
//		if (parser.verification())
//		parser.printHelp();
		return true;
	}
	public boolean invoke2(String[] args) {
		// TODO: test data
//		args = new String[] {"msg", "cs", "/Users/yanglibing/Work/ACON/src/ACON/i18n/AppResources.xlsx", "-s", "test"};
//		args = new String[] {"msg"};

		if (args.length == 0) {
			showHelp();
			return true;
		}

		CommandLineParser parser = new DefaultParser();
		SupportType command;
		SupportLang type;
		CommandLine commandLine;
		String file;
		try {
			String cmdStr  = notOption(0, args);
			String typeStr = notOption(1, args);
			file = notOption(2, args);

			Options opts = options();
			commandLine = parser.parse(opts, args);
			if (commandLine.hasOption("help")) {
				showHelp(cmdStr, typeStr);
				return true;
			}
			if (commandLine.hasOption("version")) {
				showVersion();
				return true;
			}

			// 检查参数是否正确
			command = SupportType.valueOf(cmdStr);
			type = SupportLang.valueOf(notOption(1, args));
			File f = new File(file);
			if(!(f.exists() && f.isFile())) {
				System.out.printf("指定的文件(%s)不存在或者是一个目录。\r\n", file);
				return false;
			}

			// 根据命令生成解析
			opts = options(cmdStr, typeStr);
			commandLine = parser.parse(opts, args);
		} catch (IllegalArgumentException | NullPointerException | ParseException ex) {
			System.out.println("您输入的参数不正确，请使用 --help 命令查看用法。");
			return false;
		}

		// 执行
		try {
			ParamaModel model = new ParamaModel();
			model.setLang(type);
			model.setType(command);
			model.setSheets(commandLine.getOptionValues("s"));
			model.setIn(file);
			model.setOut(commandLine.getOptionValue("out", "./out"));
			IGenerator generator = GenFactory.createGenerator(model);
			generator.invoke();

			// 不出错时就成功
			System.out.println("恭喜你，代码生成成功！");
			return true;
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
	}

	private String notOption(int idx, String[] args) {
		int i = 0;
		for (String itm : args) {
			if (itm.startsWith("-")) continue;
			if (i++ >= idx) return itm;
		}
		return null;
	}

	private void showVersion() {
		System.out.println("v" + Infos.Version);
	}

	private void showHelp(String cmd, String type) {
		if (cmd == null) {
			showHelp();
			return;
		}
		System.out.println("cmd 用法 " + cmd);
		showHelp();
	}
//	private void showMsgHelp(String type) {
//		HelpFormatter formatter = new HelpFormatter();
//		formatter.setSyntaxPrefix("用法：");
//
//		System.out.println("生成 " + cmd);
//
//		formatter.printHelp("cg msg type file [options]", options(SupportType.msg.toString(), type));
//
//		System.out.println("cmd 用法 " + cmd);
//		showHelp();
//	}
	private void showHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setSyntaxPrefix("用法：");
		formatter.printHelp("cg command type file [options]", options());

		System.out.println();
		System.out.println("command & type: ");
		System.out.println(" ddl: 生成数据库结构SQL脚本。");
		System.out.println(" dml: 生成数据库初始数据SQL脚本。");
		System.out.println(" msg: 生成国际化资源文件。");
		System.out.println("通过 cg command --help 查看type选项。");
		System.out.println();
		System.out.println("示例：");
		System.out.println("  cg msg json 001.xlsx");
		System.out.println("  cg msg resx 001.xlsx --sheets Sheet1 Sheet2 Sheet5");
		System.out.println();
		System.out.println("帮助：");
		System.out.println("  cg ddl --help  显示数生成据库结构的更多帮助信息。");
		System.out.println("  cg dml --help  显示生成初始数据的更多帮助信息。");
		System.out.println("  cg msg --help  显示生成国际化资源的更多帮助信息。");

		System.out.println();
		System.out.println("---");
		System.out.println(Infos.Name + " v" + Infos.Version);
		System.out.println("By " + Infos.Website);
	}
}
