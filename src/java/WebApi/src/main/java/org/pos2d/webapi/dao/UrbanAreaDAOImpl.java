/**
 * 
 */
package org.pos2d.webapi.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.pos2d.webapi.entities.City;
import org.pos2d.webapi.entities.UrbanArea;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author soumik
 *
 */
public class UrbanAreaDAOImpl implements UrbanAreaDAO {
	
	private SessionFactory sessionFactory;
	 
    public UrbanAreaDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	@Override
	@Transactional
	public UrbanArea getByName(String name, String city) {
		String hql = "FROM UrbanArea as ua where ua.name = :subUrbName and ua.city = :cityName";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("subUrbName", name);
		query.setParameter("cityName", city);
		return (UrbanArea) query.list().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<UrbanArea> getAllForCity(String city) {
		String hql = "select ua.name FROM UrbanArea as ua where ua.city = :cityName";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("cityName", city);
		return (List<UrbanArea>) query.list();
	}

}
