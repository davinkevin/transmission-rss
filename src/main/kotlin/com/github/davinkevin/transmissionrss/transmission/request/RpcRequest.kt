package com.github.davinkevin.transmissionrss.transmission.request

data class RpcRequest<T> (
        val method: String,
        val arguments: T,
        val tag: String
)