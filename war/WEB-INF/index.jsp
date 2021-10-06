<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head><title>Anna Accounting</title></head>
  <body>
    <div>
        Please enter your Google Apps domain to log in.<br/>

        <form action="<c:url value="/openid"/>" method="get">
            <div class="field">
                <label for="domain_field">Google Apps Domain</label><input id="domain_field" type="text" name="hd"/>
            </div>
            <div class="field">
                <input type="submit" value="Log in">
            </div>
        </form>
    </div>
    <%@ include file="./scripts.jsp" %>
  </body>
</html>