package com.github.davinkevin.transmissionrss.transmission.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by kevin on 08/04/2018
 */
@Getter
@RequiredArgsConstructor
abstract class RpcRequest<T> {
    final String method;
    final T arguments;
    final String tag;
}
