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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import recipes_service.data.Operation;

/**
 * @author Joan-Manuel Marques, Daniel Lázaro Iglesias
 * December 2012
 *
 */
public class Log implements Serializable{

	private static final long serialVersionUID = -4864990265268259700L;
	/**
	 * This class implements a log, that stores the operations
	 * received  by a client.
	 * They are stored in a ConcurrentHashMap (a hash table),
	 * that stores a list of operations for each member of 
	 * the group.
	 */
	private ConcurrentHashMap<String, List<Operation>> log= new ConcurrentHashMap<String, List<Operation>>();  

	public Log(List<String> participants){
		// create an empty log
		for (Iterator<String> it = participants.iterator(); it.hasNext(); ){
			log.put(it.next(), new Vector<Operation>());
		}
	}

	/**
	 * inserts an operation into the log. Operations are 
	 * inserted in order. If the last operation for 
	 * the user is not the previous operation than the one 
	 * being inserted, the insertion will fail.
	 * 
	 * @param op
	 * @return true if op is inserted, false otherwise.
	 */
	public synchronized boolean add(Operation op){
		/******TODO******/
		//First get the HostID  through the class op(that is class operation) method getHostid() 
		String hostID = op.getTimestamp().getHostid(); 
		// call a method to obtain the lastTimestamp of hostID
        Timestamp lastTimestamp = this.returnLastTimestamp(hostID);
        //create class timestampDifference to compare
        Timestamp timestampDifference=op.getTimestamp();
        //obtain in long the difference with 
        long longTimestampDifference = timestampDifference.compare(lastTimestamp); 
        //create variable if Add to controller the value return and if op is inserted
        boolean result=false;
        //if lastTimestamp is null and the difference is 0 result is true
        if (lastTimestamp == null && longTimestampDifference == 0)result=true;
        //if lastTimestamp is not null and the difference is 1 result is true
        if(lastTimestamp != null && longTimestampDifference == 1)result=true;
        //if result is true the operation is add
        if(result)this.log.get(hostID).add(op);    
        return result;        
        /******TODO******/
	}
	/**
	 * This method return the LastTimestamp
	 * @param: String with the host ID
	 * @return: null or the lastTimesTamp 
	 * */
	private Timestamp returnLastTimestamp(String hostID) {
		//Create a List of Operations
      List<Operation> operations = this.log.get(hostID);
      //If operations is null or is empty(method of interface List) return null
      if (operations == null || operations.isEmpty()) {
          return null;
      } else {
    	  //the last is the list is their size -1
    	  int lastOperationsList=operations.size() - 1;
    	//if is OK return the Timestamp the last of list 
    	  return operations.get(lastOperationsList).getTimestamp();
      }
		
	}
	
	/**
	 * Checks the received summary (sum) and determines the operations
	 * contained in the log that have not been seen by
	 * the proprietary of the summary.
	 * Returns them in an ordered list.
	 * @param sum
	 * @return list of operations
	 */
	public synchronized List<Operation> listNewer(TimestampVector sum){
		List<Operation> operations = new ArrayList<>();

		for (String host_name : this.log.keySet()) {
			Timestamp last_timestamp = sum.getLast(host_name);
			List<Operation> operacions_host = this.log.get(host_name);
			for (Operation o : operacions_host) {
				if (o.getTimestamp().compare(last_timestamp) < 1) {
					operations.add(o);
				}
			}
		}

		return operations;

	}
	
	/**
	 * Removes from the log the operations that have
	 * been acknowledged by all the members
	 * of the group, according to the provided
	 * ackSummary. 
	 * @param ack: ackSummary.
	 */
	public synchronized void purgeLog(TimestampMatrix ack){
	}

	/**
	 * equals
	 * @param object
	 * @return boolean with the result of operation
	 */
	@Override
	public synchronized boolean equals(Object obj) {
		/******TODO******/
		boolean result;
		//if object is null return false and is not necessary do anything
		if(obj==null)return false;
		//if object is this same return true because are equals 
	    if (this == obj)return true;
	    //if object is not object log return false  
        if (!(obj instanceof Log))return false;    
        //create object log with the @param
        Log compare = (Log) obj;
        //return the result of method compare
        result=this.log.equals(compare.log);
        return result;
        /******TODO******/
	}

	/**
	 * toString
	 */
	@Override
	public synchronized String toString() {
		String name="";
		for(Enumeration<List<Operation>> en=log.elements();
		en.hasMoreElements(); ){
		List<Operation> sublog=en.nextElement();
		for(ListIterator<Operation> en2=sublog.listIterator(); en2.hasNext();){
			name+=en2.next().toString()+"\n";
		}
	}
		
		return name;
	}

}
