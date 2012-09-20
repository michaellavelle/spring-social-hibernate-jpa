spring-social-hibernate-jpa
======================================

A UsersConnectionRepository/ConnectionRepository implementation using hibernate entities and JPA Entity Manager for persistence 
as an alternative to the JDBC versions of the repositories in spring-social-core. 

This module provides Hibernate implementations of Spring Social JPA components ( https://github.com/mschipperheyn/spring-social-jpa ) 

To use this implementation in your application:

*1 Add our snapshot repository and dependency to your project, eg. in pom.xml:

```

<repository>
    <id>opensourceagility-snapshots</id>
  <url>http://repo.opensourceagility.com/snapshots</url>
</repository>
...
 <dependency>
        <groupId>org.springframework.social</groupId>
        <artifactId>spring-social-hibernate-jpa</artifactId>
	    <version>1.0.0-SNAPSHOT</version>
</dependency>

```

*2 Component scan for the hibernate implementation of Spring Social JPA, and for the Entity Manager Factory and JPA Transaction manager

```
   <context:component-scan
		base-package="org.springframework.social.connect.jpa.hibernate" />
	
   <import resource="classpath:/spring-social-jpa-hibernate-config.xml" />

```

*3 Replace JdbcUsersConnectionRepository/JdbcConnectionRepository bean configurations with 
JpaUsersConnectionRepository/JpaConnectionRepository implementations.  

The construction of these beans should only need to change in respect to the first argument of the
JpaUsersConnectionRepository constructor, which must be the component-scanned UserConnectionDao implementation of JpaTemplate (registered under the bean name
"userConnectionDao") instead of a DataSource bean.

Your datasource bean will be autowired into the UserConnectionDao instead by the component-scan.

Your configuration should now resemble the following:

```

@Autowired 
private JpaTemplate jpaTemplate:
...


    @Bean
    @Scope(value="singleton", proxyMode=ScopedProxyMode.INTERFACES) 
	public UsersConnectionRepository usersConnectionRepository() {
		return new JpaUsersConnectionRepository(jpaTemplate, connectionFactoryLocator(), Encryptors.noOpText());
	}

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public ConnectionRepository connectionRepository() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
		}
		return usersConnectionRepository().createConnectionRepository(authentication.getName());
}

```

or in xml...

```

    <bean id="usersConnectionRepository"
    	class="org.springframework.social.connect.jpa.JpaUsersConnectionRepository">
		<constructor-arg ref="userConnectionDao" />
		<constructor-arg ref="connectionFactoryLocator" />
		<constructor-arg ref="textEncryptor" />
    </bean>

	<bean
		class="org.springframework.social.connect.jpa.JpaConnectionRepository"
		id="connectionRepository" factory-method="createConnectionRepository"
		factory-bean="usersConnectionRepository" scope="request">
		<constructor-arg value="#{request.userPrincipal.name}" />
		<aop:scoped-proxy proxy-target-class="true" />
	</bean>
```

*4  Ensure you have a persistence.xml file for a persistence unit of name "persistenceUnit" enabled for Hibernate in src/main/resources/META-INF directory.  Add <class>org.springframework.social.connect.jpa.hibernate.UserConnection</class> to your persistence.xml, eg:

```
       <?xml version="1.0" encoding="UTF-8" standalone="no"?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0" xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd">
<persistence-unit name="persistenceUnit" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.ejb.HibernatePersistence</provider>
        <class>org.springframework.social.connect.jpa.hibernate.UserConnection</class> 
        <properties>
            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect"/>
            <!-- value="create" to build a new database on each run; value="update" to modify an existing database; value="create-drop" means the same as "create" but also drops tables when Hibernate closes; value="validate" makes no changes to the database -->
            <property name="hibernate.hbm2ddl.auto" value="create"/>
            <property name="hibernate.connection.charSet" value="UTF-8"/>
            <!-- Uncomment the following two properties for JBoss only -->
            <!-- property name="hibernate.validator.apply_to_ddl" value="false" /-->
            <!-- property name="hibernate.validator.autoregister_listeners" value="false" /-->
        </properties>
    </persistence-unit>
</persistence>
            ....

```

With this configuration, there is no need to create the user connection table, as Hibernate will take care of
ORM for the UserConnection entity, which you can now use amongst any other persistent classes in your application.

This implementation contains an associated Test class for the repositories which subclasses AbstractUsersConnectionRepositoryTest
from https://github.com/michaellavelle/spring-social-core-extension.   This applies the same suite of tests to the
repositories as for the JDBC version from spring-core



spring-social-hibernate-jpa
===========================
