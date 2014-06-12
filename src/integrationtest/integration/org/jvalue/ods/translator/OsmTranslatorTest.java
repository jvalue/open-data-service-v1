/*
 * 
 */
package integration.org.jvalue.ods.translator;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.translator.OsmTranslator;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class OsmTranslatorTest.
 */
public class OsmTranslatorTest {

	/** The translator. */
	private OsmTranslator translator;

	/** The test url. */
	private final String testUrl = "http://api.openstreetmap.org/api/0.6/map?bbox=9.382840810129357,52.78909755467678,9.392840810129357,52.79909755467678";

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		translator = new OsmTranslator();
		assertNotNull(translator);
	}

	/**
	 * Test Translate.
	 */
	@Test
	public void testTranslate() {
		ListObject lv = (ListObject) translator.translate(new DataSource("testUrl", testUrl,
				null, null, null, null) {});
		assertNotNull(lv);
	}

	/**
	 * Test Translate.
	 */
	@Test
	public void testTranslateOffline() {
		ListObject lv = (ListObject) translator.translate(new DataSource("osm",
				"/nbgcity.osm", null, null, null, null) {});
		assertNotNull(lv);
		DbAccessor<JsonNode> db = DbFactory.createDbAccessor("testOsm");
		db.connect();

		List<MapObject> listMap = new LinkedList<MapObject>();

		for (Serializable gv : lv.getList()) {
			listMap.add((MapObject) gv);
		}

		db.executeBulk(listMap, null);
		db.deleteDatabase();
	}

	/**
	 * Test Translate invalid source.
	 */
	@Test
	public void testTranslateInvalidSource() {
		ListObject lv = (ListObject) translator.translate(new DataSource("invalidSource",
				"invalidsource", null, null, null, null){});
		assertNull(lv);
	}

	/**
	 * Test Translate null source.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testTranslateNullSource() {
		translator.translate(null);
	}

}
