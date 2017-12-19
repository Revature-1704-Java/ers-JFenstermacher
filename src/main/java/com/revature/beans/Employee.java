package com.revature.beans;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import com.revature.dao.PositionDAO;

public class Employee extends Position {
	
	public Employee(int employeeID) {
		super(employeeID);
		commands = new HashSet<String>(Arrays.asList("create", "check", "exit", "menu"));
	}
	
	public void makeReimbursementClaim() {
		PositionDAO posDAO = new PositionDAO();
		Float moneys = null;
		
		Scanner sc = new Scanner(System.in);
		
		while (moneys == null) {
			System.out.println("How much is the reimbursement for? ");
			System.out.print("> ");
			try {
				String tmp = sc.nextLine();
				moneys = Float.parseFloat(tmp);
			} catch (Exception ex) {
				System.out.println("That's an invalid amount.");
			}
		}
		
		posDAO.addReimbursement(getEmployeeID(), moneys);
	}
	
	public boolean processCommand(String s) {
		if (!isValidCommand(s)) { 
			System.out.println("That's not a valid command");
			return false;
		}
		
		switch (s) {
			case ("create") :
				makeReimbursementClaim();
				break;
			case ("check") :
				getCurrentReimbursements();
				break;
			case ("menu") :
				printMenu();
				break;
			case ("exit") :
				return true;
			default :
				System.out.println("Command not recognized");
		}
		
		return false;
	}
	
	public void printMenu() {
		System.out.println();
		System.out.println("To make a new reimbursement type 'create'");
		System.out.println("To check your current reimbursements type 'check'");
		System.out.println("To get menu options type 'menu'");
		System.out.println("To quit type 'quit'");
		System.out.println();
	}
	
	private void getCurrentReimbursements() {
		PositionDAO posDAO = new PositionDAO();
		
		posDAO.viewCurrentReimbursements(employeeID);
	}
}
