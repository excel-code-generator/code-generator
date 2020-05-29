package com.yanglb.codegen.core.parser.impl;

import com.yanglb.codegen.core.parser.BaseParser;

public class MsgParser extends BaseParser {
    @Override
    protected boolean headerHelp() {
        System.out.println("生成多语言资源信息");
        System.out.println("用法：cg command file [options]");
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
