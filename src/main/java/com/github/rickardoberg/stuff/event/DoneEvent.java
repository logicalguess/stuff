package com.github.rickardoberg.stuff.event;

import com.github.rickardoberg.cqrs.event.Event;

public class DoneEvent
    extends Event
{
    private boolean done;

    public DoneEvent( boolean done )
    {
        this.done = done;
    }

    public boolean isDone()
    {
        return done;
    }
}
