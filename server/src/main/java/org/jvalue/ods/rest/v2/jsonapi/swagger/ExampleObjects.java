/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.jsonapi.swagger;

import io.swagger.v3.oas.annotations.media.Schema;

public class ExampleObjects {

	public class DataAttributesExample {
		@Schema(example = "42")
		public String km;

		@Schema(example = "POVODÍ LABE")
		public String agency;

		@Schema(example = "Orlice")
		public String water;

		@Schema(example = "0370")
		public String gaugeId;

		@Schema(example = "Tyniste")
		public String name;
	}

}
