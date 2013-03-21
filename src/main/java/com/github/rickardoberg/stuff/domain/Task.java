package com.github.rickardoberg.stuff.domain;

import com.github.rickardoberg.cqrs.domain.Entity;
import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.stuff.event.ChangedDescriptionEvent;
import com.github.rickardoberg.stuff.event.CreatedTaskEvent;
import com.github.rickardoberg.stuff.event.DoneEvent;

public class Task
    extends Entity
{
    private boolean done = false;

    public Task( Identifier identifier )
    {
        super( identifier );
        add( new CreatedTaskEvent( identifier ) );
    }

    public void changeDescription( String description )
    {
        add( new ChangedDescriptionEvent( description ) );
    }

    public boolean isDone()
    {
        return done;
    }

    public void setDone(boolean done)
    {
        if (this.done != done)
        {
            this.done = done;
            add( new DoneEvent(done) );
        }
    }

    protected void apply( Event event )
    {
        if (event instanceof DoneEvent)
        {
            done = ((DoneEvent)event).isDone();
        }
    }
}
