/**
 * Copyright 2019-2021 覃海林(qinhaisenlin@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package com.qinhailin.common.visit;

import java.io.Serializable;
import java.util.Map;

import com.qinhailin.common.entity.ILoginUser;


/**
 * 访问者接口
 * 
 * @author qinhailin
 * @date 2018-10-23
 */
public interface Visitor extends Serializable {
	public long getLoginTime();

	public ILoginUser getUserData();

	public String getName();

	public String getCode();

	public String getOrgName();

	public Integer getType(); // 获取用户类型
	
	public Map<String,Boolean> getFuncMap();
}
