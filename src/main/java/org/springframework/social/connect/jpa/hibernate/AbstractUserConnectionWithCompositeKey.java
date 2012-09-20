package org.springframework.social.connect.jpa.hibernate;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "userId",
		"providerId", "rank" }) })
public abstract class AbstractUserConnectionWithCompositeKey extends
		AbstractUserConnection<UserConnectionPK> {

	@Id
	private UserConnectionPK primaryKey = new UserConnectionPK();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getProviderId() {
		return primaryKey.getProviderId();
	}

	@Override
	public void setProviderId(String providerId) {
		primaryKey.setProviderId(providerId);
	}

	@Override
	public String getProviderUserId() {
		return primaryKey.getProviderUserId();
	}

	@Override
	public void setProviderUserId(String providerUserId) {
		primaryKey.setProviderUserId(providerUserId);
	}

	@Override
	public String getUserId() {
		return primaryKey.getUserId();
	}

	@Override
	public void setUserId(String userId) {
		primaryKey.setUserId(userId);
	}

	@Override
	protected UserConnectionPK getId() {
		return primaryKey;
	}

}
