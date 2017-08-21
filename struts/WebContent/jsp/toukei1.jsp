<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>過去半年と本日の占い結果の割合</title>
</head>
<body>
<h1>過去半年と本日の占い結果の割合</h1>

<span>半年分の占い結果の数:</span>
<br>${resultCount}<span>件</span>
<br>

<h4>過去半年の割合</h4>
<table border ="1">
<tr>
	<c:forEach var="kako" items="${wariai}">
	<tr>
	<td><c:out value="${kako.unsei}"/></td>
	<td><c:out value="${kako.unseiId}%"/></td>
	</tr>
	</c:forEach>
	</tr>
</table>

<h4>今日の割合</h4>
<table border="1">
	<tr>
	<c:forEach var="today" items="${todayRatio}">
	<tr>
	<td><c:out value="${today.unsei}"/></td>
	<td><c:out value="${today.unseiId}%"/></td>
	</tr>
	</c:forEach>
	</tr>
</table>


<a href="<%= request.getContextPath()%>/top.jsp">占い画面に戻る</a>
</body>
</html>