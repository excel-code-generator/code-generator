/**
 * Copyright 2015-2023 yanglb.com
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
package com.yanglb.codegen.core.parser.impl;

import com.yanglb.codegen.core.parser.BaseParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class DmlParser extends BaseParser {
    @Override
    protected Options options() {
        Options options = super.options();

        Option target = Option.builder()
                .longOpt("target")
                .hasArg()
                .desc("指定生成目标，可选 mysql/mssql/sqlite，默认为mysql")
                .build();
        options.addOption(target);
        return options;
    }

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
