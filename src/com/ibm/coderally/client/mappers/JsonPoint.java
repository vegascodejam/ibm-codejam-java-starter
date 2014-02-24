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
package com.ibm.coderally.client.mappers;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.coderally.geo.Point;
import com.ibm.json.java.JSONObject;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE,
getterVisibility=JsonAutoDetect.Visibility.NONE,
setterVisibility=JsonAutoDetect.Visibility.NONE,
creatorVisibility=JsonAutoDetect.Visibility.NONE,
isGetterVisibility=JsonAutoDetect.Visibility.NONE)
public class JsonPoint {
	@JsonProperty
	public double x;
	@JsonProperty
	public double y;
	public JsonPoint() {
	}


	public JsonPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point getPoint() {
		return new Point(this.x, this.y);
	}
	
	public static JSONObject getPointJSON(Point p) {
		JSONObject json = new JSONObject();
		json.put("y", p.getY());
		json.put("x", p.getX());
		return json;
	}

	public static Point fromJson(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonPoint mappedPoint = mapper.readValue(json, JsonPoint.class);
			return mappedPoint.getPoint();
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
