<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<tiles:useAttribute name="userName" ignore="true"/>
<tiles:useAttribute name="user" ignore="true"/>

<authz:authorize access="!hasRole('ROLE_USER')">
	<p>You are not logged in. &nbsp;<a href="/login" />Login</a></p>
</authz:authorize>
<authz:authorize access="hasRole('ROLE_USER')">
						<p>You are logged in locally as <c:out value="${userName}" />. &nbsp;<a
		href="/logout">Logout</a></p>
		
		<c:forEach var="userConnection" items="${user.userConnections}">
		<c:out value="${userConnection.providerId}" />
		</c:forEach>
		
		
</authz:authorize>