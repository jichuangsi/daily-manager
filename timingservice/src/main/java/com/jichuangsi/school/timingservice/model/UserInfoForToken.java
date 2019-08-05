/**
 * 作为jwt token的用户信息封装（因为在token中可能不包含用户所有的信息）
 */
package com.jichuangsi.school.timingservice.model;

/**
 * @author huangjiajun
 *
 */
public class UserInfoForToken {
	private String userId;
	private String userNum;
	private String userName;
	private String timeStamp;
	private String roleName;

	public UserInfoForToken() {
	}

	public UserInfoForToken(String userId, String userNum, String userName, String timeStamp) {
		this.userId = userId;
		this.userNum = userNum;
		this.userName = userName;
		this.timeStamp = timeStamp;
	}

	public final String getUserId() {
		return userId;
	}

	public final void setUserId(String userId) {
		this.userId = userId;
	}

	public final String getUserNum() {
		return userNum;
	}

	public final void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public final String getUserName() {
		return userName;
	}

	public final void setUserName(String userName) {
		this.userName = userName;
	}

	public final String getTimeStamp() {
		return timeStamp;
	}

	public final void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
