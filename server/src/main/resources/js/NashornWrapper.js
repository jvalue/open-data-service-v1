var resultSet = [];

function output(value){
	resultSet.push(JSON.stringify(value));
}

function transformationWrapper(jsonStr, query){
	resultSet = [];
	var jsObject = JSON.parse(jsonStr);
	var result = transform(jsObject);
	if(!query){
		if(!result) {
			return null;
		}
		//discard previous output() calls in non-query mode
		resultSet = [];
		output(result);
	}
	return resultSet;
}

function reduceWrapper(jsonStrResultSet) {
	var jsObjectSet = [];
	jsonStrResultSet.forEach(function(item){
		jsObjectSet.push(JSON.parse(item));
	});
	return reduce(jsObjectSet);
}
