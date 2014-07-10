/*
 * 
 */
package integration.org.jvalue.ods.translator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.translator.Translator;
import org.jvalue.ods.translator.TranslatorFactory;
import org.jvalue.ods.utils.DummyDataSource;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class OsmTranslatorTest.
 */
public class OsmTranslatorTest {

	/*
	 * TODO for this to work, integration tests such reside in the same package, not
	 * in integration....
	 *
	private Translator translator;

	private final String testUrl = "http://api.openstreetmap.org/api/0.6/map?bbox=9.382840810129357,52.78909755467678,9.392840810129357,52.79909755467678";

	@Before
	public void setUp() {
		translator = TranslatorFactory.getOsmTranslator();
		assertNotNull(translator);
	}

	@Test
	public void testTranslate() {
		ListObject lv = (ListObject) translator.translate(DummyDataSource.newInstance("testUrl", testUrl));
		assertNotNull(lv);
	}

	@Test
	public void testTranslateOffline() {
		ListObject lv = (ListObject) translator.translate(DummyDataSource.newInstance("osm", "/nbgcity.osm"));
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

	@Test
	public void testTranslateInvalidSource() {
		ListObject lv = (ListObject) translator.translate(DummyDataSource.newInstance("invalidSource", "invalidsource"));
		assertNull(lv);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTranslateNullSource() {
		translator.translate(null);
	}
	*/

}
