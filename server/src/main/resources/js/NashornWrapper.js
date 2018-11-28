var resultSet = [];

function output(value){
	resultSet.push(JSON.stringify(value));
}

function transformationWrapper(jsonStr, query) {
	var jsObject = JSON.parse(jsonStr);
	var result = transform(jsObject);
	if(!query){
		//discard previous output() calls in non-query mode
		resultSet = [];
		if(result) {
			output(result);
		}
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
