<%@page import="java.io.IOException"%>
<%@page import="org.elasticsearch.ElasticsearchException"%>
<%@page import="org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse"%>
<%@page import="org.elasticsearch.common.io.Streams"%>
<%@page import="org.elasticsearch.client.Client"%>
<%@page import="org.elasticsearch.node.internal.InternalNode"%>
<%@page import="com.search.service.bean.Keyword"%>
<%@page import="com.search.utils.StringUtils"%>
<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="com.search.utils.Constants"%>
<%@page import="com.search.service.job.Jerseys"%>
<%@page import="com.sun.jersey.api.client.WebResource"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/context.jsp" %>
<script type="text/javascript" src="../../js/jquery-1.11.2.js"></script>
<body>
<%
/**************************************/
/*				Init Data	 		  */
/**************************************/
String type = request.getParameter("type");
String o = "";
if(StringUtils.isNotBlank(type)) {
InternalNode esNode = (InternalNode)context.getBean("esNode");
Client esClient = esNode.client();
try {
	String proMapping = Streams
			.copyToStringFromClasspath(Constants.ES_SEARCH_JSON_PATH
					+ "keyword.json");
	try {
		PutMappingResponse proMappingResponse = esClient.admin()
				.indices()
				.preparePutMapping(Constants.GLOBAL_INDEX_NAME)
				.setType("keyword").setSource(proMapping).execute().actionGet();
	} catch (ElasticsearchException e) {
		e.printStackTrace();
	} catch (Exception e) {
		e.printStackTrace();
	}
} catch (IOException e) {
	e.printStackTrace();
}
WebResource client = Jerseys.createClient(Constants.BASE_URL);
int proCount = 100;
for(int i = 0; i < proCount;i++){
	Keyword t = new Keyword();
	int id = StringUtils.randomInt(1000, 9999);
	String word = "p" + StringUtils.randomChinese(5);
	t.setId(Long.valueOf(id));
	t.setWord(word);
	WebResource wr = client.path("/" + Constants.GLOBAL_INDEX_NAME + "/keyword/" + id);
	String pjson = JSON.toJSON(t).toString();
	wr.put(pjson);
}
o = "{\"info\":\"Success\"}";
}
%>
<input type="button" id="init" value="init"/>
<div id="data"><%=o %></div>
<script type="text/javascript" src="../../js/jquery-1.11.2.js"></script>
<script>
$('#init').click(function(){
	window.location.href = 'init.jsp?type=init';
});
</script>
</body>