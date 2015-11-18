package com.mgs.rbsnov.client.app;

import com.mgs.rbsnov.client.util.Settings;
import com.mgs.rbsnov.spring.Config;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


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
			Settings.init();
			AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Config.class);
			Player player = (Player) annotationConfigApplicationContext.getBean("player");
			player.Play();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error(e.getStackTrace()[1].getClass().getSimpleName() + ": " + e.getMessage());
		}
    }
}
