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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jbox2d.common.Vec2;

import com.ibm.coderally.client.mappers.JsonPoint;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.coderally.entity.cars.CarAttributes;
import com.ibm.coderally.entity.cars.RaceCar;
import com.ibm.coderally.entity.cars.attributes.JsonCarAttributes;
import com.ibm.coderally.geo.CheckPoint;
import com.ibm.coderally.geo.Point;
import com.ibm.json.java.JSONObject;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE,
getterVisibility=JsonAutoDetect.Visibility.NONE,
setterVisibility=JsonAutoDetect.Visibility.NONE,
creatorVisibility=JsonAutoDetect.Visibility.NONE,
isGetterVisibility=JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehicle {
	@JsonProperty
	public int brakePercent;
	@JsonProperty
	public int accelerationPercent;
	@JsonProperty
	public Point target;
	
	@JsonProperty
	private Point position;

	@JsonProperty
	private Vec2 acceleration;

	@JsonProperty
	private Vec2 velocity;

	@JsonProperty
	private int rotation;
	
	@JsonProperty
	private int lap;
	
	@JsonProperty
	private CheckPoint checkpoint;
	

	@JsonIgnore
	private boolean hasChanged = false;;
	
	
	@JsonIgnore
	private String state;
	
	@JsonIgnore
	private CarAttributes attributes;
	
	@JsonProperty
	private double velocityNormalized;


	/**
	 * Sets the percent acceleration for this car. Values may be between 100 and
	 * 0. Setting a value greater than 100 or less than 0 will be ignored.
	 * 
	 * @param percent
	 */
	public void setAccelerationPercent(int percent)
	{
		if (percent < 0 || percent > 100) {
			throw new IllegalArgumentException("function only accepts percentages 0-100. Input was: " + percent);
		}

		this.accelerationPercent = percent;
		hasChanged = true;
	}


	/**
	 * Gets the percent acceleration for this car. Values may be between 100 and 0.
	 * 
	 * Lower acceleration will allow the car to handle better.
	 * 
	 * @return acceleration
	 */
	public int getAccelerationPercent() {
		return this.accelerationPercent;
	}
	
	public Vec2 getAcceleration() {
		return acceleration;
	}

	
	/**
	 * Sets the percent braking for this car. Values may be between 100 and 0.
	 * 
	 * @param percent braking
	 */
	public void setBrakePercent(int percent)
	{
		if (percent < 0 || percent > 100) {
			throw new IllegalArgumentException("function only accepts percentages 0-100. Input was: " + percent);
		}

		this.brakePercent = percent;
		hasChanged = true;
	}

	/**
	 * Gets the percent braking for this car. Values may be between 100 and 0.
	 * 
	 * @return brake percent
	 */
	public int getBrakePercent() {
		return this.brakePercent;
	}

	/**
	 * setTarget - Specify a 2D point (x,y) on the game track, that your vehicle will head towards.
	 * 
	 * @param Point p Coordinate to move towards.
	 */
	public void setTarget(Point p)
	{
		this.target = p;
		hasChanged = true;
	}
	
	/**
	 * getTarget - Returns the point you are currently headed towards.
	 * 
	 * @return Point Current destination point of vehicle.
	 */
	public Point getTarget() {
		return target;
	}

	/**
	 * getVelocity - Returns how fast you are currently moving in x and y directions
	 * 
	 * @return Vector Contains x and y velocity indicators
	 */
	public Vec2 getVelocity() {
		return velocity;
	}
	
	
	public double getVelocityNormalized() {
		return velocityNormalized;
	}
	
	/**
	 * getPosition() - Returns the current position of this vehicle.
	 * 
	 * @return Point Current vehicle position
	 */
	public Point getPosition() {
		return position;
	}
	
	/**
	 * getRotation - Returns the current rotation of the vehicle
	 * 
	 * @return int degrees of rotation
	 */
	public int getRotation() {
		return rotation;
	}
	
	/**
	 * getLap - Get the current lap of the race for this vehicle
	 * 
	 * @return int Current lap count
	 */
	public int getLap() {
		return lap;
	}

	/**
	 * getCheckpoint Get the next Checkpoint on the Track in relation to your vehicle
	 * 
	 * @return Checkpoint Next checkpoint.
	 */
	public CheckPoint getCheckpoint() {
		return checkpoint;
	}
	
	/**
	 * Calculates the maximum degrees the car can turn with the
	 *  given acceleration percentage in one second
	 * 
	 * @param acceleration of the car
	 * 
	 * @return maximum degrees of turning per second
	 */
	public double calculateMaximumTurning(int acceleration) {
		if (acceleration > 0) {
			//100% acceleration reduces turning by 50%
			return attributes.getTurningDegrees() * (-acceleration / 2 + 100) / 100;
		} else {
			//100% braking (-100% acceleration) increases turning by 100%
			return attributes.getTurningDegrees() * (-acceleration + 100) / 100;
		}
	}
	
	/**
	 * calculateHeading - Calculates the amount of degrees the entity must turn to 
	 * head towards the given point
	 * 
	 * @param  Point  point Point to turn to
	 * @return int Degrees the vehicle must turn
	 */
	public double calculateHeading(Point point) {
		//+90 to represent the image axis offset
		final double desiredHeading = (position.getHeadingTo(point) + 90) % 360;
		final double currentHeading = rotation;
		double degrees = desiredHeading - currentHeading;
		//Faster to turn the other way
		if (degrees > 180) {
			degrees -= 360;
		} else if (degrees < -180) {
			degrees += 360;
		}
		return degrees;
	}
	

	/* Car attribute methods */
	public double getMaxMph()
	{
		return this.attributes.getAcceleration();
	}


	public double getWeight()
	{
		return this.attributes.getWeight();
	}


	public double getTractionCoefficient()
	{
		return this.attributes.getTractionCoefficient();
	}

	public double getTurningDegrees()
	{
		return this.attributes.getTurningDegrees();
	}
	
	
	public boolean hasChanged() {
		return this.hasChanged;
	}

	public JSONObject getState()
	{
		JSONObject response = new JSONObject();
		response.put("brakePercent", this.brakePercent);
		response.put("accelerationPercent", this.accelerationPercent);
		response.put("target", JsonPoint.getPointJSON(this.target));

		return response;
	}

	public static Vehicle fromJson(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Vehicle mappedPoint = mapper.readValue(json, Vehicle.class);
			return mappedPoint;
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Vehicle fromJson(String json, String attributesJson) {
		Vehicle mappedVehicle = Vehicle.fromJson(json);
		mappedVehicle.attributes = JsonCarAttributes.fromJson(new ByteArrayInputStream(attributesJson.getBytes()));
		return mappedVehicle;
	}
}
