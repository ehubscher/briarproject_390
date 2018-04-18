package org.briarproject.bramble.restClient.ServerObj;

/**
 * Created by winterhart on 4/12/18.
 * This object will store the preferences for a user
 */

public class PreferenceUser {
	private String username;
	private int avatarId;
	private int statusId;

	public PreferenceUser(String user, int statusid, int avatarid){
		this.username = user;
		this.statusId = statusid;
		this.avatarId = avatarid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(int avatarId) {
		this.avatarId = avatarId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
}
