package com.interwater.kotlin.demo.model


data class ResponseResult(
        var code: Int,
        var msg: String?,
        var data: Any?) {
    constructor() : this(200, null, 111)
    constructor(data: Any) : this(200, null, data)
    constructor(errorCode: Int, msg: String) : this(errorCode, msg, null)
    constructor(errorCode: Int) : this(errorCode, null, null)

}