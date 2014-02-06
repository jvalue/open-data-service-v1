package integration.org.jvalue.ods.grabber;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.GenericValue;
import org.jvalue.ods.grabber.XmlGrabber;

public class XmlGrabberTest {

	private XmlGrabber grabber;
	
	@Before
	public void setUp() throws Exception {
		grabber = new XmlGrabber();
		assertNotNull(grabber);
	}

	@Test
	public void testGrab() {
		GenericValue gv = grabber.grab("test.osm");
		assertNotNull(gv);			
	}
	
	@Test
	public void testGrabNotExistingFile() {
		GenericValue gv = grabber.grab("NotExistingFile");
		assertNull(gv);		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testGrabNullSource() {
		GenericValue gv = grabber.grab(null);
		assertNull(gv);		
	}
}
