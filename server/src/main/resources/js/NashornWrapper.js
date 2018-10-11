var resultSet;
var keySet;

function init(){
	resultSet = [];
	keySet = [];
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

//key must be a sortable value
function output(value){
	resultSet.push(JSON.stringify(value));
}


