package com.revature.ERS;

import java.util.Scanner;

import com.revature.beans.Employee;
import com.revature.beans.Manager;
import com.revature.beans.Position;
import com.revature.dao.PositionDAO;

public class Driver {
	Position position;
	Scanner sc;
	PositionDAO posDAO;
	
	public Driver() { 
		position = null;
		sc = new Scanner(System.in);
		posDAO = new PositionDAO();
	}
	
	public void runProgram() {
		
		boolean exitFlag = false;
		
		while (position == null)
			userLogin();

		position.printMenu();
		
		while (!exitFlag) {
			
			exitFlag = position.processCommand(getCommand());
		}
		
		System.out.println("Goodbye.");
		sc.close();
	}
	
	public String getCommand() {
		System.out.println("What would you like to do?");
		System.out.print("> ");
		
		String s = sc.nextLine().trim();
		
		return s;
	}
	
	public void userLogin() {
		System.out.print("Enter your username: ");
		String user = sc.nextLine().trim();
		System.out.print("Enter your password: ");
		String password = sc.nextLine().trim();
		
		position = posDAO.checkUserPass(user, password);
	}
}
