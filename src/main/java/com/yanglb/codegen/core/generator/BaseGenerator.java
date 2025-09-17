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
package com.yanglb.codegen.core.generator;

import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.exceptions.ParamaCheckException;
import com.yanglb.codegen.model.ParameterModel;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;


public class BaseGenerator implements IGenerator {
    protected ParameterModel parameterModel;
    protected HashMap<String, String> settingMap;

    protected void printInfo() {
        System.out.println("生成信息:");
        System.out.printf("%8s: %s\n", "cmd", parameterModel.getCmd());
        System.out.printf("%8s: %s\n", "file", parameterModel.getFile());
        System.out.printf("%8s: \n", "options");
        CommandLine cl = parameterModel.getOptions();
        for (Option opt : cl.getOptions()) {
            String s = opt.getLongOpt();
            if (s == null) s = opt.getOpt();
            if (opt.hasArgs()) {
                System.out.printf("%8s-%s=%s\n", "", s, opt.getValuesList());
            } else if (opt.hasArg()) {
                System.out.printf("%8s-%s=%s\n", "", s, opt.getValue());
            } else {
                System.out.printf("%8s-%s\n", "", s);
            }
        }
        System.out.println();
    }

    /**
     * 初始化
     */
    protected void init(ParameterModel parameterModel) {
        this.parameterModel = parameterModel;
        this.settingMap = new HashMap<>();

        this.settingMap.put("generationDate", new Date().toString());
    }

    @Override
    public final void invoke(ParameterModel parameterModel) throws CodeGenException, ParamaCheckException {
        init(parameterModel);

        printInfo();

        // 生成代码
        onGeneration();
    }

    protected void onGeneration() throws CodeGenException {
        // 子类实现
    }
}
