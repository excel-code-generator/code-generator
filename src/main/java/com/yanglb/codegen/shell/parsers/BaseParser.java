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
package com.yanglb.codegen.shell.parsers;

import com.yanglb.codegen.core.model.ParamaModel;
import com.yanglb.codegen.shell.ICmdParser;
import com.yanglb.codegen.support.SupportCmd;
import com.yanglb.codegen.utils.Infos;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseParser implements ICmdParser {
    protected String[] args;
    @Override
    public void setArgs(String[] args) {
        this.args = args;
    }

    protected Options options() {
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

        Option fn = Option.builder("fn")
                .longOpt("file-name")
                .argName("fn")
                .desc("生成的文件名，默认为Excel名。")
                .hasArg(true)
                .build();
        options.addOption(fn);

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

    protected boolean headerHelp() {
        System.out.println("用法：cg command file [options]");
        return true;
    }
    protected boolean commandHelp() {
        System.out.println("Commands: ");

        List<String> keys = new ArrayList<String>(SupportCmd.SupportCmds().keySet());
        Collections.sort(keys);
        for (String key : keys) {
            SupportCmd cmd = SupportCmd.SupportCmds().get(key);
            System.out.println(String.format(" %-23s%s", cmd.getCmd(), cmd.getDescription()));
        }
        return true;
    }

    protected boolean optionsHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("");
        formatter.printHelp("Options: ", options());
        return true;
    }

    protected boolean examplesHelp() {
        System.out.println("示例: ");
        System.out.println(" cg msg.json 001.xlsx");
        System.out.println(" cg msg.resx 001.xlsx --sheets Sheet1 Sheet2 Sheet5");

        System.out.println();
        System.out.println("帮助: ");
        System.out.println(String.format(" %-23s显示数生成据库结构的更多帮助信息。", "cg ddl.mysql --help"));
        System.out.println(String.format(" %-23s显示生成初始数据的更多帮助信息。", "cg dml --help"));
        System.out.println(String.format(" %-23s显示生成国际化资源的更多帮助信息。", "cg msg.json --help"));
        System.out.println("通过 cg command --help 查看详细命令。");
        return true;
    }

    protected boolean footerHelp() {
        System.out.println("---");
        System.out.println(Infos.Name + " v" + Infos.Version);
        System.out.println("By " + Infos.Website);
        return true;
    }

    @Override
    public void printHelp() {
        headerHelp();

        if (commandHelp()) System.out.println();
        if (optionsHelp()) System.out.println();
        if (examplesHelp()) System.out.println();
        if (footerHelp()) System.out.println();
    }

    public void printHelp1() {

        System.out.println("用法：cg command file [options]");

        System.out.println();
        System.out.println("command: ");
        List<String> keys = new ArrayList<String>(SupportCmd.SupportCmds().keySet());
        Collections.sort(keys);
        for (String key : keys) {
            SupportCmd cmd = SupportCmd.SupportCmds().get(key);
            System.out.println(String.format(" %-23s%s", cmd.getCmd(), cmd.getDescription()));
        }

        System.out.println();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("options: ");
        formatter.printHelp(" ", options());

        System.out.println();
        System.out.println("通过 cg command --help 查看详细命令。");

        System.out.println();
        System.out.println("examples: ");
        System.out.println("  cg msg json 001.xlsx");
        System.out.println("  cg msg resx 001.xlsx --sheets Sheet1 Sheet2 Sheet5");
        System.out.println();
        System.out.println("帮助：");
        System.out.println("  cg ddl.mysql --help  显示数生成据库结构的更多帮助信息。");
        System.out.println("  cg dml --help  显示生成初始数据的更多帮助信息。");
        System.out.println("  cg msg.json --help  显示生成国际化资源的更多帮助信息。");

        System.out.println();
        System.out.println("---");
        System.out.println(Infos.Name + " v" + Infos.Version);
        System.out.println("By " + Infos.Website);
    }

    protected void printVersion() {
        System.out.println("v" + Infos.Version);
    }

    @Override
    public ParamaModel parsing() {
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        String cmd;
        String file;
        try {
            Options opts = options();
            commandLine = parser.parse(opts, args);
            if (commandLine.hasOption("help")) {
                printHelp();
                return null;
            }
            if (commandLine.hasOption("version")) {
                printVersion();
                return null;
            }

            String[] as = commandLine.getArgs();
            if (as.length < 2) {
                throw new IllegalArgumentException("您输入的参数不正确，请使用 --help 命令查看用法。");
            }
            cmd = as[0];
            file = as[1];

            // 检查参数是否正确
            if (SupportCmd.SupportCmds().get(cmd) == null) {
                throw new IllegalArgumentException(String.format("未知命令: %s，请使用 --help 命令查看用法。", cmd));
            }
            File f = new File(file);
            if(!(f.exists() && f.isFile())) {
                throw new IllegalArgumentException(String.format("指定的文件(%s)不存在或者是一个目录。\r\n", file));
            }

            // 根据命令生成解析
            commandLine = parser.parse(opts, args);
        } catch (NullPointerException | ParseException ex) {
            throw new IllegalArgumentException("您输入的参数不正确，请使用 --help 命令查看用法。");
        }

        ParamaModel model = new ParamaModel();
        model.setCmd(cmd);
        model.setFile(file);
        model.setOptions(commandLine);
        return model;
    }
}
