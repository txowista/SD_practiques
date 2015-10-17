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
 * Interface to get the parameters specified in the XML that we send to the frontend
 * @author Daniel Llamazares
 *
 */
public interface LSimParametersAPI {

	/**
	 * Returns the value to which the specified key is mapped, or null if this key doesn't exist.
	 * @param key the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or null if this key doesn't exist.
	 */
	public Object get(String key);
	
	/**
	 * Associates the specified value with the specified key.
	 * If the key was previously contained, the old value is replaced by the specified value.
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 */
	public void put(String key, Object param);
	
	/**
	 * Copies all the parameters from params
	 * @param params all the parameters to be copied
	 */
	public void putAll(Object params);
	
	/**
	 * Removes the mapping for a key if it is present.
	 * @param key key whose mapping is to be removed
	 * @return the previous value associated with key, or null if there was no mapping for key
	 */
	public Object remove(String key);
	
	/**
	 * Returns the size
	 * @return size
	 */
	public int size();
	
	/**
	 * Returns a shallow copy of this instance: the keys and values themselves are not cloned.
	 * @return a shallow copy of this instance
	 */
	public Object clone();
	
	/**
	 * Returns a string representation of the object.
	 * @return a string representation of the object
	 */
	public String toString();
	
	/**
	 * Indicates whether some other object is "equal to" this one.
	 * @param obj the reference object with which to compare
	 * @returns true if this object is the same as the obj argument; false otherwise
	 */
	public boolean equals(Object obj);

}
