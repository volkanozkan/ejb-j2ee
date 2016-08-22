package bean;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.Users;
import util.HibernateUtil;

@Stateless
@Remote(UsersBeanRemote.class)
public class StatelessBean implements UsersBeanRemote {

	// Get User By Username.
	@Override
	public Users getUserByName(String username) {
		Transaction transaction = null;
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		try {
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Users where username=:username_param");
			query.setParameter("username_param", username);
			if (!query.list().isEmpty()) {
				return (Users) query.list().get(0); // Return User.
			} else {
				return null;
			}
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return null;
	}

	// Update User.
	@Override
	public void updateUser(Users user) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Users usr = (Users) session.get(Users.class, user.getUsername());
			usr.setBirthday(user.getBirthday());
			usr.setSex(user.getSex());
			session.update(usr);
			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
	}

}
