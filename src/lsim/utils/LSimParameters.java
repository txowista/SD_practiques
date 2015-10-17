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

package lsim.utils;

/**
 * @author Daniel Llamazares
 *
 */
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LSimParameters implements LSimParametersAPI, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Map<String, Object> parameters;
	
	public LSimParameters() {
		this.parameters = new HashMap<String, Object>();
	}
	
	public Map<String, Object> getParameters() {
		return this.parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	@Override
	public Object get(String key) {
		return parameters.get(key);
	}
	
	@Override
	public void put(String key, Object param) {
		this.parameters.put(key, param);
	}

	@Override
	public Object remove(String key) {
		return this.parameters.remove(key);		
	}
	
	@Override
	public LSimParameters clone() {
		// the keys and values themselves are not cloned
		
		LSimParameters res = new LSimParameters();
		res.setParameters((Map<String, Object>) ((HashMap<String, Object>)this.parameters).clone());
		return res;
	}
	
	@Override
	public String toString() {
		return this.parameters.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.parameters.equals(((LSimParameters) obj).getParameters());
	}

	@Override
	public void putAll(Object params) {
		this.parameters.putAll((Map<String, Object>) params);
	}

	@Override
	public int size() {
		return this.parameters.size();
	}
	
}
