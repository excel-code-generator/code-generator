/**
 * Copyright 2015-2023 yanglb.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanglb.codegen.core.parser.impl;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class MsgResxParser extends MsgParser {
    @Override
    protected Options options() {
        Options options = super.options();

        Option designer = Option.builder()
                .longOpt("designer")
                .desc("是否生成 .Designer.cs 文件，默认不生成。")
                .build();

        Option ns = Option.builder()
                .longOpt("namespace")
                .desc("Designer.cs 名空间。")
                .hasArg()
                .numberOfArgs(1)
                .argName("ns")
                .build();

        Option accessibility = Option.builder()
                .longOpt("accessibility")
                .desc("Designer.cs 代码可访问性，public (默认), private, internal")
                .hasArg()
                .numberOfArgs(1)
                .argName("ab")
                .build();

        options.addOption(designer)
                .addOption(ns)
                .addOption(accessibility);
        return options;
    }

    @Override
    protected boolean headerHelp() {
        System.out.println("生成.NET平台的多语言资源。");
        System.out.println("用法：cg msg.resx file [options]");
        return true;
    }
}
