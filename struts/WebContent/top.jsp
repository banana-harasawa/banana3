<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>トップ画面</title>
</head>
<body>

<span>誕生日を入力してください(例：19990909)</span>
<form action="result.do" method="post">
	<input type="text" name="birthday"/>
	<input type="submit" value="おみくじを引く"/>
	<div style="color:red">
	<html:errors />
	</div>
</form>
</body>
</html>