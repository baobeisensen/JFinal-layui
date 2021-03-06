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

package com.qinhailin.portal.core.ctrl;

import java.util.ArrayList;
import java.util.Collection;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.qinhailin.common.base.BaseController;
import com.qinhailin.common.config.WebContant;
import com.qinhailin.common.model.SysOrg;
import com.qinhailin.common.vo.Feedback;
import com.qinhailin.common.vo.TreeNode;
import com.qinhailin.portal.core.service.SysOrgService;
import com.qinhailin.portal.core.service.SysUserService;

/**
 * 部门
 * 
 * @author QinHaiLin
 *
 */
@Path("/portal/core/sysOrg")
public class SysOrgController extends BaseController {

	@Inject
	SysOrgService service;
	@Inject
	SysUserService sysUserService;

	public void index() {
		String str=getPara(0);
		render("index.html");
		if(str!=null)
			render("index_sub_table.html");
	}
	
	public void list(){
		Record record = new Record();
		String showSubOrg=getPara("showSubOrg");
		String orgName=getPara("orgName");

		if(showSubOrg!=null&&orgName!=null&&showSubOrg.equals("1")){
			record.set("org_name like '%"+orgName+"%' or parentid_name like",orgName);			
		}else{		
			record.set("org_name like", getPara("orgName"));
		}
		renderJson(service.queryForList(getParaToInt("pageNumber", 1), getParaToInt("pageSize", 10), record));
	}

	public void add() {
		SysOrg entity = (SysOrg) service.findById(getPara("orgCode"));	
		setAttr("parentid", entity!=null?entity.getId():"sys");
		setAttr("parentIdName", entity!=null?entity.getOrgName():"组织机构");
		render("add.html");
	}

	public void save() {
		SysOrg entity=getBean(SysOrg.class);
		entity.setId(createUUID()).setOrgCode(entity.getId()).save();
		setAttr("sysOrg", entity);
		CacheKit.removeAll("orgManager");
		render("add.html");
	}

	public void edit() {
		setAttr("sysOrg", service.findById(getPara("orgCode")));
		render("edit.html");
	}

	public void update() {
		SysOrg entity=getBean(SysOrg.class);
		entity.update();
		setAttr("sysOrg", entity);
		service.updateOrgParentName(entity.getId());
		CacheKit.removeAll("orgManager");
		render("edit.html");
	}

	public void detail() {
		setAttr("vo", service.findById(getPara("orgCode")));
		render("detail.html");
	}

	
	public void delete() {
		service.deleteOrgById(getPara("orgCode"));
		CacheKit.removeAll("orgManager");
		renderJson(Feedback.success());
	}

	/**
	 * 部门树
	 * 
	 * @author QinHaiLin
	 * @date 2018年10月8日
	 */
	public void tree() {
		Collection<TreeNode> nodeList = CacheKit.get("orgManager", "tree", new IDataLoader() {
			
			@Override
			public Object load() {
				return service.getOrgTree("sys");
			}
		});
		
		Collection<TreeNode> nodes = new ArrayList<TreeNode>();
		TreeNode node = new TreeNode();
		node.setId("sys");
		node.setText("组织机构");
		node.setChildren(nodeList);
		nodes.add(node);

		renderJson(nodes);
	}

	/**
	 * 添加部门和用户，演示主从表录入操作
	 * @author QinHaiLin
	 * @date 2020-06-05
	 */
	public void addOrgUser(){
		SysOrg entity = (SysOrg) service.findById(getPara("orgCode"));
		set("sysOrg",new SysOrg()
				.setId(createUUID())
				.setParentid(entity!=null?entity.getId():"sys")
				.setParentidName(entity!=null?entity.getOrgName():"组织机构")
				.setIsstop(1)
			);
		render("addOrgUser.html");
	}
	
	/**
	 * 保存部门信息（主）
	 * @author QinHaiLin
	 * @date 2020-06-05
	 */
	public void saveFormData(){
		SysOrg entity=getBean(SysOrg.class);
		boolean b=entity.setOrgCode(entity.getId()).save();
		String json=(String) getSession().getAttribute(entity.getId()+"sysUser");
		//主表录入成功才录入从表信息
		if(b&&json!=null){			
			sysUserService.saveUserList(JSONArray.parseArray(json), entity.getId());
			getSession().removeAttribute(entity.getId()+"sysUser");
		}
		setAttr("sysOrg", entity);
		CacheKit.removeAll("orgManager");
		setMsg("数据保存成功");
		render("addOrgUser.html");
	}
	
	/**
	 * 保存用户信息（从表一）
	 * @author QinHaiLin
	 * @date 2020-06-05
	 */
	public void saveTableTata(){
		if("formTable".equals(getPara("type"))){
			sysUserService.saveUserList(JSONArray.parseArray(getPara("tableList")), getPara("orgId"));
		}else{
			//暂存从表数据
			getSession().setAttribute(getPara("orgId")+"sysUser", getPara("tableList"));			
		}
		renderJson(ok());
	}
	
	public void getXmSelectData(){
		String parentId=getPara("parentId","sys");
		String selectData=getPara("selectData","");
		String disabledData=getPara("disabledData","");
		Collection<Record> xmSelectData=service.getXMSelectData(parentId,selectData,disabledData);
		Collection<Record> node=new ArrayList<>();
		Record rd =new Record();
		rd.set("value", "sys");

		if(disabledData.contains("sys")){
			rd.set("disabled", true);
		}
		if(selectData.contains("sys")){
			rd.set("selected", true);
		}
		rd.set("name", "组织机构");
		rd.set("children", xmSelectData);
		node.add(rd);
		renderJson(ok(node));
	}
	 
}
