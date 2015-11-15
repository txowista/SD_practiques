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

package recipes_service.tsae.sessions;


import java.io.IOException;
import java.net.Socket;

import communication.ObjectInputStream_DS;
import communication.ObjectOutputStream_DS;
import recipes_service.ServerData;
//TODO 
//import new for the phase 2
import recipes_service.data.*;
import recipes_service.tsae.data_structures.*;
import recipes_service.communication.*;
import java.util.*;

/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public class TSAESessionPartnerSide extends Thread{
	private Socket socket = null;
	private ServerData serverData = null;
	
	public TSAESessionPartnerSide(Socket socket, ServerData serverData) {
		super("TSAEPartnerSideThread");
		this.socket = socket;
		this.serverData = serverData;
	}

	public void run() {
				
		try {
			ObjectOutputStream_DS out = new ObjectOutputStream_DS(socket.getOutputStream());
			ObjectInputStream_DS in = new ObjectInputStream_DS(socket.getInputStream());		
			// receive originator's summary and ack
			//TODO
			Message msg = (Message) in.readObject();
			if (msg.type() == MsgType.AE_REQUEST){
		   /******TODO******/
				/**create object MessageAErequest since the msg received via socket to use the method
				*of class
				**/
				MessageAErequest aeRquestMsg = (MessageAErequest) msg;
				/** Get the LocalSumary and localAck
				 * */
				TimestampVector localSummary;
				TimestampMatrix localAck;	
				/** Use synchronized to lock the object serverdata thus 
				 * we are careful with concurrent access to data structures
				 * */
	            synchronized (serverData) {
	            	//localSumary is a clone of local 
	                localSummary = serverData.getSummary().clone();
	                serverData.getAck().update(serverData.getId(), localSummary);
	                //localAck is a clone of local 
	                localAck=serverData.getAck().clone();	               
	            }
	           
	        /******TODO******/
			// send operations
			
			// send to originator: local's summary and ack			
			msg = new MessageAErequest(localSummary, localAck);			
			out.writeObject(msg);
			// receive operations
			msg = (Message) in.readObject();
			while (msg.type() == MsgType.OPERATION){			
				msg = (Message) in.readObject();
			}
			}
			// receive message to inform about the ending of the TSAE session
			if (msg.type() == MsgType.END_TSAE){
			// send and "end of TSAE session" message
			msg = new MessageEndTSAE();
			out.writeObject(msg);
			}
			// receive operations
				
			// receive message to inform about the ending of the TSAE session

			// send and "end of TSAE session" message

			socket.close();		
		} //catch (ClassNotFoundException e) {//Comment because Eclipse said Description	Resource	Path	Location	Type
		//Unreachable catch block for ClassNotFoundException. This exception is never thrown from the try statement body	TSAESessionPartnerSide.java	/2015t-practiques-SD--baseCode/src/recipes_service/tsae/sessions	line 65	Java Problem

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            System.exit(1);
		}/*catch (IOException e) {//Comment because Exception
	    }*/
	}
}
