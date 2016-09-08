package com.tsystems.si.aviation.imf.vzHandle.db.bean;
// Generated Jun 23, 2016 4:34:11 PM by Hibernate Tools 4.3.1.Final

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class ComFlight.
 * @see com.tsystems.si.aviation.imf.atcHandle.db.bean.ComFlight
 * @author Hibernate Tools
 */
public class ComFlightHome {

	private static final Log log = LogFactory.getLog(ComFlightHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(ComFlight transientInstance) {
		log.debug("persisting ComFlight instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ComFlight instance) {
		log.debug("attaching dirty ComFlight instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ComFlight instance) {
		log.debug("attaching clean ComFlight instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ComFlight persistentInstance) {
		log.debug("deleting ComFlight instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ComFlight merge(ComFlight detachedInstance) {
		log.debug("merging ComFlight instance");
		try {
			ComFlight result = (ComFlight) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ComFlight findById(java.lang.Integer id) {
		log.debug("getting ComFlight instance with id: " + id);
		try {
			ComFlight instance = (ComFlight) sessionFactory.getCurrentSession()
					.get("com.tsystems.si.aviation.imf.atcHandle.db.bean.ComFlight", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ComFlight instance) {
		log.debug("finding ComFlight instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.tsystems.si.aviation.imf.atcHandle.db.bean.ComFlight")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
