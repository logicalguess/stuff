/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package com.github.rickardoberg.cqrs.event;

public interface InteractionContextSink
{
    void apply( InteractionContext interactionContext );
}
