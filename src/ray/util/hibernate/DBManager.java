package ray.util.hibernate;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;



/**
 * Configures and provides access to Hibernate sessions, tied to the current
 * thread of execution. Follows the Thread Local Session pattern, see
 * {@link http://hibernate.org/42.html}.
 */

public class DBManager {

	public static Session currentSession() {
		Session session = null;
		try {
			session = ErpSessionFactory.getSession();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return session;
	}

	public static void closeSession() throws HibernateException {
		ErpSessionFactory.closeSession();
	}

	public static void closeOpenSession() {
		closeSession();
		currentSession();
	}

	public static void save(Object obj) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = currentSession();
			tx = session.beginTransaction();
			session.save(obj);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					e1.printStackTrace();
				}
			if (session != null)
				try {
					closeSession();
				} catch (HibernateException e2) {
					e2.printStackTrace();
				}
			e.printStackTrace();
			throw e;
		}

	}

	public static void saveSet(Set<?> listOfObj) throws Exception {

		Session session = null;
		Transaction tx = null;
		try {
			session = currentSession();
			tx = session.beginTransaction();
			for (Object object : listOfObj) {
				session.save(object);
			}
			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					e1.printStackTrace();
				}
			if (session != null)
				try {
					closeSession();
				} catch (HibernateException e2) {
					e2.printStackTrace();
				}
			e.printStackTrace();
			throw e;
		}

	}

	public static void update(Object obj) throws Exception {

		Session session = null;
		Transaction tx = null;
		try {
			session = currentSession();
			tx = session.beginTransaction();
			session.update(obj);

			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					e1.printStackTrace();
				}
			if (session != null)
				try {
					closeSession();
				} catch (HibernateException e2) {
					e2.printStackTrace();
				}
			e.printStackTrace();
			throw e;
		}
	}

	public static void delete(Object obj) throws Exception {

		Session session = null;
		Transaction tx = null;
		try {
			session = currentSession();
			tx = session.beginTransaction();
			session.delete(obj);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					e1.printStackTrace();
				}
			if (session != null)
				try {
					closeSession();
				} catch (HibernateException e2) {
					e2.printStackTrace();
				}

			throw e;
		}

	}

	public static List<?> find(String hql) {
		return find(hql, Integer.MAX_VALUE, 0);
	}

	public static List<?> find(String hql, int sizeOfResult, int first) {
		
		
		Session session = null;
		Transaction tx = null;
		List<?> returnValue = null;
		try {
			session = currentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setMaxResults(sizeOfResult);
			query.setFirstResult(first * sizeOfResult);
			returnValue = query.list();
			tx.commit();
		} catch (Exception e) {
			if (tx != null){
				tx.rollback();
			}
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return returnValue;
	}

}
