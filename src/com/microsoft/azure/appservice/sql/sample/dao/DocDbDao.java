package com.microsoft.azure.appservice.sql.sample.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.Connection;
import javax.sql.DataSource;

import com.microsoft.azure.appservice.sql.sample.model.TodoItem;

import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;

public class DocDbDao implements TodoDao {

    @Override
    public TodoItem createTodoItem(TodoItem todoItem) {
    	
    	try {
        	// create a connection to the database from the data source
        	Context ctx = new InitialContext();
        	DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/todoItemDb");
        	
        	// insert the todo item fields into a new row in the todo item table
        	Connection conn = ds.getConnection();
        	
        	todoItem.setId(UUID.randomUUID().toString());
        	
        	String insertQuery = "INSERT INTO ITEMS (id, name, category, complete) "
        			+ "VALUES (\""+todoItem.getId()+"\", \""+ 
        			todoItem.getName() + "\", \""+ todoItem.getCategory() +
        			"\"," + todoItem.isComplete() + ");";
        	System.out.println(insertQuery);
        	Statement insertStatement = conn.createStatement();
        	insertStatement.execute(insertQuery);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return todoItem;
    }

    @Override
    public TodoItem readTodoItem(String id) {
        // Retrieve the todo item with the given id from the database
    	
    	try {
        	// create a connection to the database from the data source
        	Context ctx = new InitialContext();
        	DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/todoItemDb");
        	
        	// select the todo item from the table. If no matching item, return null
        	Connection conn = ds.getConnection();
        	String selectIdQuery = "SELECT * FROM ITEMS WHERE ID="+id+";";
        	Statement selectStatement = conn.createStatement();
        	ResultSet rs = selectStatement.executeQuery(selectIdQuery);

        	
        	if (rs != null)
        	{
        		return TodoItem.builder().id("id").name("name").category("category").complete(true).build();
        	} else {
        		return null;
        	}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    @Override
    public List<TodoItem> readTodoItems() {
        List<TodoItem> todoItems = new ArrayList<TodoItem>();

    	try {
        	// create a connection to the database from the data source
        	Context ctx = new InitialContext();
        	DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/todoItemDb");
        	
        	// select all todo items from the table. If no matching item, return null
        	Connection conn = ds.getConnection();
        	String selectIdQuery = "SELECT * FROM ITEMS;";
        	Statement selectStatement = conn.createStatement();
        	ResultSet rs = selectStatement.executeQuery(selectIdQuery);
        	
        	if (rs != null)
        	{
        		while(rs.next()) {
        			todoItems.add(TodoItem.builder().id(rs.getString(1)).name(rs.getString(2)).
        						category(rs.getString(3)).complete("1".equals(rs.getString(4))).build());
        		}
        		return todoItems;
        	} else {
        		return null;
        	}
    	} catch(Exception e) { 
    		e.printStackTrace();
    		return null;
    	}
    }

    @Override
    public TodoItem updateTodoItem(String id, boolean isComplete) {
    	
    	try {
        	// create a connection to the database from the data source
        	Context ctx = new InitialContext();
        	DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/todoItemDb");
        	
        	// update the todo item in the database
        	Connection conn = ds.getConnection();
        	String updateQuery = "UPDATE ITEMS SET complete = " + isComplete + " WHERE id = \'" + id + "\';";
        	Statement updateStatement = conn.createStatement();
        	boolean updated = updateStatement.execute(updateQuery);
        	
        	if (updated) {
        		return readTodoItem(id);
        	}
        	else {
        		return null;
        	}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	} 
    }

    @Override
    public boolean deleteTodoItem(String id) {

    	try {
        	// create a connection to the database from the data source
        	Context ctx = new InitialContext();
        	DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/todoItemDb");
        	
        	// delete the todo item in the database
        	Connection conn = ds.getConnection();
        	String deleteQuery = "DELETE FROM ITEMS WHERE id = " + id + ";";
        	Statement deleteStatement = conn.createStatement();
        	boolean isDeleted = deleteStatement.execute(deleteQuery);
        	deleteStatement.close();
        	conn.close();
        	
        	return isDeleted;

    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }
}