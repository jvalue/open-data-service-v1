/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
function transform(doc) {
	if(doc != null) {
		return doc.timeseries[0].characteristicValues.reduce(
			function(previous, key) {
				previous.keycount ++;
				return previous;
			}, {keycount: 0});
	}
	return doc;
}
