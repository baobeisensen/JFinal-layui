package com.qinhailin.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysFunction<M extends BaseSysFunction<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.String getId() {
		return getStr("id");
	}

	public M setFuncName(java.lang.String funcName) {
		set("func_name", funcName);
		return (M)this;
	}
	
	public java.lang.String getFuncName() {
		return getStr("func_name");
	}

	public M setIsStop(java.lang.Integer isStop) {
		set("is_stop", isStop);
		return (M)this;
	}
	
	public java.lang.Integer getIsStop() {
		return getInt("is_stop");
	}

	public M setLinkPage(java.lang.String linkPage) {
		set("link_page", linkPage);
		return (M)this;
	}
	
	public java.lang.String getLinkPage() {
		return getStr("link_page");
	}

	public M setParentCode(java.lang.String parentCode) {
		set("parent_code", parentCode);
		return (M)this;
	}
	
	public java.lang.String getParentCode() {
		return getStr("parent_code");
	}

	public M setParentName(java.lang.String parentName) {
		set("parent_name", parentName);
		return (M)this;
	}
	
	public java.lang.String getParentName() {
		return getStr("parent_name");
	}

	public M setFuncType(java.lang.Integer funcType) {
		set("func_type", funcType);
		return (M)this;
	}
	
	public java.lang.Integer getFuncType() {
		return getInt("func_type");
	}

	public M setIcon(java.lang.String icon) {
		set("icon", icon);
		return (M)this;
	}
	
	public java.lang.String getIcon() {
		return getStr("icon");
	}

	public M setOrderNo(java.lang.Integer orderNo) {
		set("order_no", orderNo);
		return (M)this;
	}
	
	public java.lang.Integer getOrderNo() {
		return getInt("order_no");
	}

	public M setDescript(java.lang.String descript) {
		set("descript", descript);
		return (M)this;
	}
	
	public java.lang.String getDescript() {
		return getStr("descript");
	}

	public M setSpread(java.lang.Integer spread) {
		set("spread", spread);
		return (M)this;
	}
	
	public java.lang.Integer getSpread() {
		return getInt("spread");
	}
	
	public M setIsWindowOpen(java.lang.Integer isWindowOpen) {
		set("is_window_open", isWindowOpen);
		return (M)this;
	}
	
	public java.lang.Integer getIsWindowOpen() {
		return getInt("is_window_open");
	}
}
