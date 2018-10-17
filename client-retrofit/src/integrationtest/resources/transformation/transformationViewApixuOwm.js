function transform(doc) {
	if (doc != null) {
		var newdoc = {};
		newdoc.ApixuAdapter = {};
		newdoc.ApixuAdapter.data = doc.weatherApixuSourceAdapter;
		newdoc.OpenWeatherMapAdapter = {};
		newdoc.OpenWeatherMapAdapter.data = doc.weatherOpenWeatherMap;
		newdoc.someId = doc.someId;
		newdoc.extension = doc.extension;
		output(newdoc);
	}
}
