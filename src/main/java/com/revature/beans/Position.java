package com.revature.beans;

import java.util.Iterator;
import java.util.Set;

public abstract class Position {
	protected int employeeID;
	protected Set<String> commands;
	
	public Position(int employeeID) {
		this.employeeID = employeeID;
	}
	
	public int getEmployeeID() {
		return employeeID;
	}
	
	public abstract void printMenu();
	
	public abstract boolean processCommand(String s);
	
	public boolean isValidCommand(String s) {
		return commands.contains(s);
	}
}
