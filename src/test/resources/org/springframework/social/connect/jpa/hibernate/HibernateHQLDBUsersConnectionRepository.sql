create table if not exists USERCONNECTION (userId varchar(255) not null,
	providerId varchar(255) not null,
	providerUserId varchar(255),
	rank int not null,
	displayName varchar(255),
	profileUrl varchar(512),
	imageUrl varchar(512),
	accessToken varchar(255) not null,					
	secret varchar(255),
	refreshToken varchar(255),
	expireTime bigint,
	primary key (userId, providerId, providerUserId));
create unique index if not exists USERCONNECTIONRANK on USERCONNECTION(userId, providerId, rank);