/**
 * 
 */
package org.pos2d.webapi.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.pos2d.webapi.entities.City;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author soumik
 *
 */
public class CityDAOImpl implements CityDAO {

	private SessionFactory sessionFactory;
	 
    public CityDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	
	/* (non-Javadoc)
	 * @see org.pos2d.webapi.dao.CityDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<City> getAll() {
		String hql = "FROM City";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<City>) query.list();
	}

	@Override
	@Transactional
	public City getByName(String name) {
		String hql = "FROM City as city where city.name = :cityName";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("cityName", name);
		return (City) query.list().get(0);
	}

}
