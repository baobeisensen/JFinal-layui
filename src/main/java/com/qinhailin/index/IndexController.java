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

package com.qinhailin.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.NotAction;
import com.jfinal.core.Path;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.qinhailin.common.base.BaseController;
import com.qinhailin.common.kit.Md5Kit;
import com.qinhailin.common.model.SysUser;
import com.qinhailin.common.visit.Visitor;
import com.qinhailin.common.visit.VisitorUtil;
import com.qinhailin.portal.core.service.SysFuncService;
import com.qinhailin.portal.core.service.SysUserService;

/**
 * 系统首页
 * 
 * @author qinhailin
 * @date 2018年7月19日
 */
@Path("/")
public class IndexController extends BaseController {

	@Inject
	SysFuncService sysFuncService;
	@Inject
	SysUserService sysUserService;

	/**
	 * 主页方法
	 * 
	 * @author qinhailin
	 * @date 2018年11月15日
	 */
	public void index() {
		//屏蔽无效的url地址
		if(getPara()!=null){
			getResponse().setStatus(404);
			renderError(404);
			return;
		}
		
		if(this.isUnLock()){
			logout();
			return;
		}	
		
		Record record=sysFuncService.getMenuInfo(getVisitor().getCode(),getPara("menuId"));			
		setAttr("funcList", JsonKit.toJson(record.get("funcList")));
		setAttr("topMenuList",record.get("topMenuList"));		
		setAttr("menuId",record.get("menuId"));	
		setAttr("menu",record.get("menu"));
		render("index.html");
	}

	@NotAction
	public boolean isUnLock(){
		// 锁屏未解锁,刷新浏览器强制移除登录身份信息
		String userName = (String) getSession().getAttribute(getVisitor().getName());
		if (userName != null) {
			getSession().removeAttribute(userName);
			VisitorUtil.removeVisitor(getSession());
			return true;
		}
		return false;
	}

	/**
	 * 退出登录
	 * 
	 * @author qinhailin
	 * @date 2018年11月15日
	 */
	public void logout() {    
		VisitorUtil.removeVisitor(getSession());
		redirect("/pub/login");
	}

	/**
	 * 锁屏
	 *
	 * @param userName
	 * @param session
	 * @return
	 * @author QinHaiLin
	 * @date 2018年3月6日下午11:48:34
	 */
	public void lock() {
		Visitor vs = VisitorUtil.getVisitor(getSession());
		if(vs==null){
			renderJson(err("登录信息已失效，请刷新浏览器(F5)重新登录"));
			return;
		}
		getSession().setAttribute(getPara("userName","userName"), getPara("userName","userName"));
		renderJson(suc());
	}

	/**
	 * 解屏
	 *
	 * @param userName
	 * @param userPasswd
	 * @param session
	 * @return
	 * @author QinHaiLin
	 * @date 2018年3月6日下午11:48:34
	 */
	public void unLock() {
		Visitor vs = VisitorUtil.getVisitor(getSession());
		if(vs==null){
			renderJson(err("登录信息已失效，请刷新浏览器(F5)重新登录"));
			return;
		}
		String passwd = Md5Kit.md5(getPara("password"));
		SysUser user = sysUserService.findByUserCode(vs.getCode());
		if (passwd.equals(user.getPasswd())) {
			getSession().removeAttribute(getPara("userName"));
			renderJson(suc());
		} else {
			renderJson(err("密码错误，请重新输入..."));
		}
	}
	
	
	/**
	 * 主题切换
	 */
	public void switchTheme() {
		String theme=getPara("theme");
		Visitor vs = VisitorUtil.getVisitor(getSession());
		String id=vs.getUserData().getId();
		if(sysUserService.updateTheme(id,theme)) {
			renderJson(suc("切换成功"));
		}else {
			renderJson(err("切换失败"));
		}
	}
	
}
