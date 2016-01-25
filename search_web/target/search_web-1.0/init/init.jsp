<%@page import="com.search.utils.StringUtils"%>
<%@page import="com.search.service.job.Job"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/context.jsp" %>
<%
String param_ids = request.getParameter("ids");
if(StringUtils.isNotBlank(param_ids)){
	Job job = (Job)context.getBean("job");
	List<String> idList = new ArrayList<String>();
	try{
		String ids[] = param_ids.split(",");
		for(String i : ids) {
			idList.add(i);
		}
		try{
			job.flushKeyword(idList);
		}catch(Exception e){
			out.print("{\"info\":\"flush error : "+e.getMessage()+"\"}");
		}
		out.print("{\"info\":\"Success\"}");
	}catch(Exception e){
		out.print("{\"info\":\"ids type error : "+e.getMessage()+"\"}");
	}
} else {
	out.print("{\"info\":\"ids is null\"}");
}
%>