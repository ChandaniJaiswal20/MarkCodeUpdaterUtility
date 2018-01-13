package com.wipo.markcode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;


public class DBdataProcessor {
	
	UtilityConfiguration  uc	=	null;
	
	
	private String databaseHost	= null; 
	private String databasePort	=	null;
	private String databaseTool	=	null;
	private String databaseName	=	null;
	private	String userName		=	null;
	private	String password		=	 null;
	private	Connection connection	=	null;
	
	public static Logger logger	=	Logger.getLogger(DBdataProcessor.class);
	
	
	
	
	public DBdataProcessor(String databaseHost, String databasePort,
			String databaseTool, String databaseName,String userName,String password ){
		
		
		 this.databaseHost	= databaseHost; 
		 this.databasePort	=	databasePort;
		 this.databaseTool	=	databaseTool;
		 this.databaseName	=	databaseName;
		 this.userName		=	userName;
		 this.password		=	 password;
		 
			init();
		
	}
	
	public void init(){
		
		if(connection==null)
			configureConnection();
		
	}
	
	public Connection getConnection(){
		
		return connection;
	}
	
	
	public void closeConnection(){
		
		 if(connection!=null){
		 
		 try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
		 
		 
		 
		  
		 
	 }
		
	}
	
	
	
	public  void 	configureConnection() {
		// TODO Auto-generated method stub
		//Connection conn = null;
		if( databaseTool.equalsIgnoreCase("oracle") ){
			
			logger.info("Database Type is Oracle....");
			
		//("jdbc:oracle:thin:@" + databaseHost + ":" + databasePort + ":"+databaseName,userName,password  );
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection	=	DriverManager.getConnection("jdbc:oracle:thin:@" + databaseHost + ":" + databasePort + ":"+databaseName,userName,password  );
			
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			
			logger.error("Error Occured while loading OracleDriver class ", e);
			//e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Error Occured while obtaining connection ", e);
		//	e.printStackTrace();
		}
		}
		else if(databaseTool.equalsIgnoreCase("sql")){
			
			logger.info("Database Type is MSSQL....");
			
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				
				logger.info("Driver class loaded successfully....");
				
				connection	=	DriverManager.getConnection( "jdbc:sqlserver://"+databaseHost+ ":" + databasePort +";" +
			    		  "databaseName="+databaseName+";"+"user="+userName+";"+"password="+password);
				logger.info("Driver class loaded successfully....");
			} catch (ClassNotFoundException e) {
				logger.error("Error Occured while loading SQLDriver class ", e);
			//	e.printStackTrace();
			} catch (SQLException e) {
				logger.error("Error Occured While obtaining  Connection: ", e);
			//	e.printStackTrace();
			}
			
			
		}
		
		
	
		
		
	}
	
	
	
	

	

	public   ArrayList <MarkEditImpl> readMarksFromDB(String signWcode) {
		// TODO Auto-generated method stub
		
//		HashMap	<String,String> mark_detail_map	=	new HashMap<String ,String>();
		ArrayList <MarkEditImpl> list_of_mark=	new ArrayList<MarkEditImpl>();
		PreparedStatement ps	=	null;
		ResultSet rs				=	null;
	
		String query	=	"SELECT  FILE_NBR,FILE_SEQ,FILE_SER,FILE_TYP FROM IP_MARK WHERE SIGN_WCODE = ? and MARK_CODE is null ";
			
		try {
			ps	=		connection.prepareStatement(query);
				
			ps.setString(1, signWcode);
			
			rs	=	  ps.executeQuery();
			
			   while (rs.next()) {
				   MarkEditImpl mark	=	 new MarkEditImpl();
				   mark.setFile_nbr(rs.getInt("FILE_NBR"));
				   mark.setFile_seq(rs.getString("FILE_SEQ"));
				   mark.setFile_ser( rs.getInt("FILE_SER"));
				   mark.setFile_typ( rs.getString("FILE_TYP"));
				
			    	  
			    	  list_of_mark.add(mark);
			      }
					
		logger.debug("Total number of marks with problem::"+list_of_mark.size() );
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Error Occured while Reading mark_detail from ip_mark table. ", e);
			e.printStackTrace();
		}
		 
			finally{
				 if (rs != null){
					 
					 try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 
				 
				 if(ps!=null){
				 
					 try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 
		
		
		}
		
		return list_of_mark; 
	}

	public boolean insertIntoSqlDatabase(String sequence) {
		// TODO Auto-generated method stub
				
				Statement ps	=	null;
				ResultSet rs				=	null;
				int row	=	0;
				String query	=	"insert into "+sequence+ " values('') " ;
				boolean isInsertedIntoSQLDatabase	=	false;	
				try {
					ps	=		connection.createStatement();
					row = 	ps.executeUpdate(query);
					if(row>0){
						isInsertedIntoSQLDatabase	=	true;
						logger.debug("Row inserted in "+sequence+" successfully");
				 	} 	
			
								
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error("Error Occured while inserting row in "+ sequence +" ", e);
					
					e.printStackTrace();
				}
				 
			finally{
				 if (rs != null){
					 
					 try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 
				 
				 if(ps!=null){
				 
					 try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 
		
		
		}
				return isInsertedIntoSQLDatabase;
		
		
	}

	public int selectNextValuefromSQL() {
		// TODO Auto-generated method stub
		
		Statement ps	=	null;
		ResultSet rs				=	null;
		
		int nextValue	=	0;
	
		String query	=	"select @@identity as markCode";
			
		try {
			ps	=		connection.createStatement();
				
			
			
			rs	=	  ps.executeQuery(query);
			
			   while (rs.next()) {
				   
				  	nextValue	=	 rs.getInt("markCode");//change
				   	  
			      }
					
		
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Error Occured while selecting nextvalue from identity. ", e);
			e.printStackTrace();
		}
		 
			finally{
				 if (rs != null){
					 
					 try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 
				 
				 if(ps!=null){
				 
					 try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 
		
		
		}
		
		return nextValue; 
	}

	public int selectNextValuefromOracle() {
		// TODO Auto-generated method stub
		

		Statement ps	=	null;
		ResultSet rs				=	null;
		
		int nextMarkCode	=	0;
	
		String query	=	"SELECT SEC_DENOMINACION.NEXTVAL  FROM DUAL";
			
		try {
			ps	=		connection.createStatement();
				
			
			
			rs	=	  ps.executeQuery(query);
			
			   while (rs.next()) {
				   
				   nextMarkCode	=	 rs.getInt("NEXTVAL");
				   	  
			      }    
			   
			   
			   
			   
					
		
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Error Occured while selecting nextvalue from identity. ", e);
			e.printStackTrace();
	}
		 
			finally{
				 if (rs != null){
					 
					 try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 
				 
				 if(ps!=null){
				 
					 try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 
		
		
		}
		
		return nextMarkCode; 
	}

	public boolean insertMarkCodeInIP_NAME(int mark_code) throws SQLException {
		

		logger.debug("going to insert a new row in IP_NAME table::");
		logger.debug("==========================================================");
	
		PreparedStatement ps	= null;
		boolean	isMarkCodeInsertedInIP_NAME	=	false;
		int row	=	0;
		String query1	=	"INSERT INTO IP_NAME (ROW_VERSION,MARK_CODE) VALUES (?,?)";
		
		
	

	// try {
		 init();
		 	ps	=	connection.prepareStatement(query1);
		 	
		 	ps.setInt(1, 1);
		 	ps.setInt(2, mark_code);
		 //	ps.setString(3, null);
		 	//ps.setString(4, null);
				
		 	row	=	ps.executeUpdate();
		 	
		 	if(row>0){
		 		isMarkCodeInsertedInIP_NAME		=	true;
		 		logger.debug("Row inserted in IP_NAME successfully");
		 	}
			
		 	

			 
			 if(ps!=null){
			 
				 try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 
		
		
		
		
/*	 	} catch (SQLException e) {
		// TODO Auto-generated catch block
		logger.error("Error Occured while inserting row in IP_NAME table. ", e);
		e.printStackTrace();
	 	}
	 
		finally{
			
			 
			 
			 if(ps!=null){
			 
				 try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 
	
	
	}
	**/
	return isMarkCodeInsertedInIP_NAME;
	 

		
	}

	public boolean updateIP_MARKwithMarkCode(int mark_code, MarkEditImpl mark) throws SQLException {
		

		logger.debug("going to update mark_code in IP_MARK table::");
		logger.debug("==========================================================");
		
		boolean isMarkCodeUpdatedInIP_Mark	=	false;	
		PreparedStatement ps	= null;
		int row =0;
		String query	=	"update ip_mark set mark_code = ? where file_seq = ? and file_ser = ? and file_typ = ? and file_nbr = ? ";
		
		
	

//	 try {
		 init();
		 	ps	=	connection.prepareStatement(query);
		 	
		 	ps.setInt(1, mark_code);
		 	ps.setString(2, mark.getFile_seq());
		 	ps.setInt(3, mark.getFile_ser());
		 	ps.setString(4, mark.getFile_typ());
		 	ps.setInt(5, mark.getFile_nbr());
		 	row = ps.executeUpdate();
			if(row>0){
				isMarkCodeUpdatedInIP_Mark	=	true;
		 		logger.debug("mark_code in IP_MARK is updates successfully for mark::"+mark.toString());
		 	} 	
			else{
				
				throw new SQLException();
			}
		logger.debug("===============================================================");
		
		 if(ps!=null){
			 
			 try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		
	/* 	} catch (SQLException e) {
		// TODO Auto-generated catch block
		logger.error("Error Occured while inserting row in IP_NAME table. ", e);
		e.printStackTrace();
	 	}
	 
		finally{
	
			 
			 if(ps!=null){
			 
				 try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 
	
	
	}
		**/
	return isMarkCodeUpdatedInIP_Mark;
	 

	
	
	}		
		

		

}
