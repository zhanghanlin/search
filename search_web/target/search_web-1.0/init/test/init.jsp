<%@page import="java.io.IOException"%>
<%@page import="org.elasticsearch.ElasticsearchException"%>
<%@page import="org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse"%>
<%@page import="org.elasticsearch.common.io.Streams"%>
<%@page import="org.elasticsearch.client.Client"%>
<%@page import="org.elasticsearch.node.internal.InternalNode"%>
<%@page import="com.search.service.bean.Product"%>
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
					+ "product.json");
	String brandMapping = Streams
			.copyToStringFromClasspath(Constants.ES_SEARCH_JSON_PATH
					+ "brand.json");
	try {
		PutMappingResponse proMappingResponse = esClient.admin()
				.indices()
				.preparePutMapping(Constants.GLOBAL_INDEX_NAME)
				.setType("product").setSource(proMapping).execute().actionGet();
		PutMappingResponse brandMappingResponse = esClient.admin()
				.indices()
				.preparePutMapping(Constants.GLOBAL_INDEX_NAME)
				.setType("brand").setSource(brandMapping).execute().actionGet();
	} catch (ElasticsearchException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
WebResource client = Jerseys.createClient(Constants.BASE_URL);
int proCount = 100;
for(int i = 0; i < proCount;i++){
	Product t = new Product();
	int id = StringUtils.randomInt(1000, 9999);
	String name = "p" + StringUtils.randomChinese(5);
	t.setId(Long.valueOf(id));
	t.setName(name);
	WebResource wr = client.path("/" + Constants.GLOBAL_INDEX_NAME + "/product/" + id);
	String pjson = JSON.toJSON(t).toString();
	wr.put(pjson);
}

int brandCount = 100;
for(int i = 0; i < brandCount;i++){
	Brand t = new Brand();
	int id = StringUtils.randomInt(1000, 9999);
	String name ="b" +  StringUtils.randomChinese(4);
	t.setId(Long.valueOf(id));
	t.setName(name);
	WebResource wr = client.path("/" + Constants.GLOBAL_INDEX_NAME + "/brand/" + id);
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