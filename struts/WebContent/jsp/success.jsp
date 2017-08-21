<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org /TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 <title>おみくじ結果</title>
</head>
 <body>
<h2>おみくじ結果</h2>
<%= (String)request.getAttribute("message") %>
</body>
</html>