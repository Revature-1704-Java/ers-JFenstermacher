package com.revature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revature.beans.Employee;
import com.revature.beans.Manager;
import com.revature.beans.Position;
import com.revature.util.ConnectionUtil;

public class PositionDAO {
	
	public Position checkUserPass(String user, String pass) {
		PreparedStatement ps = null;
		Position p = null;
		int sg = 0;
		
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM employees WHERE employees.username = ? AND employees.password = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user);
			ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();
			
			if (!rs.next())
				System.out.println("There is no account with that username/password");
			else {
				sg = rs.getInt("securitygroup");
				int eID = rs.getInt("employeeID");
				
				if (sg == 1) p = new Manager(eID);
				else p = new Employee(eID);
				
			}
			
			ps.close();
			rs.close();
				
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return p;
	}
	
	public void addEmployee(String fn, String ln, String un, String pass, String email, int sg) {
		CallableStatement cs = null;
		
		try (Connection conn = ConnectionUtil.getConnection()){
			String sql = "{CALL ADD_EMPLOYEE(?, ?, ?, ?, ?, ?)}";
			cs = conn.prepareCall(sql);
			cs.setString(1, fn);
			cs.setString(2, ln);
			cs.setString(3, un);
			cs.setString(4, pass);
			cs.setString(5, email);
			cs.setInt(6, sg);
			
			cs.execute();
			
			cs.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void addReimbursement(int eid, float amt) {
		CallableStatement cs = null;
		
		try (Connection conn = ConnectionUtil.getConnection()){
			String sql = "{CALL ADD_REIMBURSEMENTS(?, ?, ?)}";
			cs = conn.prepareCall(sql);
			cs.setInt(1, eid);
			cs.setFloat(2, amt);
			cs.setString(3, "PENDING");
			
			cs.execute();
			
			cs.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void updateReimbursement(int rid, String decision) {
		CallableStatement cs = null;
		
		try (Connection conn = ConnectionUtil.getConnection()){
			String sql = "{CALL UPDATE_REIMBURSEMENT(?, ?)}";
			cs.setInt(1, rid);
			cs.setString(2, decision);
			
			cs.execute();
			
			cs.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void viewCurrentReimbursements(int eid) {
		PreparedStatement ps = null;
		
		try (Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM reimbursements WHERE employeeid = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, eid);
			
			ResultSet rs = ps.executeQuery();
			
			System.out.println();
			System.out.println("\t   YOUR REIMBURSEMENTS");
			System.out.println("ReimbursementID |   Amount   | Approval");
			
			while (rs.next()) {
				int reimbID = rs.getInt("reimbursementID");
				float amt = rs.getFloat("Amount");
				String appr = rs.getString("Approved");
				System.out.println(String.format("%15d | %10.2f | %8s", reimbID, amt, appr));
			}
			ps.close();
			rs.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void mangerView() {
		PreparedStatement ps = null;
		
		try (Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM manager";
			ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			System.out.println();
			System.out.println("\t\t REMIMBURSEMENT TABLE");
			System.out.println("EmployeeID | Total Reimbursement | Approval Needed");
			
			while (rs.next()) {
				int eid = rs.getInt("employeeid");
				float total = rs.getFloat("total");
				int approval = rs.getInt("need_approval");
				String app = approval == 1 ? "YES" : "NO";
				System.out.println(String.format("%10d | %19.2f | %15s", eid, total, app));
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
