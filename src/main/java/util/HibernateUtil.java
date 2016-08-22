package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// Create Session Factory From cfg.xml.
			return new Configuration().configure().buildSessionFactory();
		} catch (Throwable e) {
			System.err.println("SessionFactory create failed." + e);
			throw new ExceptionInInitializerError(e);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// Close Connection.
	public static void shutdown() {
		getSessionFactory().close();
	}
}
