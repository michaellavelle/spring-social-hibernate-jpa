package org.springframework.social.connect.jpa.hibernate;

import org.springframework.stereotype.Service;

@Service
public class UserConnectionDao extends
		AbstractUserConnectionJpaDao<UserConnection> {

	public UserConnectionDao() {
		super(UserConnection.class);
	}

	@Override
	protected UserConnection createNewUserConnection(String userId,
			String providerId,

			String providerUserId, int rank, String displayName,
			String profileUrl, String imageUrl, String accessToken,
			String secret, String refreshToken, Long expireTime) {
		UserConnection userConnection = new UserConnection();
		setDefaultProperties(userConnection, userId, providerId,
				providerUserId, rank, displayName, profileUrl, imageUrl,
				accessToken, secret, refreshToken, expireTime);
		return userConnection;
	}

}
