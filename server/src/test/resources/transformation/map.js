function transform(doc) {
	if(doc != null) {
		Object.keys(doc.water).map(
			function(key, index) {
				doc.water[key] = "RHEIN"
			});
	}
	return doc;
}
