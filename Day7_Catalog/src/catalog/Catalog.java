package catalog;
import java.sql.*;
import java.util.*;

public class Catalog {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/Catalog";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "46619962";
	
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		String action = "query";
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			
			switch(action){
			case "create":
				//STEP 4: Execute a query
				System.out.println("Creating tables in given database...");
				stmt = conn.createStatement();
				String sql = "CREATE TABLE Suppliers (sid INTEGER, sname VARCHAR(255), address VARCHAR(255), PRIMARY KEY ( sid ))"; 
				stmt.executeUpdate(sql);
				System.out.println("Created table Suppliers in given database...");
				sql = "CREATE TABLE Parts (pid INTEGER, pname VARCHAR(255), color VARCHAR(255), PRIMARY KEY ( pid ))";
				stmt.executeUpdate(sql);
				System.out.println("Created table Parts in given database...");
				sql = "CREATE TABLE Catalog (sid INTEGER, pid INTEGER, cost REAL, PRIMARY KEY ( cost ))";
				stmt.executeUpdate(sql);
				System.out.println("Created table Catalog in given database...");
				break;
				
			case "fill":
				stmt = conn.createStatement();
		        stmt.executeUpdate("insert into Suppliers values(1,'Sauron','Mordor')");
		        stmt.executeUpdate("insert into Suppliers values(2,'Elrond','Rivendell')");
		        stmt.executeUpdate("insert into Suppliers values(3,'Saruman','Isengard')");
		        
		        stmt.executeUpdate("insert into Parts values(1,'Armor','Black')");
		        stmt.executeUpdate("insert into Parts values(2,'Spear','Brown')");
		        stmt.executeUpdate("insert into Parts values(3,'Ring','Gold')");
		        stmt.executeUpdate("insert into Parts values(4,'Shield','Green')");
		        stmt.executeUpdate("insert into Parts values(5,'Cloak','Green')");
		        stmt.executeUpdate("insert into Parts values(6,'Staff','White')");
		        stmt.executeUpdate("insert into Parts values(7,'Sword','Silver')");
		        stmt.executeUpdate("insert into Parts values(8,'Potion','Red')");
		        stmt.executeUpdate("insert into Parts values(9,'Leaf','Green')");
		        
		        stmt.executeUpdate("insert into Catalog values(1,1,'500')");
		        stmt.executeUpdate("insert into Catalog values(1,2,'150')");
		        stmt.executeUpdate("insert into Catalog values(1,3,'25000')");
		        stmt.executeUpdate("insert into Catalog values(2,4,'250')");
		        stmt.executeUpdate("insert into Catalog values(2,5,'80')");
		        stmt.executeUpdate("insert into Catalog values(3,6,'320')");
		        stmt.executeUpdate("insert into Catalog values(3,7,'300')");
		        stmt.executeUpdate("insert into Catalog values(3,8,'25')");
		        stmt.executeUpdate("insert into Catalog values(3,9,'15')");
				break;
				
			case "query":
				stmt = conn.createStatement();
//				STEP 4: Execute a query
				System.out.println("Getting Catalog info...\n");
				sql = "SELECT pid, pname, cost, sname, sid, address FROM catalog,parts,suppliers WHERE catalog.prodId = parts.pid AND catalog.suppId = suppliers.sid";
				ResultSet rs = stmt.executeQuery(sql);
				
				List<String> prodName = new ArrayList<>();
				List<String> suppName = new ArrayList<>();
				List<String> sAddress = new ArrayList<>();
				List<String> pid = new ArrayList<>();
				List<String> sid = new ArrayList<>();
				List<String> cost = new ArrayList<>();

				//STEP 5: Extract data from result set
				while(rs.next()){
					//Retrieve by column name
					pid.add(String.valueOf(rs.getInt("pid")));
					sid.add(String.valueOf(rs.getInt("sid")));
					cost.add("$"+String.valueOf(rs.getFloat("cost")));
					prodName.add(rs.getString("pname"));
					suppName.add(rs.getString("sname"));
					sAddress.add(rs.getString("address"));
				}
				prodName = Catalog.homogenize(prodName);
				suppName = Catalog.homogenize(suppName);
				sAddress = Catalog.homogenize(sAddress);
				pid = Catalog.homogenize(pid);
				sid = Catalog.homogenize(sid);
				cost = Catalog.homogenize(cost);
				
				for(int i=0;i<pid.size();i++){
					System.out.println("("+pid.get(i)+") "+prodName.get(i)+" - "+
							cost.get(i)+" - Supplier: "+suppName.get(i)+
							"("+sid.get(i)+") - Address: "+sAddress.get(i));
				}
				rs.close();
				break;
				
			case "modifiy":
				
			}
			stmt.close();
			conn.close();
			
			
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
		System.out.println("Goodbye!");
	}//end main
	
	
	public static List<String> homogenize(List<String> set){
		int maxLength = 0;
		for(int i=0;i<set.size();i++){
			if(set.get(i).length()>maxLength){
				maxLength = set.get(i).length();
			}
		}
		List<String> retSet = new ArrayList<>();
		for(int i=0;i<set.size();i++){
			String aux = set.get(i);
			for (int ii = aux.length(); ii < maxLength; ii++){
				aux += " ";
			}
			retSet.add(aux);
		}
		return retSet;
	}
}
