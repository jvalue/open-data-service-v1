/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
function transformationWrapper(jsonStr){
    var json = JSON.parse(jsonStr);
    var result = transform(json);
    return JSON.stringify(result);
}
