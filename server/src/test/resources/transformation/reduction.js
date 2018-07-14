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
