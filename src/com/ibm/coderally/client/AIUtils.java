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

import java.util.Random;

import com.ibm.coderally.geo.CheckPoint;
import com.ibm.coderally.geo.Point;
import com.ibm.coderally.track.Track;

/**
 * Static utility methods for the AI
 */
public class AIUtils {
	
	/**
	 * Returns true if the other car is ahead of (or beating) the other car.
	 * 
	 * @param track
	 * @param us
	 * @param them
	 * @return true if is ahead
	 */
	public static boolean isCarAhead(Track track, Vehicle us, Vehicle them) {
		if (us.getLap() != them.getLap()) {
			return us.getLap() > them.getLap();
		}
		int index = track.getCheckpoints().indexOf(us.getCheckpoint());
		int otherIndex = track.getCheckpoints().indexOf(them.getCheckpoint());
		return otherIndex > index;
	}

	/**
	 * Returns a random point on the CheckPoint
	 * @param vehicle
	 */
	public static Point randomUpdateCheckpoint(Vehicle vehicle) {
		Random r = new Random();
		int s = r.nextInt(3);
		if (s == 0) {
			return vehicle.getCheckpoint().getCenter();
		} else if (s == 1) {
			return vehicle.getCheckpoint().getStart();
		}
		return vehicle.getCheckpoint().getEnd();
	}

	public static Point getClosestLane(CheckPoint point, Point pos) {
		Point startMid = point.getCenter().midpoint(point.getStart());
		Point endMid = point.getCenter().midpoint(point.getEnd());
		
		if (pos.getDistanceSquared(endMid) > pos.getDistanceSquared(startMid)) {
			return startMid;
		}
		return endMid;
	}

	public static Point getAlternativeLane(CheckPoint point, Point pos) {
		Point startMid = point.getCenter().midpoint(point.getStart());
		Point endMid = point.getCenter().midpoint(point.getEnd());

		if (pos.getDistanceSquared(endMid) < pos.getDistanceSquared(startMid)) {
			if (pos.getDistanceSquared(point.getCenter()) < pos.getDistanceSquared(endMid)) {
				return point.getCenter();
			} else {
				return startMid;
			}
		} else if (pos.getDistanceSquared(point.getCenter()) < pos.getDistanceSquared(startMid)) {
			return point.getCenter();
		} else {
			return endMid;
		}
	}

}
