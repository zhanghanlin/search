<%@page import="com.search.service.es.KeywordFacade"%>
<%@page import="com.search.service.bean.Keyword"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.search.service.es.bussiness.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/context.jsp" %>
<%
try{
	Long id = Long.valueOf(request.getParameter("id"));
	KeywordFacadeImpl keywordFacade = (KeywordFacadeImpl)context.getBean(KeywordFacade.BEAN_ID);
    Keyword t = keywordFacade.get(id);
	out.print(JSONObject.toJSONString(t));
}catch(Exception e){
	out.print("{\"info\":\"get "+type+" error : "+e.getMessage()+"\"}");
}
%>