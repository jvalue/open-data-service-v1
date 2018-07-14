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
