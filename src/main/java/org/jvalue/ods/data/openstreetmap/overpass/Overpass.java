
package org.jvalue.ods.data.openstreetmap.overpass;

import java.util.List;

public class Overpass{
   	private List<?> elements;
   	private String generator;
   	private Osm3s osm3s;
   	private Number version;

 	public List<?> getElements(){
		return this.elements;
	}
	public void setElements(List<?> elements){
		this.elements = elements;
	}
 	public String getGenerator(){
		return this.generator;
	}
	public void setGenerator(String generator){
		this.generator = generator;
	}
 	public Osm3s getOsm3s(){
		return this.osm3s;
	}
	public void setOsm3s(Osm3s osm3s){
		this.osm3s = osm3s;
	}
 	public Number getVersion(){
		return this.version;
	}
	public void setVersion(Number version){
		this.version = version;
	}
}
