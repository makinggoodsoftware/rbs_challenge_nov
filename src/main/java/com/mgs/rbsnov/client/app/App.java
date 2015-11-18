package com.mgs.rbsnov.client.app;

import com.mgs.rbsnov.client.util.Settings;
import org.apache.log4j.Logger;


/**
 * Hello world!
 *
 */
public class App 
{
	private final static Logger logger = Logger.getLogger(App.class.getSimpleName());
    public static void main( String[] args )
    {    	
    	
    	try {
    		 String teamName = "FlyingBirds", password = "mypassword";

             if (args.length == 2)
             {
                 teamName = args[0].trim();
                 password = args[1].trim();
             }
    		logger.info("Start Game");
			Settings.init();
	        Player player = new Player(teamName, password);
	        player.Play();			
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error(e.getStackTrace()[1].getClass().getSimpleName() + ": " + e.getMessage());
		}
    }
}
