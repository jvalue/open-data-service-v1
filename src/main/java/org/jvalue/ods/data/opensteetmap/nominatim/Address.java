
package org.jvalue.ods.data.opensteetmap.nominatim;

public class Address{
   	private String city;
   	private String country;
   	private String country_code;
   	private String county;
   	private String house_number;
   	private String postcode;
   	private String road;
   	private String state;
   	private String state_district;
   	private String suburb;

 	public String getCity(){
		return this.city;
	}
	public void setCity(String city){
		this.city = city;
	}
 	public String getCountry(){
		return this.country;
	}
	public void setCountry(String country){
		this.country = country;
	}
 	public String getCountry_code(){
		return this.country_code;
	}
	public void setCountry_code(String country_code){
		this.country_code = country_code;
	}
 	public String getCounty(){
		return this.county;
	}
	public void setCounty(String county){
		this.county = county;
	}
 	public String getHouse_number(){
		return this.house_number;
	}
	public void setHouse_number(String house_number){
		this.house_number = house_number;
	}
 	public String getPostcode(){
		return this.postcode;
	}
	public void setPostcode(String postcode){
		this.postcode = postcode;
	}
 	public String getRoad(){
		return this.road;
	}
	public void setRoad(String road){
		this.road = road;
	}
 	public String getState(){
		return this.state;
	}
	public void setState(String state){
		this.state = state;
	}
 	public String getState_district(){
		return this.state_district;
	}
	public void setState_district(String state_district){
		this.state_district = state_district;
	}
 	public String getSuburb(){
		return this.suburb;
	}
	public void setSuburb(String suburb){
		this.suburb = suburb;
	}
}
