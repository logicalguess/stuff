/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package com.github.rickardoberg.cqrs.event;

import java.util.Date;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.domain.InteractionContext;

public interface EventStore
{
    void add(InteractionContext interactionContext);

    Function<Identifier, Stream<InteractionContext>> getInteractionsById();
    Function<String, Stream<InteractionContext>> getInteractionsByType();
    Function<Date, Stream<InteractionContext>> getInteractionsByTimestamp();
}
