package org.springframework.social.connect.jpa.hibernate;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.springframework.social.connect.jpa.RemoteUser;

/*
 * Copyright 2011 the original author or authors.
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

/**
 * @author Michael Lavelle
 */
@MappedSuperclass
public abstract class AbstractUserConnection<P> implements RemoteUser,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String accessToken;
	private String displayName;
	private Long expireTime;
	private String imageUrl;
	private String profileUrl;
	private int rank;
	private String refreshToken;
	private String secret;


	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public abstract String getProviderId();

	public abstract void setProviderId(String providerId);

	public abstract String getProviderUserId();

	public abstract void setProviderUserId(String providerUserId);

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public abstract String getUserId();

	public abstract void setUserId(String userId);

	protected abstract P getId();
}
