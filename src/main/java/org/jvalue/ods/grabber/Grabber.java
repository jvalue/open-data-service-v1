package org.jvalue.ods.grabber;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericValue;

public interface Grabber {
	public GenericValue grab(DataSource dataSource);
}
