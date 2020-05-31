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
package com.yanglb.codegen.core.parser.impl;

import com.yanglb.codegen.utils.Conf;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class DdlMySqlParser extends DdlParser {
    @Override
    protected Options options() {
        Options options = super.options();

        Option engine = Option.builder()
                .longOpt("engine")
                .hasArg()
                .desc(String.format("指定MySql Engine，默认为 %s", Conf.getSetting("mysql.engine")))
                .build();
        options.addOption(engine);

        Option charset = Option.builder()
                .longOpt("charset")
                .hasArg()
                .desc(String.format("指定MySql Default Charset，默认为 %s", Conf.getSetting("mysql.charset")))
                .build();
        options.addOption(charset);

        return options;
    }

    @Override
    protected boolean headerHelp() {
        System.out.println("生成MySql数据库结构SQL脚本。");
        System.out.println("用法：cg ddl.mysql file [options]");
        return true;
    }
}
