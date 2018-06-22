package com.aramis.library.http.custom;



/**
 * 
 * @author Wangsg
 * @Description 基础entity
 */
public class BaseEntity {

	private String respCode;  //返回码
	private String respDesc;   //状态
	private String respNo;  //错误描述

	public String getRespNo() {
		return respNo;
	}

	public void setRespNo(String respNo) {
		this.respNo = respNo;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;

	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public boolean isOk() {
//		if (respCode != null && respCode.equals(NetConfig.ResponseCode.OK)) {
//			return true;
//		} else {
			return false;
//		}
	}

	public boolean isAuthTokenInvalid() {
		if ("1039".equals(respNo)) {
			return true;
		} else {
			return false;
		}
	}

}
