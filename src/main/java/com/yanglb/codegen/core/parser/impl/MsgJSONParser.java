/**
 * Copyright 2015-2021 yanglb.com
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

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class MsgJSONParser extends MsgParser {
    @Override
    protected Options options() {
        Options options = super.options();

        Option group = Option.builder("c")
                .longOpt("combine")
                .desc("是否将内容合并输出，合并后将所有Sheet合并输出\n否则将以Sheet名为key保存该Sheet下的数据")
                .build();
        options.addOption(group);

        Option min = Option.builder("m")
                .longOpt("minify")
                .desc("是否压缩输出内容")
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
}
