/**
 * 
 */
package org.pos2d.webapi.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.pos2d.webapi.beans.TransformerStatusBean;
import org.pos2d.webapi.entities.Transformer;
import org.pos2d.webapi.entities.TransformerStatus;
import org.pos2d.webapi.entities.Vehicle;
import org.pos2d.webapi.entities.VehicleLocation;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * @author soumik
 *
 */
public class TransformerDAOImpl implements TransformerDAO {

	private SessionFactory sessionFactory;
	 
    public TransformerDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	/* (non-Javadoc)
	 * @see org.pos2d.webapi.dao.TransformerDAO#getAllSuburb(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Transformer> getBySuburbAndStatus(String city, String suburb, String status) {
		String hql = "FROM Transformer as xfr where xfr.city = :cityName and xfr.suburb = :suburbName and xfr.transformerStatus.status = :statusCode";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("cityName", city);
		query.setParameter("suburbName", suburb);
		query.setParameter("statusCode", status);
		return (List<Transformer> ) query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Transformer> getBySuburb(String city, String suburb) {
		String hql = "FROM Transformer as xfr where xfr.city = :cityName and xfr.suburb = :suburbName";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("cityName", city);
		query.setParameter("suburbName", suburb);
		return (List<Transformer> ) query.list();
	}
	
	@Override
	public void createXfrStatus(TransformerStatusBean bean, String data) {
		Transformer xfr = null;
		String hql = "FROM Transformer as xfr where xfr.name = :name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("name", bean.getName());
		xfr = (Transformer) query.uniqueResult();
		
		TransformerStatus xfrStatus = null;
		
		if (xfr == null){
			System.out.println("Transformer not found. Ignoring update");
			return;
		}
		else{
			if(xfr.getTransformerStatus() == null){
				xfrStatus = new TransformerStatus();
			}
			else{
				xfrStatus = xfr.getTransformerStatus();
			}
		}
		xfrStatus.setTransformer(xfr);
		//xfrStatus = (TransformerStatus) sessionFactory.getCurrentSession().merge(xfrStatus);
		xfrStatus.setStatusData(data);
		xfrStatus.setStatus(bean.getStatus());
		sessionFactory.getCurrentSession().saveOrUpdate(xfrStatus);
		//sessionFactory.getCurrentSession().save(xfr);
	}

}
