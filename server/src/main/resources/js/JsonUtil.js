var transformationWrapper = function(jsonStr){
    var json = JSON.parse(jsonStr);
    var result = transform(json);
    return JSON.stringify(json);
};