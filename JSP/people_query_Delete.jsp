<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	int no = request.getParameter("peopleno");
	int phoneno = request.getParameter("phoneno");

		
//------
	String url_mysql = "jdbc:mysql://localhost/address?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";

	int result = 0; // 삭제 확인 

	PreparedStatement ps = null;
	try{
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
	    Statement stmt_mysql = conn_mysql.createStatement();
	
		String A = "DELETE people, phone FROM people INNER JOIN phone ON people.peopleno = phone.people_peopleno WHERE phoneno = ?";
	
		ps = conn_mysql.prepareStatement(A);

        ps.setString(1, phoneno);
 
		result = ps.executeUpdate();
%>
		{
			"result" : "<%=result%>"
		}

<%	
		conn_mysql.close();
	} 
	
	catch (Exception e){
%>
		{
			"result" : "<%=result%>"
		}
<%		
		e.printStackTrace();

	}

%>

