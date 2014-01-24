
package org.jvalue.ods.data.openstreetmap.overpass;

import java.util.List;

public class Elements{
   	private Number id;
   	private Number lat;
   	private Number lon;
   	private Tags tags;
   	private String type;

 	public Number getId(){
		return this.id;
	}
	public void setId(Number id){
		this.id = id;
	}
 	public Number getLat(){
		return this.lat;
	}
	public void setLat(Number lat){
		this.lat = lat;
	}
 	public Number getLon(){
		return this.lon;
	}
	public void setLon(Number lon){
		this.lon = lon;
	}
 	public Tags getTags(){
		return this.tags;
	}
	public void setTags(Tags tags){
		this.tags = tags;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
}
