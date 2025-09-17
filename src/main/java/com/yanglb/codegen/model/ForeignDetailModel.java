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
package com.yanglb.codegen.model;


public class ForeignDetailModel {
    // 外键列模型
    private DdlDetail ddlDetail;

    // 外键表模型
    private DdlModel foreignDdlModel;

    // 外键表主键列模型
    private DdlDetail foreignDdlDetail;

    public DdlDetail getDdlDetail() {
        return ddlDetail;
    }

    public void setDdlDetail(DdlDetail ddlDetail) {
        this.ddlDetail = ddlDetail;
    }

    public DdlModel getForeignDdlModel() {
        return foreignDdlModel;
    }

    public void setForeignDdlModel(DdlModel foreignDdlModel) {
        this.foreignDdlModel = foreignDdlModel;
    }

    public DdlDetail getForeignDdlDetail() {
        return foreignDdlDetail;
    }

    public void setForeignDdlDetail(DdlDetail foreignDdlDetail) {
        this.foreignDdlDetail = foreignDdlDetail;
    }


}
