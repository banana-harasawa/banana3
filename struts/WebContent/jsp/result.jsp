<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>あなたの運勢</title>
</head>
<body>

<h3>あなたの運勢</h3>
<table border="1">
<tr>
<td>運勢</td>
<td>願い事</td>
<td>商い</td>
<td>学問</td>
</tr>
<tr>
<td>${message.getUnsei()}</td>
<td>${message.getNegaigoto()}</td>
<td>${message.getAkinai()}</td>
<td>${message.getGakumon()}</td>
</tr>
</table>
<br>
<a href="<%= request.getContextPath() %>/toukei1.do">統計1</a><br>
<a href="<%= request.getContextPath() %>/toukei2.do">統計2</a>
</body>
</html>