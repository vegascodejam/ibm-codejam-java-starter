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

/**
 * MyVehicleAI - Your code to implement your vehicle goes here!
 */

import com.ibm.coderally.api.AbstractCarListener;

import com.ibm.coderally.geo.Point;

import com.ibm.coderally.geo.CheckPoint;
import com.ibm.json.java.JSONObject;


public class MyVehicleAI  {
	private Vehicle vehicle;
	private Track track;
	public String extra;


	public MyVehicleAI(Vehicle vehicle, Track track) {
		this.vehicle = vehicle;
		this.track = track;
	}
	
	/**
	 * onRaceStart - Called when a race first begins
	 */
	public void onRaceStart() {
		// Write your logic here

		vehicle.setAccelerationPercent(100);
		vehicle.setTarget(new Point(200, 200));
	}

	/**
	 * onCheckpointUpdated - Called when the car updates it's current checkpoint target
	 */
	public void onCheckpointUpdated() {
		
		// Write your logic here
		
		this.vehicle.setTarget(this.vehicle.getCheckpoint().getCenter());

		this.vehicle.setAccelerationPercent(80);
		this.vehicle.setBrakePercent(0);
		
	}

	/**
	 * onOffTrack - Called when the car goes off track
	 */
	public void onOffTrack() {
		
		// Write your logic here
	}
	
	/**
	 * onStalled - Called when the cars velocity drops to zero
	 */
	public void onStalled() {
		
		// Write your logic here
	}

	/**
	 * onOpponentInProximity - Called when an opponent is in proximity of the car
	 */
	public void onOpponentInProximity() {
		Vehicle otherVehicle = Vehicle.fromJson(extra);
		
		// Write your logic here
		
		// Avoidance

		this.vehicle.setAccelerationPercent(20);
		this.vehicle.setTarget(AIUtils.getAlternativeLane(this.vehicle.getCheckpoint(), this.vehicle.getPosition()));
	}
	
	/**
	 * onCarCollision - Called when the car collides with another car
	 */
	public void onCarCollision() {
		Vehicle otherVehicle = Vehicle.fromJson(extra);
		
		// Write your logic here
	}
	
	/**
	 * onObstacleInProximity - Called when an obstacle is in proximity of the car
	 */
	public void onObstacleInProximity() {
		Obstacle obstacle = Obstacle.fromJson(extra);
		
		// Write your logic here
	}

	/**
	 * onObstacleCollision - Called when the car collides with an obstacle
	 */
	public void onObstacleCollision() {
		Obstacle obstacle = Obstacle.fromJson(extra);
	
		// Write your logic here
	}
	
	
	/**
	 * onTimeStep - Called once for each timestep of the race
	 */
	public void onTimeStep() {
		
	}

	public JSONObject getUpdate(int requestCount) {

		if (!this.vehicle.hasChanged()) {
			return getNoop();
		}

		JSONObject response = new JSONObject();
		response.put("status", "success");
		response.put("vehicle", this.vehicle.getState());
		response.put("count", requestCount);
		
		return this.vehicle.getState();
	}
	
	private JSONObject getNoop() {
		
		JSONObject response = new JSONObject();
		response.put("status", "noop");
		
		return response;		
	}

}
