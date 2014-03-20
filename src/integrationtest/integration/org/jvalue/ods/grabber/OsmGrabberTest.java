/*
 * 
 */
package integration.org.jvalue.ods.grabber;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.osm.OsmData;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.grabber.OsmGrabber;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class OsmGrabberTest.
 */
public class OsmGrabberTest {

	/** The grabber. */
	private OsmGrabber grabber;

	/** The test url. */
	private final String testUrl = "http://api.openstreetmap.org/api/0.6/map?bbox=9.382840810129357,52.78909755467678,9.392840810129357,52.79909755467678";

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		grabber = new OsmGrabber();
		assertNotNull(grabber);
	}

	/**
	 * Test grab.
	 */
	@Test
	public void testGrab() {
		OsmData d = grabber.grab(testUrl);
		assertNotNull(d);
	}
	
	
	/**
	 * Test grab.
	 */
	@Test
	public void testGrabOffline() {
		OsmData d = grabber.grab("/nbgcity.osm");
		assertNotNull(d);
		DbAccessor<JsonNode> db = DbFactory.createDbAccessor("testOsm");
		db.connect();
		db.executeBulk(d.getNodes());
		db.executeBulk(d.getRelations());
		db.executeBulk(d.getWays());	
		db.deleteDatabase();
	}

	
	/**
	 * Test grab invalid source.
	 */
	@Test
	public void testGrabInvalidSource() {
		OsmData d = grabber.grab("invalidsource");
		assertNull(d);
	}

	/**
	 * Test grab null source.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGrabNullSource() {
		grabber.grab(null);
	}

}
