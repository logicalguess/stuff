package com.github.rickardoberg.stuff.event;

import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.cqrs.domain.Identifier;
import org.codehaus.jackson.annotate.JsonUnwrapped;

public class CreatedTaskEvent extends Event
{
    @JsonUnwrapped
    private Identifier id;

    public CreatedTaskEvent( Identifier id)
    {
        this.id = id;
    }

    public Identifier getId()
    {
        return id;
    }
}
