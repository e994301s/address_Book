<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	int no = request.getParameter("peopleno");
	String name = request.getParameter("peoplename");
	String email = request.getParameter("peopleemail");
	String relation = request.getParameter("peoplerelation");
	String memo = request.getParameter("peoplememo");
	String phonetel = request.getParameter("phonetel");
	int phoneno = request.getParameter("phoneno");
		
//------
	String url_mysql = "jdbc:mysql://localhost/address?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";

	PreparedStatement ps = null;
	try{
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
	    Statement stmt_mysql = conn_mysql.createStatement();
	
		
		String A = "UPDATE people INNER JOIN phone ON peopleno = people_peopleno"
		String B = " SET people.peoplename = ?, people.peopleemail = ?, people.peoplerelation = ?, people.peoplememo = ?, phone.phonetel = ?
		String C = " WHERE phoneno = ?; 
	
	    ps = conn_mysql.prepareStatement(A+B+C);
	    ps.setString(1, name);
	    ps.setString(2, email);
	    ps.setString(3, relation);
		ps.setString(4, memo);
		ps.setString(5, phonetel);
		ps.setInt(6, phoneno)
	    
	    ps.executeUpdate();
	
	    conn_mysql.close();
	} 
	
	catch (Exception e){
	    e.printStackTrace();
	}
	
%>

