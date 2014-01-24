/*
 * 
 */

package org.jvalue.ods.data.opensteetmap.nominatim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class NominatimQueryResult.
 */
public class NominatimQueryResult{
   	
	   /** The boundingbox. */
	   private List boundingbox;
   	
   	/** The clas. */
	   @JsonProperty(value = "class")
   	private String clas;
   	
   	/** The display_name. */
	   private String display_name;
   	
	   /** The icon. */
	   private String icon;
   	
	   /** The importance. */
	   private Number importance;
   	
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
   	
	   /** The type. */
	   private String type;

 	/**
	  * Gets the boundingbox.
	  *
	  * @return the boundingbox
	  */
	 public List getBoundingbox(){
		return this.boundingbox;
	}
	
	/**
	 * Sets the boundingbox.
	 *
	 * @param boundingbox the new boundingbox
	 */
	public void setBoundingbox(List boundingbox){
		this.boundingbox = boundingbox;
	}
 	
	 /**
	  * Gets the clas.
	  *
	  * @return the clas
	  */
	 public String getClas(){
		return this.clas;
	}
	
	/**
	 * Sets the class.
	 *
	 * @param clas the new class
	 */
	public void setClass(String clas){
		this.clas = clas;
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
	  * Gets the icon.
	  *
	  * @return the icon
	  */
	 public String getIcon(){
		return this.icon;
	}
	
	/**
	 * Sets the icon.
	 *
	 * @param icon the new icon
	 */
	public void setIcon(String icon){
		this.icon = icon;
	}
 	
	 /**
	  * Gets the importance.
	  *
	  * @return the importance
	  */
	 public Number getImportance(){
		return this.importance;
	}
	
	/**
	 * Sets the importance.
	 *
	 * @param importance the new importance
	 */
	public void setImportance(Number importance){
		this.importance = importance;
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
 	
	 /**
	  * Gets the type.
	  *
	  * @return the type
	  */
	 public String getType(){
		return this.type;
	}
	
	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type){
		this.type = type;
	}
}
