package org.springframework.social.connect.jpa.hibernate;

public abstract class AbstractUserConnectionWithUUIDJpaDao<U extends AbstractUserConnectionWithUUID>
		extends AbstractUserConnectionJpaDao<U> {

	public AbstractUserConnectionWithUUIDJpaDao(Class<U> persistentClass) {
		super(persistentClass);
	}

	protected String getProviderIdJpql() {
		return "u.providerId";
	}

	protected String getUserIdJpql() {
		return "u.userId";
	}

	protected String getProviderUserIdJpql() {
		return "u.providerUserId";
	}

}
