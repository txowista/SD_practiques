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
import java.util.List;
import java.util.TimerTask;

import recipes_service.ServerData;
import recipes_service.activity_simulation.SimulationData;
import recipes_service.communication.Host;
import communication.ObjectInputStream_DS;
import communication.ObjectOutputStream_DS;
//TODO 
//import new for the phase 2
import recipes_service.tsae.data_structures.*;
import recipes_service.communication.*;
import java.util.*;


/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public class TSAESessionOriginatorSide extends TimerTask{
	private ServerData serverData;
	public TSAESessionOriginatorSide(ServerData serverData){
		super();
		this.serverData=serverData;		
	}
	
	/**
	 * Implementation of the TimeStamped Anti-Entropy protocol
	 */
	public void run(){
		sessionWithN(serverData.getNumberSessions());
	}

	/**
	 * This method performs num TSAE sessions
	 * with num random servers
	 * @param num
	 */
	public void sessionWithN(int num){
		if(!SimulationData.getInstance().isConnected())
			return;
		List<Host> partnersTSAEsession= serverData.getRandomPartners(num);
		Host n;
		for(int i=0; i<partnersTSAEsession.size(); i++){
			n=partnersTSAEsession.get(i);
			sessionTSAE(n);
		}
	}
	
	/**
	 * This method perform a TSAE session
	 * with the partner server n
	 * @param n
	 */
	private void sessionTSAE(Host n){
		if (n == null) return;

		try {
			Socket socket = new Socket(n.getAddress(), n.getPort());
			ObjectInputStream_DS in = new ObjectInputStream_DS(socket.getInputStream());
			ObjectOutputStream_DS out = new ObjectOutputStream_DS(socket.getOutputStream());
			//Create the object localSummary with contain the summary of server 
			/******TODO******/
			TimestampVector localSummary;
			TimestampMatrix localAck;
			/** Use synchronized to lock the object serverdata thus 
			 * we are careful with concurrent access to data structures
			 * */
            synchronized (serverData) {
                localSummary = serverData.getSummary().clone();
                serverData.getAck().update(serverData.getId(), localSummary);
                localAck=serverData.getAck().clone();
               
            }
            /******TODO******/
			// Send to partner: local's summary and ack
            List<MessageOperation> operations = new ArrayList<>();
			Message msg = new MessageAErequest(localSummary, localAck);
            out.writeObject(msg);

            // receive operations from partner
            msg = (Message) in.readObject();
            while (msg.type() == MsgType.OPERATION){
            operations.add((MessageOperation)msg);
            msg = (Message) in.readObject();
            }

         // receive partner's summary and ack
            if (msg.type() == MsgType.AE_REQUEST){
            
            // send operations
            
            // send and "end of TSAE session" message
            msg = new MessageEndTSAE();
            out.writeObject(msg);
            // receive message to inform about the ending of the TSAE session
            msg = (Message) in.readObject();
            if (msg.type() == MsgType.END_TSAE){
            //
            }
            }

			// send and "end of TSAE session" message

			// receive message to inform about the ending of the TSAE session

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
