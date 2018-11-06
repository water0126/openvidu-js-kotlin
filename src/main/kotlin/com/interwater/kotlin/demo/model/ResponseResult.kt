package com.interwater.kotlin.demo.model

import com.google.gson.Gson


data class ResponseResult(
        var code: Int,
        var msg: String?,
        var data: Any?) {
    constructor() : this(1, null, 111)
    constructor(code: Int, msg: String) : this(code, msg, null)
    constructor(code: Int) : this(code, null, null) {
        val gson = Gson()
        println(gson.toJson(this));
    }

}