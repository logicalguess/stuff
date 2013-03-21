package com.github.rickardoberg.cqrs.event;

import java.util.Collection;
import java.util.List;

import com.github.rickardoberg.cqrs.domain.Identifier;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonUnwrapped;

public class Interaction
{
    private Identifier identifier;
    private Collection<Event> events;

    public Interaction( Identifier identifier, List<Event> events )
    {
        this.identifier = identifier;
        this.events = events;
    }

    @JsonUnwrapped
    public Identifier getIdentifier()
    {
        return identifier;
    }

    @JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="type")
    public Collection<Event> getEvents()
    {
        return events;
    }
}
