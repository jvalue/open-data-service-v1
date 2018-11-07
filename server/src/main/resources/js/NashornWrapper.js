var resultSet;

function init(){
	resultSet = [];
}

function transformationWrapper(jsonStr, query) {
	init();
	var json = JSON.parse(jsonStr);
	var result = transform(json);
	if(!query){
		output(result);
	}
	return resultSet;
}

function output(value){
	resultSet.push(JSON.stringify(value));
}

function reduceWrapper(jsonStrResultSet, size) {
	var jsonArray = [];
	for(var i = 0; i < size; i++){
		jsonArray.push(JSON.parse(jsonStrResultSet[i]));
	}
	return reduce(jsonArray);
}


