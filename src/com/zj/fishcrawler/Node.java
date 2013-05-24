package com.zj.fishcrawler;

public class Node {

	private String id;
	private String cname;
	private String ename;
	private String parentId;
	private String remark;
	private int nodeType;

	public int getNodeType() {
		return nodeType;
	}
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public Node() {
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Node(String id, String cname, String ename, String parentId, int nodeType) {
		this.id = id;
		this.cname = cname;
		this.ename = ename;
		this.parentId = parentId;
		this.nodeType = nodeType;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Override
	public String toString() {
		return "Node [id=" + id + ", cname=" + cname + ", ename=" + ename
				+ ", parentId=" + parentId + "]";
	}
	
}
