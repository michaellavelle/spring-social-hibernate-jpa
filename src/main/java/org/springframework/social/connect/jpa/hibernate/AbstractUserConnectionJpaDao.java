package org.springframework.social.connect.jpa.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.jpa.JpaTemplate;
import org.springframework.social.connect.jpa.RemoteUser;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

public abstract class AbstractUserConnectionJpaDao<U extends AbstractUserConnection<?>>
		implements JpaTemplate {

	private Class<U> persistentClass;

	public Class<U> getPersistentClass() {
		return persistentClass;
	}

	@PersistenceContext(unitName = "persistenceUnit")
	private EntityManager entityManager;

	public AbstractUserConnectionJpaDao(Class<U> persistentClass) {
		this.persistentClass = persistentClass;
	}

	protected String getProviderIdJpql() {
		return "u.primaryKey.providerId";
	}

	protected String getUserIdJpql() {
		return "u.primaryKey.userId";
	}

	protected String getProviderUserIdJpql() {
		return "u.primaryKey.providerUserId";
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Set<String> findUsersConnectedTo(String providerId,
			Set<String> providerUserIds) {
		TypedQuery<String> query = entityManager.createQuery("select "
				+ getUserIdJpql() + " from UserConnection u where "
				+ getProviderIdJpql() + " = :providerId and "
				+ getProviderUserIdJpql() + " in (:providerUserIds)",
				String.class);
		query.setParameter("providerId", providerId);
		query.setParameter("providerUserIds", providerUserIds);
		List<String> users = query.getResultList();

		Set<String> userIds = new HashSet<String>();
		for (String userId : users) {
			if (!userIds.contains(userId)) {
				userIds.add(userId);
			}
		}
		return userIds;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<RemoteUser> getPrimary(String userId, String providerId) {
		TypedQuery<RemoteUser> query = entityManager.createQuery(
				"select u from UserConnection u where " + getUserIdJpql()
						+ " = :userId and " + getProviderIdJpql()
						+ " = :providerId order by u.rank", RemoteUser.class);
		query.setParameter("userId", userId);
		query.setParameter("providerId", providerId);
		return query.getResultList();

	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public int getRank(String userId, String providerId) {

		TypedQuery<Integer> query = entityManager
				.createQuery("select max(u.rank) from UserConnection u where "
						+ getUserIdJpql() + "= :userId and "
						+ getProviderIdJpql() + " = :providerId", Integer.class);
		query.setParameter("userId", userId);
		query.setParameter("providerId", providerId);

		Integer result = query.getSingleResult();
		return result == null ? 1 : (result + 1);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<RemoteUser> getAll(String userId,
			MultiValueMap<String, String> providerUsers) {
		List<RemoteUser> userList = new ArrayList<RemoteUser>();
		for (Map.Entry<String, List<String>> entry : providerUsers.entrySet()) {

			TypedQuery<RemoteUser> query = entityManager.createQuery(
					"select u from UserConnection u where " + getUserIdJpql()
							+ " = :userId and " + getProviderIdJpql()
							+ "= :providerId and " + getProviderUserIdJpql()
							+ " in (:providerUserIds) order by u.rank",
					RemoteUser.class);
			query.setParameter("userId", userId);
			query.setParameter("providerId", entry.getKey());
			query.setParameter("providerUserIds", entry.getValue());
			userList.addAll(query.getResultList());

		}
		return userList;

	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<RemoteUser> getAll(String userId) {

		TypedQuery<RemoteUser> query = entityManager.createQuery(
				"select u from UserConnection u where " + getUserIdJpql()
						+ " = :userId order by u.rank", RemoteUser.class);
		query.setParameter("userId", userId);
		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<RemoteUser> getAll(String userId, String providerId) {
		TypedQuery<RemoteUser> query = entityManager.createQuery(
				"select u from UserConnection u where " + getUserIdJpql()
						+ " = :userId and " + getProviderIdJpql()
						+ "= :providerId order by u.rank", RemoteUser.class);
		query.setParameter("userId", userId);
		query.setParameter("providerId", providerId);
		return query.getResultList();

	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public RemoteUser get(String userId, String providerId,
			String providerUserId) {
		TypedQuery<RemoteUser> query = entityManager.createQuery(
				"select u from UserConnection u where " + getUserIdJpql()
						+ " = :userId and " + getProviderIdJpql()
						+ "= :providerId and " + getProviderUserIdJpql()
						+ " = :providerUserId", RemoteUser.class);
		query.setParameter("userId", userId);
		query.setParameter("providerId", providerId);
		query.setParameter("providerUserId", providerUserId);
		List<RemoteUser> userList = query.getResultList();
		if (userList.size() == 0) {
			throw new EmptyResultDataAccessException(1);
		}
		return userList.get(0);

	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<RemoteUser> get(String providerId, String providerUserId) {
		TypedQuery<RemoteUser> query = entityManager.createQuery(
				"select u from UserConnection u where " + getProviderIdJpql()
						+ " = :providerId and " + getProviderUserIdJpql()
						+ " = :providerUserId order by u.rank",
				RemoteUser.class);
		query.setParameter("providerId", providerId);
		query.setParameter("providerUserId", providerUserId);
		return query.getResultList();

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void remove(String userId, String providerId) {
		for (RemoteUser remoteUser : getAll(userId, providerId)) {
			entityManager.remove(remoteUser);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void remove(String userId, String providerId, String providerUserId) {
		try {
			entityManager.remove(get(userId, providerId, providerUserId));
		} catch (EmptyResultDataAccessException e) {

		}
	}

	protected abstract U createNewUserConnection(String userId,
			String providerId, String providerUserId, int rank,
			String displayName, String profileUrl, String imageUrl,
			String accessToken, String secret, String refreshToken,
			Long expireTime);

	protected void setDefaultProperties(U userConnection, String userId,
			String providerId, String providerUserId, int rank,
			String displayName, String profileUrl, String imageUrl,
			String accessToken, String secret, String refreshToken,
			Long expireTime) {
		userConnection.setUserId(userId);
		userConnection.setProviderId(providerId);
		userConnection.setProviderUserId(providerUserId);

		userConnection.setRank(rank);
		userConnection.setDisplayName(displayName);
		userConnection.setProfileUrl(profileUrl);
		userConnection.setImageUrl(imageUrl);
		userConnection.setAccessToken(accessToken);
		userConnection.setSecret(secret);
		userConnection.setRefreshToken(refreshToken);
		userConnection.setExpireTime(expireTime);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public RemoteUser createRemoteUser(String userId, String providerId,
			String providerUserId, int rank, String displayName,
			String profileUrl, String imageUrl, String accessToken,
			String secret, String refreshToken, Long expireTime) {
		RemoteUser remoteUser = createNewUserConnection(userId, providerId,
				providerUserId, rank, displayName, profileUrl, imageUrl,
				accessToken, secret, refreshToken, expireTime);

		try {
			RemoteUser existingConnection = get(userId, providerId,
					providerUserId);
			if (existingConnection != null)
				throw new DuplicateConnectionException(new ConnectionKey(
						providerId, providerUserId));
		} catch (EmptyResultDataAccessException e) {
		}

		save(remoteUser);
		return remoteUser;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public RemoteUser save(RemoteUser user) {
		entityManager.merge(user);
		return user;
	}

}
