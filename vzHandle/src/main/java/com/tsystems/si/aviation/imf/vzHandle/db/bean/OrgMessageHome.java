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
 * Home object for domain model class OrgMessage.
 * @see com.tsystems.si.aviation.imf.atcHandle.db.bean.OrgMessage
 * @author Hibernate Tools
 */
public class OrgMessageHome {

	private static final Log log = LogFactory.getLog(OrgMessageHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(OrgMessage transientInstance) {
		log.debug("persisting OrgMessage instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(OrgMessage instance) {
		log.debug("attaching dirty OrgMessage instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OrgMessage instance) {
		log.debug("attaching clean OrgMessage instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(OrgMessage persistentInstance) {
		log.debug("deleting OrgMessage instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OrgMessage merge(OrgMessage detachedInstance) {
		log.debug("merging OrgMessage instance");
		try {
			OrgMessage result = (OrgMessage) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public OrgMessage findById(java.lang.Integer id) {
		log.debug("getting OrgMessage instance with id: " + id);
		try {
			OrgMessage instance = (OrgMessage) sessionFactory.getCurrentSession()
					.get("com.tsystems.si.aviation.imf.atcHandle.db.bean.OrgMessage", id);
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

	public List findByExample(OrgMessage instance) {
		log.debug("finding OrgMessage instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.tsystems.si.aviation.imf.atcHandle.db.bean.OrgMessage")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
