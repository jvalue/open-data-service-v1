package integration.org.jvalue.ods.grabber;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.osm.OsmData;
import org.jvalue.ods.grabber.OsmGrabber;

public class OsmGrabberTest {

	private OsmGrabber grabber;

	private final String testUrl = "http://api.openstreetmap.org/api/0.6/map?bbox=9.382840810129357,52.78909755467678,9.392840810129357,52.79909755467678";

	@Before
	public void setUp() {
		grabber = new OsmGrabber();
		assertNotNull(grabber);
	}

	@Test
	public void testGrab() {
		OsmData d = grabber.grab(testUrl);
		assertNotNull(d);
	}

	@Test
	public void testGrabInvalidSource() {
		OsmData d = grabber.grab("invalidsource");
		assertTrue(d.getNodes().isEmpty());
		assertTrue(d.getWays().isEmpty());
		assertTrue(d.getRelations().isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGrabNullSource() {
		grabber.grab(null);
	}

}
