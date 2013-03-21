/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package com.github.rickardoberg.cqrs.event;

import com.github.rickardoberg.cqrs.domain.Interaction;

public interface InteractionSource
{
    Interaction getInteraction();
}
