/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
function sum(valueArray) {
	return valueArray.reduce(
		function(previous,element){
			return previous + element;
		}, 0);
}

function avarage(doubleValueArray) {
	return sum(doubleValueArray) / doubleValueArray.length;
}

function transform(doc) {
	if(doc != null) {
		values = doc.timeseries[0].characteristicValues.reduce(
				function(previous, element) {
					previous.push(element.value);
						return previous;
					}, []);
		return {'result' : avarage(values)}
	}
}
