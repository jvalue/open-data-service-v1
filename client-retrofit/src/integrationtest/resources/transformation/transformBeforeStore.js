function transform(doc) {
	if(doc != null) {
		doc.someId = "uniqueID";
		doc.extension = "This is an extension";
	}
	return doc;
}
