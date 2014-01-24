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
 * The Class Address.
 */
public class Address {
	
	/** The city. */
	private String city;
	
	/** The country. */
	private String country;
	
	/** The country_code. */
	private String country_code;
	
	/** The county. */
	private String county;
	
	/** The house_number. */
	private String house_number;
	
	/** The postcode. */
	private String postcode;
	
	/** The road. */
	private String road;
	
	/** The state. */
	private String state;
	
	/** The state_district. */
	private String state_district;
	
	/** The suburb. */
	private String suburb;

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * Sets the country.
	 *
	 * @param country the new country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Gets the country_code.
	 *
	 * @return the country_code
	 */
	public String getCountry_code() {
		return this.country_code;
	}

	/**
	 * Sets the country_code.
	 *
	 * @param country_code the new country_code
	 */
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	/**
	 * Gets the county.
	 *
	 * @return the county
	 */
	public String getCounty() {
		return this.county;
	}

	/**
	 * Sets the county.
	 *
	 * @param county the new county
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * Gets the house_number.
	 *
	 * @return the house_number
	 */
	public String getHouse_number() {
		return this.house_number;
	}

	/**
	 * Sets the house_number.
	 *
	 * @param house_number the new house_number
	 */
	public void setHouse_number(String house_number) {
		this.house_number = house_number;
	}

	/**
	 * Gets the postcode.
	 *
	 * @return the postcode
	 */
	public String getPostcode() {
		return this.postcode;
	}

	/**
	 * Sets the postcode.
	 *
	 * @param postcode the new postcode
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	/**
	 * Gets the road.
	 *
	 * @return the road
	 */
	public String getRoad() {
		return this.road;
	}

	/**
	 * Sets the road.
	 *
	 * @param road the new road
	 */
	public void setRoad(String road) {
		this.road = road;
	}

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public String getState() {
		return this.state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Gets the state_district.
	 *
	 * @return the state_district
	 */
	public String getState_district() {
		return this.state_district;
	}

	/**
	 * Sets the state_district.
	 *
	 * @param state_district the new state_district
	 */
	public void setState_district(String state_district) {
		this.state_district = state_district;
	}

	/**
	 * Gets the suburb.
	 *
	 * @return the suburb
	 */
	public String getSuburb() {
		return this.suburb;
	}

	/**
	 * Sets the suburb.
	 *
	 * @param suburb the new suburb
	 */
	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}
}
