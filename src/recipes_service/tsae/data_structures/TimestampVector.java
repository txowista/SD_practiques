/*
* Copyright (c) Joan-Manuel Marques 2013. All rights reserved.
* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
*
* This file is part of the practical assignment of Distributed Systems course.
*
* This code is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This code is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this code.  If not, see <http://www.gnu.org/licenses/>.
*/

package recipes_service.tsae.data_structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public class TimestampVector implements Serializable{

	private static final long serialVersionUID = -765026247959198886L;
	/**
	 * This class stores a summary of the timestamps seen by a node.
	 * For each node, stores the timestamp of the last received operation.
	 */
	
	private ConcurrentHashMap<String, Timestamp> timestampVector= new ConcurrentHashMap<String, Timestamp>();
	
	public TimestampVector (List<String> participants){
		// create and empty TimestampVector
		for (Iterator<String> it = participants.iterator(); it.hasNext(); ){
			String id = it.next();
			// when sequence number of timestamp < 0 it means that the timestamp is the null timestamp
			timestampVector.put(id, new Timestamp(id, Timestamp.NULL_TIMESTAMP_SEQ_NUMBER));
		}
	}

	/**
	 * Updates the timestamp vector with a new timestamp. 
	 * @param timestamp
	 */
	public synchronized void updateTimestamp(Timestamp timestamp){
		//Comentar con Miguel si seria mejor un replace que un put
		//this.timestampVector.put(timestamp.getHostid(), timestamp);
		this.timestampVector.replace(timestamp.getHostid(), timestamp);
	}
	
	/**
	 * merge in another vector, taking the elementwise maximum
	 * @param tsVector (a timestamp vector)
	 */
	public synchronized void updateMax(TimestampVector tsVector){
		for (String key : this.timestampVector.keySet()) {
			Timestamp ts = tsVector.getLast(key);
			Timestamp ts_propio = this.getLast(key);
			if(ts_propio.compare(ts) < 0) {			
				this.timestampVector.replace(key, ts);			
			}
			/*Revisar junto con Miguel creo que seria mas optimo
			 * usar la funcion getLast
			 * Respecto a hacer un replace o un put ya que recorremos
			 * todos los nodos del actual.Si existe a traves del compare
			 * al ser superior deberiamos reemplazarlo mas que añadir uno nuevo
			 */
			/*Timestamp ts = tsVector.timestampVector.get(key);
			Timestamp ts_propio = this.timestampVector.get(key);
			if(ts_propio.compare(ts) < 0) {
				timestampVector.put(ts_propio.getHostid(), ts);
			}*/
		}
		
	}
	
	/**
	 * 
	 * @param node
	 * @return the last timestamp issued by node that has been
	 * received.
	 */
	public synchronized Timestamp getLast(String node){
		return this.timestampVector.get(node);
	}
	
	/**
	 * merges local timestamp vector with tsVector timestamp vector taking
	 * the smallest timestamp for each node.
	 * After merging, local node will have the smallest timestamp for each node.
	 *  @param tsVector (timestamp vector)
	 */
	public synchronized void mergeMin(TimestampVector tsVector){
		for (String key : tsVector.timestampVector.keySet()) {
			Timestamp ts = tsVector.timestampVector.get(key);
			Timestamp ts_propio = this.timestampVector.get(key);
			if(ts_propio.compare(ts) > 0) {
				timestampVector.put(ts_propio.getHostid(), ts);
			}

		}
	}
	
	/**
	 * clone
	 */
	public synchronized TimestampVector clone(){
		List<String> participants = new ArrayList<String>(timestampVector.keySet());
		TimestampVector clone = new TimestampVector(participants);

		for (String key : timestampVector.keySet()) {
			Timestamp ts = this.timestampVector.get(key);
			clone.timestampVector.put(ts.getHostid(), ts);
		}
		return clone;
				
	}
	/**
	 * equals
	 * @param TimestampVector
	 * @return true if TimestampVector is equals,or false.
	 */
	public synchronized boolean equals(TimestampVector tsVector){
		if (this == tsVector)
			return true;
		if (timestampVector == null) {
			if (tsVector.timestampVector != null)
				return false;
			else
				return true;
		} else {
			if (timestampVector.size() != tsVector.timestampVector.size()){
				return false;
			}
			boolean equal = true;
			for (Iterator<String> it = timestampVector.keySet().iterator(); it.hasNext() && equal; ){
				String host_name = it.next();
				equal = timestampVector.get(host_name).equals(tsVector.timestampVector.get(host_name));
				if (!equal){
				}
			}
			return equal;
		}
	}

	/**
	 * toString
	 */
	@Override
	public synchronized String toString() {
		String all="";
		if(timestampVector==null){
			return all;
		}
		for(Enumeration<String> en=timestampVector.keys(); en.hasMoreElements();){
			String name=en.nextElement();
			if(timestampVector.get(name)!=null)
				all+=timestampVector.get(name)+"\n";
		}
		return all;
	}
}
