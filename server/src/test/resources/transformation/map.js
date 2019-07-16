/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
function transform(doc) {
	if(doc != null) {
		Object.keys(doc.water).map(
			function(key, index) {
				doc.water[key] = "RHEIN"
			});
	}
	return doc;
}
