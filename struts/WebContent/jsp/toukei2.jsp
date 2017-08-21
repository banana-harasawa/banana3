<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>登録された誕生日の過去半年の結果</title>
</head>
<body>

<h1>登録された誕生日の過去半年の結果（リスト表示）</h1>

<h4>過去半年の割合</h4>
<table border="1">
<tr>
<td>日付</td>
<td>運勢</td>
<td>願い事</td>
<td>商い</td>
<td>学問</td>

</tr>
<tr>
	<c:forEach var="kako" items="${omi}">
	<tr>
	<td>
		<c:out value="${kako.today}　"/>
	</td>
	<td>
		<c:out value="${kako.unsei}　"/>
	</td>
	<td>
		<c:out value="${kako.negaigoto}　"/>
	</td>
	<td>
		<c:out value="${kako.akinai}　"/>
	</td>
	<td>
		<c:out value="${kako.gakumon}"/>
	</td>
	</tr>
	</c:forEach>
</tr>


</table>

<a href="<%= request.getContextPath()%>/top.jsp">占い画面に戻る</a>

</body>
</html>