<%@page import="org.springframework.jdbc.core.JdbcTemplate"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%
WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
JdbcTemplate jdbcTemplate  = (JdbcTemplate)context.getBean("jdbcTemplate");
%>