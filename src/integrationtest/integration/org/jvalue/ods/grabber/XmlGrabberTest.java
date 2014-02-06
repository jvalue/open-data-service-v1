package integration.org.jvalue.ods.grabber;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.GenericValue;
import org.jvalue.ods.grabber.XmlGrabber;

/**
 * The Class XmlGrabberTest.
 */
public class XmlGrabberTest {

	/** The grabber. */
	private XmlGrabber grabber;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		grabber = new XmlGrabber();
		assertNotNull(grabber);
	}

	/**
	 * Test grab.
	 */
	@Test
	public void testGrab() {
		GenericValue gv = grabber.grab("test.osm");
		assertNotNull(gv);			
	}
	
	/**
	 * Test grab not existing file.
	 */
	@Test
	public void testGrabNotExistingFile() {
		GenericValue gv = grabber.grab("NotExistingFile");
		assertNull(gv);		
	}
	
	
	/**
	 * Test grab null source.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGrabNullSource() {
		GenericValue gv = grabber.grab(null);			
	}
}
