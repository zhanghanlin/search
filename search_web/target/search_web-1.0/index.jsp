<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/context.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Index</title>
</head>
<body>
<input type="text" id="value" />
<input type="button" id="search" value="搜索"/>
<div id="data"></div>
<script type="text/javascript" src="js/jquery-1.11.2.js"></script>
<script>
$('#search').click(function(){
	jumpToSearchBrand();
})

$("#value").bind("input propertychange", function() { 
	jumpToSearchBrand();
}); 
function jumpToSearchBrand(){
	var keyword = $("#value");
	var keywordvalue = $.trim(keyword.val());
	var keyword_utf8 = encodeURIComponent(keywordvalue);
	var url = "interface/associat.jsp?key="+keyword_utf8;
	var h = "";
	$.getJSON(url, function(data) {
		$.each(data,function(i,o) {
			h += o.name + "<br/>";
		});

		$('#data').html(h);
	})
}
</script>
</body>
</html>