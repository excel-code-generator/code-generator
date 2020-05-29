package com.yanglb.codegen.core.parser.impl;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class MsgJSONParser extends MsgParser {
    @Override
    protected Options options() {
        Options options = super.options();

        Option group = Option.builder("c")
                .longOpt("combine")
                .desc("是否将内容合并输出，合并后将所有Sheet合并输出。\n否则将以Sheet名为key保存该Sheet下的数据。")
                .build();
        options.addOption(group);

        Option min = Option.builder("m")
                .longOpt("minify")
                .desc("是否压缩输出内容。")
                .build();
        options.addOption(min);

        return options;
    }

    @Override
    protected boolean headerHelp() {
        System.out.println("生成JSON格式的多语言资源。");
        System.out.println("用法：cg msg.json file [options]");
        return true;
    }

    @Override
    protected boolean examplesHelp() {
        return false;
    }
}
