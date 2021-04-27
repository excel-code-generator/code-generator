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
package com.yanglb.codegen.core.parser;

import com.yanglb.codegen.core.GenFactory;
import com.yanglb.codegen.model.CmdModel;
import com.yanglb.codegen.model.ParameterModel;
import com.yanglb.codegen.utils.Conf;

public interface IParser {
    void setArgs(String[] args);

    /**
     * 显示帮助信息
     */
    void printHelp();

    /**
     * 解析命令数据
     * @return 命令行参数
     */
    ParameterModel parsing() throws IllegalArgumentException;

    static IParser parserByArgs(String[] args) {
        IParser parser;
        try {
            String cmd = args[0];
            CmdModel model = Conf.getCmdModel(cmd);
            parser = GenFactory.createByName(model.getParser());
        } catch (Exception e) {
            parser = new BaseParser();
        }

        parser.setArgs(args);
        return parser;
    }
}
