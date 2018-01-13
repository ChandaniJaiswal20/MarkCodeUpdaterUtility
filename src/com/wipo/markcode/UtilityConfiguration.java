package com.wipo.markcode;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;






public class UtilityConfiguration {

	ConfigParam	config	=	null;
	

	
	public String databaseHost;
	public String databasePort;
	public String databaseTool;
	public String databaseName;
	public String userName;
	public String password;
	
	public String 	signWcode=null;
	
	public static Logger logger	= Logger.getLogger(UtilityConfiguration.class);

	public UtilityConfiguration loadConfiguration() {
		logger.debug("loading configuration.....");
			config	= new ConfigParam("markCodeUpdate.properties");
		
		
			databaseHost	= config.getProperty("databaseHost");
			databasePort	= config.getProperty("databasePort");
			databaseTool	= config.getProperty("databaseTool");
			databaseName	=	config.getProperty("databaseName");
			userName		=	config.getProperty("userName");
			password		=	config.getProperty("password");
			signWcode		=	config.getProperty("signWcode");
		
		
			logger.debug("databaseHost:::"+databaseHost);
			logger.debug("databasePort:::"+databasePort);
			logger.debug("databaseTool:::"+databaseTool);
			logger.debug("databaseName:::"+databaseName);
			logger.debug("userName:::"+userName);
		
		
			return this;
				
	}
	
	
	public void checkValuesOfConfigFile(){
		

		if(databaseHost==null||databaseHost.isEmpty()){
			
			
			logger.error("Please specify value of  databaseHost in markCodeUpdate.properties");
			throw new MissingConfigurationException("Please specify value of  databaseHost in markCodeUpdate.properties");
		}
		
		if(databasePort==null||databasePort.isEmpty()){
			
			
			logger.error("Please specify value of  databasePort in markCodeUpdate.properties");
			throw new MissingConfigurationException("Please specify value of  databasePort in markCodeUpdate.properties");
		}
		
		if(databaseTool==null||databaseTool.isEmpty()){
			
			logger.error("Please specify value of  databaseTool in markCodeUpdate.properties");
			throw new MissingConfigurationException("Please specify value of  databaseTool in markCodeUpdate.properties");
			
		}
		else if(!(databaseTool.equalsIgnoreCase("sql")||databaseTool.equalsIgnoreCase("oracle"))){
			
			logger.error("Please specify correct database tool in markCodeUpdate.properties ie. for oracle database::<oracle> and for sql database::<sql>");
			throw new MissingConfigurationException("Please specify correct database tool in markCodeUpdate.properties ie. for oracle database::<oracle> and for sql database::<sql>");
		}
		
		if(databaseName==null||databaseName.isEmpty()){
			
			
			logger.error("Please specify value of  databaseName in markCodeUpdate.properties");
			throw new MissingConfigurationException("Please specify value of  databaseName in markCodeUpdate.properties");
		}
		
		if(userName==null||userName.isEmpty()){
			
			logger.error("Please specify value of  userName in markCodeUpdate.properties");
			throw new MissingConfigurationException("Please specify value of  userName in markCodeUpdate.properties");
		}
		
		if(password==null||password.isEmpty()){
			
			
			logger.error("Please specify value of  password in markCodeUpdate.properties");
			throw new MissingConfigurationException("Please specify value of  password in markCodeUpdate.properties");
		}
		
		if(signWcode==null||signWcode.isEmpty()){
			
			
			logger.error("Please specify value of  signWcode in markCodeUpdate.properties");
			throw new MissingConfigurationException("Please specify value of  signWcode in markCodeUpdate.properties");
		}
		
		
	}

	

	
	public static void main(String[] args) {
		
		UtilityConfiguration uc = new UtilityConfiguration();
	
			
	//String		log4jConfigFile	=	System.getProperty("user.dir")+File.separator+"log4j.properties";
		
		String		log4jConfigFile	=	"D://PatentAnnuityUtility//log4j.properties";
			
			PropertyConfigurator.configure(log4jConfigFile);
			logger.info("log4jConfigFile:::"+log4jConfigFile);	
	
		uc.loadConfiguration();
		uc.checkValuesOfConfigFile();
		
		
		
	}
	
}
