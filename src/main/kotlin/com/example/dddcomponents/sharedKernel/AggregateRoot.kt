package com.example.dddcomponents.sharedKernel


abstract class AggregateRoot<T> {
    protected val id: T

    constructor(id: T) {
        this.id = id
    }
}
