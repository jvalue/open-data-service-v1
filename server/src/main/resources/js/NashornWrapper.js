var resultSet;
var keySet;

function init(){
	resultSet = [];
	keySet = [];
}

function transformationWrapper(jsonStr, query) {
	init();
	var json = JSON.parse(jsonStr);
	transform(json);
	if(!query){
		emit(new Date().getTime(),json);
	}
	return resultSet;
}

//key must be a sortable value
function emit(key, value){
	keySet.push(key);
	keySet.sort();
	var index = keySet.indexOf(key);
	resultSet.splice( index, 0, JSON.stringify(value));
}


