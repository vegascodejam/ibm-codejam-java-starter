/*******************************************************************************
 * COPYRIGHT LICENSE: This information contains sample code provided in source 
 * code form. You may copy, modify, and distribute these sample programs in any 
 * form without payment to IBM® for the purposes of developing, using, marketing 
 * or distributing application programs conforming to the application programming 
 * interface for the operating platform for which the sample code is written. 
 * Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE 
 * ON AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, 
 * INCLUDING, BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF 
 * MERCHANTABILITY, SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE, 
 * AND ANY WARRANTY OR CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING 
 * OUT OF THE USE OR OPERATION OF THE SAMPLE SOURCE CODE. IBM HAS NO OBLIGATION 
 * TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS OR MODIFICATIONS 
 * TO THE SAMPLE SOURCE CODE.
 ******************************************************************************/
package com.ibm.coderally.client;

import java.io.IOException;
import java.util.List;
import java.lang.reflect.Method;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.coderally.entity.cars.CarAttributes;
import com.ibm.coderally.entity.cars.attributes.JsonCarAttributes;
import com.ibm.coderally.geo.CheckPoint;
import com.ibm.coderally.geo.Point;
import com.ibm.coderally.geo.Rotation;

import com.ibm.coderally.client.MyVehicleAI;

/**
 * Servlet implementation of CodeRallyServer
 */
@WebServlet("/rally")
public class CodeRallyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CodeRallyServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Process /rally reuqest and return the relevant processing results
	 *  
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//set request and response configuration
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(200);
		HttpSession session = null ;
		String action = request.getParameter("action");
		String data = request.getParameter("data");

		Vehicle vehicle = null;
		Track track = null;
		MyVehicleAI myVehicle = null;


		try {
			session = request.getSession();

			int count = 0;
			Object value = session.getAttribute("requestCount") ;
			if (value != null) {	
				count = (Integer)value;
				count++;
			}
			session.setAttribute("requestCount", count);

			Object attributesJsonObj = session.getAttribute("attributesJson") ;
			Object trackJsonObj = session.getAttribute("trackJson") ;
			String attributesJson = null;
			String trackJson = null;

			if (data != null & !data.equals("")) {
				JSONObject dataObj = JSONObject.parse(data);

				String vehicleJson = ((JSONObject)dataObj.get("vehicle")).toString();

				if (attributesJsonObj == null) {
					
					// No existing session, create new vehicle

					attributesJson = ((JSONObject)dataObj.get("vehicle")).get("attributes").toString();
					System.err.println( attributesJson );
					trackJson = ((JSONObject)dataObj.get("track")).toString();
					
					// Store these into the session
					 
					session.setAttribute("attributesJson", attributesJson);
					session.setAttribute("trackJson", trackJson);

				} else {
					attributesJson = (String) attributesJsonObj;
					trackJson = (String) trackJsonObj;
				}

				vehicle = Vehicle.fromJson(vehicleJson, attributesJson);
				track = Track.fromJson(trackJson);

				myVehicle = new MyVehicleAI(vehicle, track);

				// Handle extra data item (obstacles/other vehicles)
				if (dataObj.containsKey("extra")) {
					myVehicle.extra = dataObj.get("extra").toString();
				}

			}

			if (action != null) {
				if (action.equals("clear")) {
					response.getWriter().write("{\"status\":\"clear\"}");

					session.invalidate();
				} else if (!action.equals("")) {

					Class noparams[] = {};
					Class cls = MyVehicleAI.class;

					try {
						Method method = cls.getDeclaredMethod(action, noparams);
						method.invoke(myVehicle);
						
						response.getWriter().write(myVehicle.getUpdate(count).toString());

					} catch (NoSuchMethodException nomethod) {
						response.getWriter().write("{\"status\":\"noop\", \"reason\":\"Invalid event\"}");
					}

				}
			}
		} catch (Exception ex) {
			response.getWriter().write("Exception: " + ex.getMessage());
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			response.getWriter().write("Exception: " + errors.toString());
		}

	}
}
