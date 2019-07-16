/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
function transform(doc) {
	var resultDoc = {};
	if(doc != null) {
		resultDoc.stringValues = Object.keys(doc).filter(
			function(key) {
				return typeof doc[key] === 'string';
			});
	}
	return resultDoc;
}
