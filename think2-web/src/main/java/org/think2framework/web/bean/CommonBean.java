//package org.think2framework.web.bean;
//
//import org.think2framework.core.Constants;
//import org.think2framework.core.persistence.Action;
//import org.think2framework.core.persistence.Column;
//import org.think2framework.core.persistence.Item;
//
///**
// * 通用表的基础字段
// */
//@Action(name = "add", title = "添加", type = "0", href = "/tpl/add", clazz = "purple ace-icon fa fa-plus-circle bigger-130")
//@Action(name = "detail", title = "详情", href = "/tpl/detail", clazz = "ace-icon fa fa-search-plus bigger-130")
//@Action(name = "edit", title = "修改", href = "/tpl/edit", clazz = "green ace-icon fa fa-pencil bigger-130")
//public class CommonBean {
//
//	@Column(title = "主键", nullable = false, length = 11)
//	private Integer id;
//
//	@Column(title = "状态", nullable = false, length = 1, defaultValue = "0")
//	@Item(key = "0", value = "启用")
//	@Item(key = "99", value = "停用")
//	private Integer status;
//
//	@Column(name = "modify_time", title = "修改时间", add = false, edit = false, tag = Constants.FIELD_TIMESTAMP, length = 10, defaultValue = Constants.DEFAULT_NOW)
//	private Integer modifyTime;
//
//	@Column(name = "modify_admin", title = "修改人", add = false, edit = false, defaultValue = Constants.DEFAULT_LOGIN_CODE)
//	private String modifyAdmin;
//
//	@Column(title = "注释", tag = Constants.FIELD_TEXTAREA, display = false, length = 500)
//	private String remark;
//
//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}
//
//	public Integer getStatus() {
//		return status;
//	}
//
//	public void setStatus(Integer status) {
//		this.status = status;
//	}
//
//	public Integer getModifyTime() {
//		return modifyTime;
//	}
//
//	public void setModifyTime(Integer modifyTime) {
//		this.modifyTime = modifyTime;
//	}
//
//	public String getModifyAdmin() {
//		return modifyAdmin;
//	}
//
//	public void setModifyAdmin(String modifyAdmin) {
//		this.modifyAdmin = modifyAdmin;
//	}
//
//	public String getRemark() {
//		return remark;
//	}
//
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}
//}
