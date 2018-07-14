function transform(doc) {
	if(doc != null) {
		doc.combinedCoords = doc.longitude + ', ' + doc.latitude;
	}
	return doc;
}
