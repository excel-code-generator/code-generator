package com.yanglb.codegen.core.parser.impl;

import com.yanglb.codegen.core.parser.BaseParser;

public class DmlParser extends BaseParser {
    @Override
    protected boolean headerHelp() {
        System.out.println("生成初始数据SQL脚本，用于向数据添加初始数据。");
        System.out.println("用法：cg dml file [options]");
        return true;
    }

    @Override
    protected boolean commandHelp() {
        return false;
    }

    @Override
    protected boolean examplesHelp() {
        return false;
    }
}
