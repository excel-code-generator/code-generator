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

public class MsgAndroidParser extends MsgParser {
    @Override
    protected Options options() {
        Options options = super.options();

        Option fn = options.getOption("fn");
        fn.setDescription("默认文件名为strings");

        return options;
    }

    @Override
    protected boolean headerHelp() {
        System.out.println("生成Android平台的多语言资源。");
        System.out.println("用法：cg msg.android file [options]");
        return true;
    }
}
