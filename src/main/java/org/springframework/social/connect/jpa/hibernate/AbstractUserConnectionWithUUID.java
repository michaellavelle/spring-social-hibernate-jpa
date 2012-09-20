package org.springframework.social.connect.jpa.hibernate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = { "userId", "providerId",
				"providerUserId" }),
		@UniqueConstraint(columnNames = { "userId", "providerId", "rank" }) })
public abstract class AbstractUserConnectionWithUUID extends
		AbstractUserConnection<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;
	private String providerId;
	private String providerUserId;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(updatable = false, nullable = false)
	protected String id;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
