<%@page import="com.search.service.es.ProductFacade"%>
<%@page import="com.search.service.bean.Product"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.search.service.es.bussiness.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/context.jsp" %>
<%
try{
	Long id = Long.valueOf(request.getParameter("id"));
	ProductFacadeImpl productFacade = (ProductFacadeImpl)context.getBean(ProductFacade.BEAN_ID);
    Product t = productFacade.get(id);
	out.print(JSONObject.toJSONString(t));
}catch(Exception e){
	out.print("{\"info\":\"get "+type+" error : "+e.getMessage()+"\"}");
}
%>