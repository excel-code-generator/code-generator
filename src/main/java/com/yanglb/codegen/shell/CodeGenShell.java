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
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.exceptions.ParamaCheckException;
import com.yanglb.codegen.support.SupportLang;
import com.yanglb.codegen.support.SupportType;
import com.yanglb.codegen.utility.Infos;
import com.yanglb.codegen.utility.StringUtility;
import org.apache.commons.cli.*;
import org.apache.poi.util.StringUtil;

public class CodeGenShell {

	private final String PAR_HELP   = "--help";
//	private final String PAR_TYPE   = "-type";
//	private final String PAR_LANG   = "-lang";
//	private final String PAR_IN     = "-in";
//	private final String PAR_OUT    = "-out";
//	private final String PAR_SHEETS = "-sheets";


	private Options options(String cmd) {
		return options();
	}
	private Options options() {
		Options options = new Options();

		Option help = new Option("h", "help", false, "显示帮助信息。");
		options.addOption(help);

		Option version = new Option("v", "version", false, "打印版本信息。");
		options.addOption(version);

		Option outDir = Option.builder("out")
				.longOpt("out-dir")
				.argName("dir")
				.desc("输出目录，默认输出到 ./out 目录下。")
				.hasArg(true)
				.build();
		options.addOption(outDir);

		Option sheets = Option.builder("s")
				.longOpt("sheets")
				.argName("names")
				.desc("要处理的Excel Sheet名，默认全部，\"#\"开头的不处理。")
				.hasArg(true)
				.hasArgs()
				.build();
		options.addOption(sheets);
		return options;
	}
	public boolean invoke(String[] args) {
		if (args.length == 0) {
			showHelp();
			return true;
		}

		CommandLineParser parser = new DefaultParser();
		SupportType command;
		SupportLang type;
		CommandLine line;
		String file;
		try {
//			args = new String[] {"msg", "cs", "/Users/yanglibing/Work/ACON/src/ACON/i18n/AppResources.xlsx", "-s", "test"};
//			args = new String[] {"msg"};

			String cmd = notOption(0, args);

			Options opts = options();
			line = parser.parse(opts, args);
			if (line.hasOption("help")) {
				showHelp(cmd);
				return true;
			}
			if (line.hasOption("version")) {
				showVersion();
				return true;
			}

			command = SupportType.valueOf(cmd);
			type = SupportLang.valueOf(notOption(1, args));
			file = notOption(2, args);

			// 检查参数是否正确
			File f = new File(file);
			if(!(f.exists() && f.isFile())) {
				System.out.printf("指定的文件(%s)不存在或者是一个目录。\r\n", file);
				return false;
			}

			// 根据命令生成解析
			opts = options(cmd);
			line = parser.parse(opts, args);
		} catch (IllegalArgumentException | NullPointerException | ParseException ex) {
			System.out.println("您输入的参数不正确，请使用 --help 命令查看用法。");
			return false;
		}

		try {
			ParamaModel model = new ParamaModel();
			model.setLang(type);
			model.setType(command);
			model.setSheets(line.getOptionValues("s"));
			model.setIn(file);
			model.setOut(line.getOptionValue("out", "./out"));
			IGenerator generator = GenFactory.createGenerator(model);
			generator.invoke();

			// 不出错时就成功
			System.out.println("恭喜你，代码生成成功！");
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

		return true;
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

	private void showHelp(String cmd) {
		if (cmd == null) {
			showHelp();
			return;
		}
		System.out.println("cmd 用法 " + cmd);
		showHelp();
	}
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
//
//	public boolean invoke2(String[] args) {
//		if(args.length == 0 || this.PAR_HELP.equals(args[0])) {
//			this.showHelp();
//			return true;
//		}
//
//		// 根据命令行参数生成 model
//		ParamaModel model = this.convert2Model(args);
//		if(model == null
//				|| StringUtility.isNullOrEmpty(model.getIn())
//				|| StringUtility.isNullOrEmpty(model.getOut())
//				|| model.getType() == null
//				|| model.getLang() == null) {
//			System.out.println("您输入的参数不正确，请使用 --help 命令查看用法。");
//			return false;
//		}
//
//		// 检查参数是否正确
//		File f = new File(model.getIn());
//		if(!(f.exists() && f.isFile())) {
//			System.out.printf("指定的文件(%s)不存在或者是一个目录。\r\n", model.getIn());
//			return false;
//		}
//
//		// 执行
//		try {
//			IGenerator generator = GenFactory.createGenerator(model);
//			generator.invoke();
//
//			// 不出错时就成功
//			System.out.println("恭喜你，代码生成成功！");
//		}
//		catch (ParamaCheckException e) {
//			System.out.println("生成失败!");
//			System.out.println(e.getMessage());
//			return false;
//		}
//		catch (CodeGenException e) {
//			System.out.println("生成失败!");
//			System.out.println(e.getMessage());
//			return false;
//		}
//
//		// 最终生成成功
//		return true;
//	}
//
//	/**
//	 * 显示帮助信息
//	 */
//	private void showHelp2() {
//		System.out.println(String.format("代码生成器 v%s 使用说明", Infos.Version));
//		System.out.println(Infos.Copyright);
//		System.out.println();
//		System.out.println("用法：");
//		System.out.println("cg -type 生成类型 -lang 生成语言 -in 输入文件 [-sheets 要生成的Sheet名1[,2]] [-out 输出目录]");
//
//		System.out.println();
//		System.out.println("选项：");
//		System.out.printf("   -type: %s\r\n", StringUtility.enumToString(SupportType.class));
//		System.out.printf("   -lang: %s\r\n", StringUtility.enumToString(SupportLang.class));
//
//		System.out.println();
//		System.out.println("默认值：");
//		System.out.println("   -type: 无");
//		System.out.println("   -lang: java (type为 ddl/dml时默认值为sql)");
//		System.out.println("     -in: 无");
//		System.out.println(" -sheets: 所有");
//		System.out.println("    -out: ./out");
//
//		System.out.println();
//		System.out.println("用例：");
//		System.out.println(" cg --help");
////		System.out.println(" cg -type db -lang java -in 001.xlsx -sheets S001_配置表,S002_配置明细表 -out ./out");
//		System.out.println(" cg -type dml -in 001.xlsx -sheets 初始数据");
//		System.out.println(" cg -type ddl_mysql -in 001.xlsx");
//		System.out.println(" cg -type msg -lang js -in 001.xlsx");
//		System.out.println(" cg -type msg -lang java -in 001.xlsx");
//		System.out.println();
//	}
//
//	private ParamaModel convert2Model(String[] args) {
//		ParamaModel model = new ParamaModel();
//
//		// 默认值
//		model.setOut("./out");
//		model.setLang(SupportLang.java);
//
//		// 根据参数设置
//		for(int i=0; i<args.length; i++) {
//			String key = args[i];
//			if(key.matches("-[a-zA-Z0-9]+")) {
//				key = key.substring(1, key.length());
//
//				// 不可指定 -options
//				if(key.equals("options")) return null;
//
//				try {
//					String parValue = args[++i];
//					Object value = parValue;
//					Field field = ParamaModel.class.getDeclaredField(key);
//					if(field.getType().equals(SupportType.class)) {
//						value = SupportType.valueOf(parValue);
//					} else if(field.getType().equals(SupportLang.class)) {
//						value = SupportLang.valueOf(parValue);
//					} else if(field.getType().equals(String[].class)) {
//						value = parValue.split(",");
//					}
//
//					field.setAccessible(true);
//					field.set(model, value);
//				} catch (Exception e) {
//					return null;
//				}
//			} else if (key.matches("--[a-zA-Z0-9]+")) {
//				key = key.substring(2, key.length());
//				model.getOptions().put(key, args[++i]);
//			}
//		}
//
//		// DDL/DML时只可能是SQL
//		if(model.getType() == SupportType.ddl_mysql
//				|| model.getType() == SupportType.ddl_sqlite
//				|| model.getType() == SupportType.ddl_sqlserver
//				|| model.getType() == SupportType.dml) {
//			model.setLang(SupportLang.sql);
//		}
//		return model;
//	}
}
