package com.example.dddcomponents.sharedKernel;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AggregateRoot<T> {

    protected final T id;

}
