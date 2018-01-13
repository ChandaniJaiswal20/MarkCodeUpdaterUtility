package com.wipo.markcode;


import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



public class UtilityManager {

	
	 static{
	        
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
	        System.setProperty("current.date.time", dateFormat.format(new Date()));
	    }
	

	 private static UtilityManager uniqueInstance =	null;
	
	
	  private UtilityConfiguration configuration 	=	null;
	
	  private 	final	DBdataProcessor	dataProcessor	; 
	 
	 

	 
	  private	List<MarkEditImpl> list_of_mark_having_Problem=	new ArrayList<MarkEditImpl>();
	  private	List<MarkEditImpl> listOfMarknotUpdateWithMarkCode=	new ArrayList<MarkEditImpl>();
	//  private	CommonsProxyFactory commonsProxyFactory;


	  private int nextMarkCode;
	 
	  private 	static final	String  LOG4JCONFIGFILE	= "log4j.properties";
	
	  private 	static Logger logger	=	Logger.getLogger(UtilityManager.class);
	  
	  private static String sequence	=	"SEC_DENOMINACION" ;
	  

	private UtilityManager()
	{

		PropertyConfigurator.configure(LOG4JCONFIGFILE);
		logger.info("Initializing Utility...........................................!!!!!");
		logger.debug("################################################");
		logger.debug("=================================================");
		
		logger.debug("LOG4JCONFIGFILE:::"+LOG4JCONFIGFILE);
		configuration 	=	new UtilityConfiguration();	//aggregation (has a)
		configuration.loadConfiguration();
		configuration.checkValuesOfConfigFile();
		
	//	commonsProxyFactory = new CommonsProxyFactory(configuration.iiopHost +":"+configuration.iiopPort);
		
	//	MarkEditImpl.commonsProxyFactory=commonsProxyFactory;
		MarkEditImpl.signWcode		=	configuration.signWcode.trim().toUpperCase();
	
		logger.debug("Obtaining Database Connection....");
		dataProcessor	= new DBdataProcessor(configuration.databaseHost,configuration.databasePort,
					  configuration.databaseTool,configuration.databaseName,configuration.userName,configuration.password);
		
		logger.info("Initializing Completed...............................................!!!!!!");		
		logger.debug("=================================================");
		logger.debug("################################################");
	}

		public static  UtilityManager getInstance(){
			
					if(uniqueInstance==null)
					{	
						uniqueInstance	=	 new UtilityManager();
					}
					
					
			return uniqueInstance;
					
		}
	
		public UtilityConfiguration getConfiguration(){
			return configuration;
						
		}
	


	
	public void configureLogger(){
	//	log4jConfigFile	=	configuration.patentAnnuityUtilityFolder;
	//	log4jConfigFile	="log4j.properties";
	//	log4jConfigFile	=	System.getProperty("user.dir")+File.separator+"log4j.properties";
		
		PropertyConfigurator.configure(LOG4JCONFIGFILE);
			
	}
		
	



	public	void  processMarksHavingProblem() {
	
		
	
		
		 getListOfmarkFromIP_MARK();
		
	
		Iterator<MarkEditImpl> iterator	=	list_of_mark_having_Problem.iterator();
		
	

		while(iterator.hasNext()){
			MarkEditImpl mark	=	iterator.next();
			
			logger.debug("going to process mark:::"+mark.toString());
			
			//find next markCode
			try{
				Connection	conn	=		dataProcessor.getConnection();
		
				conn.setAutoCommit(false);
				boolean	isNextMarkCodeObtained	=	getNextSequenceValueOfMarkCode();
				
				if(!isNextMarkCodeObtained)
				{
					
					logger.info("Mark code cannot be generated , Please check database credential in  property file....");
					System.exit(0);
							
				}
				logger.debug("Obtained mark_code ::"+nextMarkCode);
				insertMarkCodeInIP_NAME(nextMarkCode);
				updateIP_MARKwithMarkCode(nextMarkCode,mark);
				conn.commit();
			}
			catch(SQLException sqe){
				listOfMarknotUpdateWithMarkCode.add(mark);
				logger.error("Exception occurred while inserting or updating Mark code",sqe);
				logger.debug("Executing for next mark");
				continue;
				
			
				
			}
		//	processCMarkDataOfCurrentIntregn(mark);
			
		//	mark.updateSignType();
			
			logger.debug("Mark :::"+mark.toString() +" is processed successfully....");
			
		}
		logger.debug("Number of marks that are not updated with markCode are::"+listOfMarknotUpdateWithMarkCode.size());
		logger.debug("Marks that are not updated with markCode are::"+listOfMarknotUpdateWithMarkCode);
		logger.debug("All Marks are processed successfully.......");
		
	
		
	}

				
				
				
	
		
		
		private void updateIP_MARKwithMarkCode(int mark_code, MarkEditImpl mark) throws SQLException {
		
			dataProcessor.updateIP_MARKwithMarkCode(mark_code,mark);
	}

		private void insertMarkCodeInIP_NAME(int mark_code) throws SQLException {
		
			
			dataProcessor.insertMarkCodeInIP_NAME(mark_code);
		
	}

		private boolean getNextSequenceValueOfMarkCode() {
		
			
		
			boolean	isInsertedInSYS_SEC_DENOMINACION	=	false;
			
			if(configuration.databaseTool.equals("sql")){
				
				if(!sequence.contains("SYS_")){
					
					sequence = "SYS_" + sequence;
				}
			//	sequence = "SYS_" + sequence; 
							 
		//		sequence =  sequence;
				isInsertedInSYS_SEC_DENOMINACION	=	dataProcessor.insertIntoSqlDatabase(sequence);
			
					if(isInsertedInSYS_SEC_DENOMINACION)
							{
								nextMarkCode	=	dataProcessor.selectNextValuefromSQL();
								
							}		
						
			}
			
			else if (configuration.databaseTool.equals("oracle")){
				
				
				nextMarkCode	=	dataProcessor.selectNextValuefromOracle();
				
				if(nextMarkCode>0){
					isInsertedInSYS_SEC_DENOMINACION	=	true;
				}
				
				
				
			} 
			
			return isInsertedInSYS_SEC_DENOMINACION;
		}
			
			
			
		


		private void getListOfmarkFromIP_MARK() {
		
			list_of_mark_having_Problem	=	dataProcessor.readMarksFromDB(configuration.signWcode);
			
			logger.debug("=================================================");
			
			if(list_of_mark_having_Problem.size()>0){
				logger.debug("list_of_mark_having_Problem ::"+list_of_mark_having_Problem);
			}
			else{
				
				logger.info("There are no marks in database with problem");
				logger.info("Exiting program");
				System.exit(0);
			}
			
			
			logger.debug("################################################");
			
			
		
	}


		
	
	public void stopUtility(){
		if(dataProcessor.getConnection()!=null){
			logger.debug("Closing database connection..........!!");
			dataProcessor.closeConnection();
			logger.debug("Database connection closed..........!!");
			
		}
	
		logger.info("Processing Completed.......................!!!");
		logger.info("=================================================");
		logger.info("Please check processed details in log file generated in log folder");
		logger.info("********************************************************");
	}


	
}
