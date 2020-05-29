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
package com.yanglb.codegen.shell;

import com.yanglb.codegen.core.model.ParamaModel;
import com.yanglb.codegen.core.model.ParamaModel2;
import com.yanglb.codegen.shell.parsers.BaseParser;
import com.yanglb.codegen.shell.parsers.MsgJSONParser;
import org.apache.commons.cli.Options;

public interface ICmdParser {
    /**
     * 显示帮助信息
     */
    void printHelp();

    /**
     * 解析命令数据
     * @return 命令行参数
     */
    ParamaModel2 parsing() throws IllegalArgumentException;

    static ICmdParser parserByArgs(String[] args) {
        // TODO: 根据命令生成处理器
        BaseParser parser = new MsgJSONParser();

        parser.setArgs(args);
        return parser;
    }
}
