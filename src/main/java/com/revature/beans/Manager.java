package com.revature.beans;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import com.revature.dao.PositionDAO;

public class Manager extends Position {

	public Manager(int employeeID) {
		super(employeeID);
		commands = new HashSet<String>(Arrays.asList("view", "update", "add", "exit", "menu"));
	}
	
	public boolean processCommand(String s) {
		if (!isValidCommand(s)) return false;
		
		switch (s) {
		case "view" :
			getManagerView();
			break;
		case "update" :
			System.out.println("You chose to update.");
			break;
		case "add" :
			addEmployee();
			break;
		case "exit" :
			return true;
		}
		
		return false;
	}
	
	public void printMenu() {
		System.out.println();
		System.out.println("To view the reimbursement table type 'view'");
		System.out.println("To update a reimbursement type 'update'");
		System.out.println("To add an employee type 'add'");
		System.out.println("To get menu options type 'menu'");
		System.out.println("To exit the program type 'exit'");
		System.out.println();
	}
	
	private void addEmployee() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Employee's first name?\n> ");
		String fn = sc.nextLine().trim();
		System.out.print("Employee's last name?\n> ");
		String ln = sc.nextLine().trim();
		System.out.print("Employee's username?\n> ");
		String un = sc.nextLine().trim();
		System.out.print("Employee's password?\n> ");
		String pass = sc.nextLine().trim();
		System.out.print("Employee's email?\n> ");
		String email = sc.nextLine().trim();
		System.out.print("Employee's security group?\n> ");
		int sg = sc.nextInt();
		
		PositionDAO pos = new PositionDAO();
		pos.addEmployee(fn, ln, un, pass, email, sg);
	}
	
	private void getManagerView() {
		PositionDAO pos = new PositionDAO();
		
		pos.mangerView();
	}
}
