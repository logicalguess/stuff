/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package com.github.rickardoberg.cqrs.domain;

import java.util.function.Block;
import java.util.function.Function;

import com.github.rickardoberg.cqrs.event.InteractionContext;

/**
 * Repositories are used to create, update, or delete entities.
 */
public interface Repository
{
    <T extends Entity> Function<String, Function<T, InteractionContext>> create( );

    <T extends Entity> Function<String, Function<Identifier, Function<Block<T>, InteractionContext>>> update()
            throws IllegalArgumentException, IllegalStateException;

    <T extends Entity> Function<String, Function<Identifier, Function<Block<T>, InteractionContext>>> delete();
}
