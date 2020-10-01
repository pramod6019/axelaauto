package axela.portal;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cloudify.connect.Connect;

public final class ContextListener implements ServletContextListener {

	Connect ct = new Connect();
	String emp_id = "";

	public ContextListener() {
	}

	public void contextInitialized(ServletContextEvent servletContextEvent) {

		// Get the context
		ServletContext servletContext = servletContextEvent.getServletContext();

		// Set a context attribute
		try {
			// Thread.sleep(3000);
			// QuartzCron quartz = new QuartzCron();
			// quartz.startjob();
		} catch (Exception ex) {
			ct.SOPError("Axelaauto===" + this.getClass().getName());
			ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {

		// ServletContext ctx = servletContextEvent.getServletContext();
		// try {
		// Enumeration<Driver> drivers = DriverManager.getDrivers();
		// while (drivers.hasMoreElements()) {
		// DriverManager.deregisterDriver(drivers.nextElement());
		// }
		// } catch (Exception ex) {
		// ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		// }
		// SOP();

	}
}
