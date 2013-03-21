/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package com.github.rickardoberg.cqrs.domain;

import java.util.function.Block;
import java.util.function.Function;

import com.github.rickardoberg.cqrs.event.InteractionContext;

public interface Repository
{
    public static class RepositoryEntity<T extends Entity>
    {
        private long version;
        private T entity;

        public RepositoryEntity( long version, T entity )
        {
            this.version = version;
            this.entity = entity;
        }

        public long getVersion()
        {
            return version;
        }

        public T getEntity()
        {
            return entity;
        }
    }

    <T extends Entity> void create( T entity );

    <T extends Entity> Function<Class<T>, Function<Identifier, Function<Block<T>, InteractionContext>>> update()
            throws IllegalArgumentException, IllegalStateException;

    <T extends Entity> Function<Class<T>, Function<Identifier, RepositoryEntity<T>>> findById();
}
