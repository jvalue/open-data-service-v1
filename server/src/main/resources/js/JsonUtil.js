transformationWrapper(mvrzesmadgnxpxqhwllv);

function transformationWrapper(jsonStr){
    var json = JSON.parse(jsonStr);
    var result = transform(json);
    return JSON.stringify(result);
}
