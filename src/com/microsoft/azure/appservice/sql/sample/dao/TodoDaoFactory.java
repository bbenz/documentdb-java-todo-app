package com.microsoft.azure.appservice.sql.sample.dao;

public class TodoDaoFactory {
	private static TodoDao myTodoDao = new DocDbDao();

	public static TodoDao getDao() {
		return myTodoDao;
	}
}
