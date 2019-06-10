package com.stan.video.bittyvideo.net.exception

/**
 * Created by Stan
 * on 2019/6/10.
 */
class ApiException : RuntimeException {

    private var code: Int? = null


    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))
}