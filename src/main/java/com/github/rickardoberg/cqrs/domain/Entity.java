package com.github.rickardoberg.cqrs.domain;

import java.util.ArrayList;
import java.util.List;

import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.cqrs.event.Interaction;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.cqrs.event.InteractionContextSink;
import com.github.rickardoberg.cqrs.event.InteractionSource;

public class Entity
    implements InteractionSource, InteractionContextSink
{
    private Identifier identifier;
    private List<Event> events = new ArrayList<>(  );

    public Entity( Identifier identifier )
    {
        this.identifier = identifier;
    }

    public Identifier getIdentifier()
    {
        return identifier;
    }

    @Override
    public Interaction getInteraction()
    {
        try
        {
            return new Interaction( identifier, events );
        }
        finally
        {
            events = new ArrayList<>(  );
        }
    }

    @Override
    public void apply( InteractionContext interactionContext )
    {
        for ( Event event : interactionContext.getInteraction().getEvents() )
        {
            apply( event );
        }
    }

    protected void add(Event event)
    {
        events.add( event );
        apply( event );
    }

    protected void apply(Event event)
    {

    }
}
