/*
 * This file is part of Transitime.org
 * 
 * Transitime.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPL) as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Transitime.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Transitime.org .  If not, see <http://www.gnu.org/licenses/>.
 */

package org.transitime.db.structs;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;
import org.transitime.db.hibernate.HibernateUtils;

/**
 * A database object for persisting information on how accurate a prediction was
 * compared to the actual measured arrival/departure time for the vehicle.
 *
 * @author SkiBu Smith
 *
 */
@Entity @DynamicUpdate 
@Table(name="PredictionAccuracy") 
@org.hibernate.annotations.Table(appliesTo = "PredictionAccuracy", 
indexes = { @Index(name="PredictionAccuracyTimeIndex", 
                   columnNames={"arrivalDepartureTime"} ) } )
public class PredictionAccuracy implements Serializable {

	// Need an ID but using regular columns doesn't really make
	// sense. So use an auto generated one. Not final since 
	// autogenerated and therefore not set in constructor.
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(length=HibernateUtils.DEFAULT_ID_SIZE)
	private final String routeId;
	
	@Column(length=HibernateUtils.DEFAULT_ID_SIZE)
	private final String directionId;
	
	@Column(length=HibernateUtils.DEFAULT_ID_SIZE)
	private final String stopId;
	
	// So can see which trip predictions for so can easily determine
	// what the travel times are and see if they appear to be correct.
	@Column(length=HibernateUtils.DEFAULT_ID_SIZE)
	private final String tripId;
	
	@Column	
	@Temporal(TemporalType.TIMESTAMP)
	private final Date arrivalDepartureTime;
	
	// The predicted time the vehicle was expected to arrive/depart the stop
	@Column	
	@Temporal(TemporalType.TIMESTAMP)
	private final Date predictedTime;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private final Date predictionReadTime;

	// Positive means vehicle arrived at stop later then predicted for and
	// negative value means vehicle arrived earlier.
	@Column
	private final int predictionAccuracyMsecs;

	@Column(length=HibernateUtils.DEFAULT_ID_SIZE)
	private final String predictionSource;
	
	@Column(length=HibernateUtils.DEFAULT_ID_SIZE)
	private final String vehicleId;

	@Column
	private final Boolean affectedByWaitStop;
	
	private static final long serialVersionUID = -6900411351649946446L;

	/********************** Member Functions **************************/

	/**
	 * Simple constructor
	 * 
	 * @param routeId
	 * @param directionId
	 * @param stopId
	 * @param tripId
	 * @param arrivalDepartureTime
	 * @param predictedTime
	 *            The time the vehicle was predicted to arrive at the stop
	 * @param predictionReadTime
	 * @param predictionSource
	 * @param vehicleId
	 */
	public PredictionAccuracy(String routeId, String directionId,
			String stopId, String tripId, Date arrivalDepartureTime,
			Date predictedTime, Date predictionReadTime,
			String predictionSource, String vehicleId, 
			Boolean affectedByWaitStop) {
		super();
		this.routeId = routeId;
		this.directionId = directionId;
		this.stopId = stopId;
		this.tripId = tripId;
		this.arrivalDepartureTime = arrivalDepartureTime;
		this.predictedTime = predictedTime;
		this.predictionReadTime = predictionReadTime;
		this.predictionAccuracyMsecs = arrivalDepartureTime!= null ? 
				(int) (arrivalDepartureTime.getTime() - predictedTime.getTime()) : 0;
		this.predictionSource = predictionSource;
		this.vehicleId = vehicleId;
		this.affectedByWaitStop = affectedByWaitStop;
	}

	/**
	 * Hibernate requires a no-arg constructor for reading objects
	 * from database.
	 */
	protected PredictionAccuracy() {
		super();
		this.routeId = null;
		this.directionId = null;
		this.stopId = null;
		this.tripId = null;
		this.arrivalDepartureTime = null;
		this.predictedTime = null;
		this.predictionReadTime = null;
		this.predictionAccuracyMsecs = -1;
		this.predictionSource = null;
		this.vehicleId = null;
		this.affectedByWaitStop = null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((affectedByWaitStop == null) ? 0 : affectedByWaitStop
						.hashCode());
		result = prime
				* result
				+ ((arrivalDepartureTime == null) ? 0 : arrivalDepartureTime
						.hashCode());
		result = prime * result
				+ ((directionId == null) ? 0 : directionId.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((predictedTime == null) ? 0 : predictedTime.hashCode());
		result = prime * result + predictionAccuracyMsecs;
		result = prime
				* result
				+ ((predictionReadTime == null) ? 0 : predictionReadTime
						.hashCode());
		result = prime
				* result
				+ ((predictionSource == null) ? 0 : predictionSource.hashCode());
		result = prime * result + ((routeId == null) ? 0 : routeId.hashCode());
		result = prime * result + ((stopId == null) ? 0 : stopId.hashCode());
		result = prime * result + ((tripId == null) ? 0 : tripId.hashCode());
		result = prime * result
				+ ((vehicleId == null) ? 0 : vehicleId.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PredictionAccuracy other = (PredictionAccuracy) obj;
		if (affectedByWaitStop == null) {
			if (other.affectedByWaitStop != null)
				return false;
		} else if (!affectedByWaitStop.equals(other.affectedByWaitStop))
			return false;
		if (arrivalDepartureTime == null) {
			if (other.arrivalDepartureTime != null)
				return false;
		} else if (!arrivalDepartureTime.equals(other.arrivalDepartureTime))
			return false;
		if (directionId == null) {
			if (other.directionId != null)
				return false;
		} else if (!directionId.equals(other.directionId))
			return false;
		if (id != other.id)
			return false;
		if (predictedTime == null) {
			if (other.predictedTime != null)
				return false;
		} else if (!predictedTime.equals(other.predictedTime))
			return false;
		if (predictionAccuracyMsecs != other.predictionAccuracyMsecs)
			return false;
		if (predictionReadTime == null) {
			if (other.predictionReadTime != null)
				return false;
		} else if (!predictionReadTime.equals(other.predictionReadTime))
			return false;
		if (predictionSource == null) {
			if (other.predictionSource != null)
				return false;
		} else if (!predictionSource.equals(other.predictionSource))
			return false;
		if (routeId == null) {
			if (other.routeId != null)
				return false;
		} else if (!routeId.equals(other.routeId))
			return false;
		if (stopId == null) {
			if (other.stopId != null)
				return false;
		} else if (!stopId.equals(other.stopId))
			return false;
		if (tripId == null) {
			if (other.tripId != null)
				return false;
		} else if (!tripId.equals(other.tripId))
			return false;
		if (vehicleId == null) {
			if (other.vehicleId != null)
				return false;
		} else if (!vehicleId.equals(other.vehicleId))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "PredictionAccuracy [" 
				+ "routeId=" + routeId
				+ ", directionId=" + directionId 
				+ ", stopId=" + stopId
				+ ", tripId=" + tripId
				+ ", arrivalDepartureTime=" + arrivalDepartureTime
				+ ", predictedTime=" + predictedTime
				+ ", predictionReadTime=" + predictionReadTime
				+ ", predictionLengthMsecs=" + getPredictionLengthMsecs()
				+ ", predictionAccuracyMsecs=" + predictionAccuracyMsecs
				+ ", predictionSource=" + predictionSource 
				+ ", vehicleId=" + vehicleId 
				+ ", affectedByWaitStop=" + affectedByWaitStop
				+ "]";
	}

	public String getRouteId() {
		return routeId;
	}

	public String getDirectionId() {
		return directionId;
	}

	public String getStopId() {
		return stopId;
	}

	public String getTripId() {
		return tripId;
	}
	
	public Date getArrivalDepartureTime() {
		return arrivalDepartureTime;
	}

	public Date getPredictedTime() {
		return predictedTime;
	}
	
	public Date getPredictionReadTime() {
		return predictionReadTime;
	}
	
	public int getPredictionLengthMsecs() {
		return (int) (predictedTime.getTime() - predictionReadTime.getTime());
	}

	public int getPredictionAccuracyMsecs() {
		return predictionAccuracyMsecs;
	}

	public String getPredictionSource() {
		return predictionSource;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	/**
	 * True if the prediction is based on scheduled departure time, false if
	 * not. Null if feed of predictions doesn't provide that information.
	 * 
	 * @return
	 */
	public Boolean isAffectedByWaitStop() {
		return affectedByWaitStop;
	}
}
