package com.github.rickardoberg.stuff.event;

import com.github.rickardoberg.cqrs.event.Event;

public class ChangedDescriptionEvent
    extends Event
{
    private String description;

    public ChangedDescriptionEvent( String description )
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}
