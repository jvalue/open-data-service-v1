/*
 * 
 */
package integration.org.jvalue.ods.grabber;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.data.generic.ListValue;
import org.jvalue.ods.data.generic.MapValue;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.translator.OsmTranslator;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class OsmGrabberTest.
 */
public class OsmGrabberTest {

	/** The grabber. */
	private OsmTranslator grabber;

	/** The test url. */
	private final String testUrl = "http://api.openstreetmap.org/api/0.6/map?bbox=9.382840810129357,52.78909755467678,9.392840810129357,52.79909755467678";

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		grabber = new OsmTranslator();
		assertNotNull(grabber);
	}

	/**
	 * Test grab.
	 */
	@Test
	public void testGrab() {
		ListValue lv = (ListValue) grabber.translate(new DataSource(testUrl, null));
		assertNotNull(lv);
	}

	/**
	 * Test grab.
	 */
	@Test
	public void testGrabOffline() {
		ListValue lv = (ListValue) grabber.translate(new DataSource("/nbgcity.osm",
				null));
		assertNotNull(lv);
		DbAccessor<JsonNode> db = DbFactory.createDbAccessor("testOsm");
		db.connect();

		List<MapValue> listMap = new LinkedList<MapValue>();

		for (GenericValue gv : lv.getList()) {
			listMap.add((MapValue) gv);
		}

		db.executeBulk(listMap, null);
		db.deleteDatabase();
	}

	/**
	 * Test grab invalid source.
	 */
	@Test
	public void testGrabInvalidSource() {
		ListValue lv = (ListValue) grabber.translate(new DataSource("invalidsource",
				null));
		assertNull(lv);
	}

	/**
	 * Test grab null source.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGrabNullSource() {
		grabber.translate(null);
	}

}
