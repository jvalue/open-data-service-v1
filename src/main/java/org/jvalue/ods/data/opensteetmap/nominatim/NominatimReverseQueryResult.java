
package org.jvalue.ods.data.opensteetmap.nominatim;

public class NominatimReverseQueryResult{
   	private Address address;
   	private String display_name;
   	private String lat;
   	private String licence;
   	private String lon;
   	private String osm_id;
   	private String osm_type;
   	private String place_id;

 	public Address getAddress(){
		return this.address;
	}
	public void setAddress(Address address){
		this.address = address;
	}
 	public String getDisplay_name(){
		return this.display_name;
	}
	public void setDisplay_name(String display_name){
		this.display_name = display_name;
	}
 	public String getLat(){
		return this.lat;
	}
	public void setLat(String lat){
		this.lat = lat;
	}
 	public String getLicence(){
		return this.licence;
	}
	public void setLicence(String licence){
		this.licence = licence;
	}
 	public String getLon(){
		return this.lon;
	}
	public void setLon(String lon){
		this.lon = lon;
	}
 	public String getOsm_id(){
		return this.osm_id;
	}
	public void setOsm_id(String osm_id){
		this.osm_id = osm_id;
	}
 	public String getOsm_type(){
		return this.osm_type;
	}
	public void setOsm_type(String osm_type){
		this.osm_type = osm_type;
	}
 	public String getPlace_id(){
		return this.place_id;
	}
	public void setPlace_id(String place_id){
		this.place_id = place_id;
	}
}
