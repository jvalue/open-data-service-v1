/*  Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
*/

package org.jvalue.ods.data.opensteetmap.nominatim;

/**
 * The Class NominatimReverseQueryResult.
 */
public class NominatimReverseQueryResult{
   	
	   /** The address. */
	   private Address address;
   	
	   /** The display_name. */
	   private String display_name;
   	
	   /** The lat. */
	   private String lat;
   	
	   /** The licence. */
	   private String licence;
   	
	   /** The lon. */
	   private String lon;
   	
	   /** The osm_id. */
	   private String osm_id;
   	
	   /** The osm_type. */
	   private String osm_type;
   	
	   /** The place_id. */
	   private String place_id;

 	/**
	  * Gets the address.
	  *
	  * @return the address
	  */
	 public Address getAddress(){
		return this.address;
	}
	
	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(Address address){
		this.address = address;
	}
 	
	 /**
	  * Gets the display_name.
	  *
	  * @return the display_name
	  */
	 public String getDisplay_name(){
		return this.display_name;
	}
	
	/**
	 * Sets the display_name.
	 *
	 * @param display_name the new display_name
	 */
	public void setDisplay_name(String display_name){
		this.display_name = display_name;
	}
 	
	 /**
	  * Gets the lat.
	  *
	  * @return the lat
	  */
	 public String getLat(){
		return this.lat;
	}
	
	/**
	 * Sets the lat.
	 *
	 * @param lat the new lat
	 */
	public void setLat(String lat){
		this.lat = lat;
	}
 	
	 /**
	  * Gets the licence.
	  *
	  * @return the licence
	  */
	 public String getLicence(){
		return this.licence;
	}
	
	/**
	 * Sets the licence.
	 *
	 * @param licence the new licence
	 */
	public void setLicence(String licence){
		this.licence = licence;
	}
 	
	 /**
	  * Gets the lon.
	  *
	  * @return the lon
	  */
	 public String getLon(){
		return this.lon;
	}
	
	/**
	 * Sets the lon.
	 *
	 * @param lon the new lon
	 */
	public void setLon(String lon){
		this.lon = lon;
	}
 	
	 /**
	  * Gets the osm_id.
	  *
	  * @return the osm_id
	  */
	 public String getOsm_id(){
		return this.osm_id;
	}
	
	/**
	 * Sets the osm_id.
	 *
	 * @param osm_id the new osm_id
	 */
	public void setOsm_id(String osm_id){
		this.osm_id = osm_id;
	}
 	
	 /**
	  * Gets the osm_type.
	  *
	  * @return the osm_type
	  */
	 public String getOsm_type(){
		return this.osm_type;
	}
	
	/**
	 * Sets the osm_type.
	 *
	 * @param osm_type the new osm_type
	 */
	public void setOsm_type(String osm_type){
		this.osm_type = osm_type;
	}
 	
	 /**
	  * Gets the place_id.
	  *
	  * @return the place_id
	  */
	 public String getPlace_id(){
		return this.place_id;
	}
	
	/**
	 * Sets the place_id.
	 *
	 * @param place_id the new place_id
	 */
	public void setPlace_id(String place_id){
		this.place_id = place_id;
	}
}
