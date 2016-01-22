package com.tcs.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcs.demo.entities.VehicleLocation;

/**
 * Servlet implementation class VehicleLocationServlet
 */
@WebServlet("/VehicleLocation")
public class VehicleLocationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Configuration hbmConfig;
	private SessionFactory sessionFactory;
	   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VehicleLocationServlet() {
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
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null; 
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//CharBuffer requestData = null;
		Gson gson = new Gson();
		//java.lang.reflect.Type typeOfCollectionOfVehicleLocation = new TypeToken<List<VehicleLocation>>(){}.getType();
		VehicleLocation reqVehicle = null;
		Session session = null;
		try{
			String requestData = request.getParameter("VehicleLocation");
			reqVehicle = gson.fromJson(requestData, VehicleLocation.class);
			session = sessionFactory.openSession();
			String hql = "FROM VehicleLocation VLH where VLH.vehicleId =:vId and VLH.locationChangedOn >=:chgTmpstmp ";
			Query query = session.createQuery(hql);
			query.setParameter("vId", reqVehicle.getVehicleId());
			query.setTimestamp("chgTmpstmp", reqVehicle.getLocationChangedOn());
			
			@SuppressWarnings("unchecked")
			List<VehicleLocation> results = query.list();
			if (results != null && !results.isEmpty()){
				String json = gson.toJson(results);
				response.getWriter().append(json);
				response.setStatus(HttpServletResponse.SC_OK);
			}
			else{
				//response.getWriter().append("NOT_FOUND");
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			}
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
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
		Gson gsonParser = new Gson ();
		VehicleLocation dataToSave = null;
		try{
			dataToSave = gsonParser.fromJson(reqData, VehicleLocation.class);
		}
		catch (Exception e){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		Session session = null;
		Transaction tx = null;
		try{
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			//session.persist(dataToSave);
			session.save(dataToSave);
			tx.commit();
			response.setStatus(HttpServletResponse.SC_CREATED);
		}
		catch (ConstraintViolationException nuoEx){
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			ErrorResponse err = new ErrorResponse();
			err.setCode("DUPLICATE_DATA");
			err.setMessage("Cannot create VehicleLocation record, check values of [vehicleId] and [locationChangedOn] fields.");
			String json = gsonParser.toJson(err);
			response.getWriter().append(json);
			
		}
		catch (Exception e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace(response.getWriter());
			e.printStackTrace();
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
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}
	
	private void printStackTrace(Exception e, HttpServletResponse resp){
		/*resp.getWriter().append("<div id=\"err\"><tr>");
		
		for (StackTraceElement traceElem:trace){
			resp.getWriter().append(csq)
		}*/
	}

}
