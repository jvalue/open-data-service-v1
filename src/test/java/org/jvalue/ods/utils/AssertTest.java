package org.jvalue.ods.utils;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;


public final class AssertTest {

	@Test(expected = NullPointerException.class)
	public void testAssertNotNullOne() {
		Object object = null;
		Assert.assertNotNull(object);
	}


	@Test(expected = NullPointerException.class)
	public void testAssertNotNullMultiple() {
		Assert.assertNotNull(new Object(), null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAssertTrue() {
		Assert.assertTrue(false);
	}


	@Test
	public void testAssertTrueMsg() {
		try {
			Assert.assertTrue(false, "error");
		} catch (IllegalArgumentException iae) {
			org.junit.Assert.assertEquals(iae.getMessage(), "error");
		}
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAssertFalse() {
		Assert.assertFalse(true, "error");
	}


	@Test
	public void testAssertEquals() {
		testAssertEquals("one", "two");
		testAssertEquals("one", null);
		testAssertEquals(null, "two");

		try {
			Assert.assertEquals(null, null, "error");
		} catch (IllegalArgumentException iae) {
			org.junit.Assert.fail();
		}
	}


	private void testAssertEquals(Object o1, Object o2) {
		try {
			Assert.assertEquals(o1, o2, "error");
		} catch (IllegalArgumentException iae) {
			org.junit.Assert.assertEquals(iae.getMessage(), "error");
		}
	}


	public void testAssertValidIdx() {
		try {
			Assert.assertValidIdx(new LinkedList<String>(), -1);
		} catch(IndexOutOfBoundsException iae) { }

		try {
			Assert.assertValidIdx(new LinkedList<String>(), 1);
		} catch(IndexOutOfBoundsException iae) { }

		try {
			List<String> values = new LinkedList<String>();
			values.add("dummy");
			Assert.assertValidIdx(values, 0);
		} catch(IndexOutOfBoundsException iae) {
			org.junit.Assert.fail();
		}
	}

}
