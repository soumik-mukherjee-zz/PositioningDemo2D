/**
 * 
 */
package org.pos2d.webapi.dao;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.pos2d.webapi.beans.VehicleLocationBean;
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
public class VehicleDAOImpl implements VehicleDAO {

	private SessionFactory sessionFactory;
	
	public VehicleDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	/* (non-Javadoc)
	 * @see org.pos2d.webapi.dao.VehicleDAO#createActiveVehicle(java.lang.String)
	 */
	@Override
	@Transactional
	public void createActiveVehicle(String vehicleName) {
		Vehicle veh = null;
		String hql = "FROM Vehicle as veh where veh.name = :name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("name", vehicleName);
		veh = (Vehicle) query.uniqueResult();
		//veh = (Vehicle) query.list().get(0);
		if (veh == null){
			veh = new Vehicle();
			veh.setName(vehicleName);
		}
		veh.setActive("Y");
		//sessionFactory.getCurrentSession().beginTransaction();
		sessionFactory.getCurrentSession().save(veh);
		//sessionFactory.getCurrentSession().getTransaction().commit();
	}
	
	// No transaction indicator as client of this method is required to handle transaction
	@Override
	public void createVehicleLocation(VehicleLocationBean location) {
		Vehicle veh = null;
		String hql = "FROM Vehicle as veh where veh.name = :name and veh.active='Y'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("name", location.getVehicleName());
		veh = (Vehicle) query.uniqueResult();
		if (veh == null){
			System.out.println("Vehicle not registered to submit location updates. Ignoring update");
			return;
		}
		VehicleLocation vLoc = new VehicleLocation();
		Coordinate pntCoord = new Coordinate();
		pntCoord.x = location.getLongitude();
		pntCoord.y = location.getLatitude();
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
		Point pnt = factory.createPoint(pntCoord);
		vLoc.setLocation(pnt);
		vLoc.setUpdatedOn(Calendar.getInstance());
		vLoc.setVehicle(veh);
		sessionFactory.getCurrentSession().save(vLoc);
	}
}
