package com.tcs.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.google.gson.Gson;
import com.tcs.demo.entities.VehicleLocation;

/**
 * Servlet implementation class VehicleTrackSubscribeServlet
 */
@WebServlet("/VehicleTrackerSubscribe")
public class VehicleTrackSubscribeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Configuration hbmConfig;
	private SessionFactory sessionFactory;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VehicleTrackSubscribeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		hbmConfig = new Configuration();
		hbmConfig.configure();
	    sessionFactory = hbmConfig.buildSessionFactory();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		sessionFactory.close();
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader reqRdr = request.getReader();
		String reqLine = reqRdr.readLine();
		StringBuffer sbfReqData = new StringBuffer();
		while(reqLine != null){
			sbfReqData.append(reqLine);
			reqLine = reqRdr.readLine();
		}
		String reqData = sbfReqData.toString();
		if (sbfReqData.length() == 0){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		Gson gson = new Gson();
		//java.lang.reflect.Type typeOfCollectionOfVehicleLocation = new TypeToken<List<VehicleLocation>>(){}.getType();
		VehicleLocation reqVehicle = null;
		Session session = null;
		try{
			//String requestData = request.getParameter("VehicleLocation");
			reqVehicle = gson.fromJson(reqData, VehicleLocation.class);
			session = sessionFactory.openSession();
			String hql = "FROM VehicleLocation VLH WHERE VLH.vehicleId =:vId ORDER BY by VLH.locationChangedOn DESC";
			Query query = session.createQuery(hql);
			query.setParameter("vId", reqVehicle.getVehicleId());
			//query.setTimestamp("chgTmpstmp", reqVehicle.getLocationChangedOn());
			
			@SuppressWarnings("unchecked")
			List<VehicleLocation> results = query.list();
			VehicleLocation mostRecent = results.get(0);
			// this code assumes that [mostRecent] will never be null
			// this is quite valid because the API entry point will be
			// well before this method which in turn should ensure and enforce
			// that this call has happened
			// only when there IS a location record for this vehicle.
			String json = gson.toJson(mostRecent);
			response.getWriter().append(json);
			response.setStatus(HttpServletResponse.SC_CREATED);
		}
		catch (Exception ex){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			ex.printStackTrace(response.getWriter());
			ex.printStackTrace();
		}
		finally{
			if (session != null && session.isOpen()){
				session.close();
			}
		}
		response.getWriter().flush();
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
