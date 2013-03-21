package com.github.rickardoberg.stuff.view;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.Interaction;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.cqrs.event.InteractionContextSink;
import com.github.rickardoberg.stuff.event.ChangedDescriptionEvent;
import com.github.rickardoberg.stuff.event.CreatedTaskEvent;
import com.github.rickardoberg.stuff.event.DoneEvent;

public class InboxModel
    implements InteractionContextSink
{
    Map<Identifier, InboxTask> tasks = new LinkedHashMap<>(  );

    @Override
    public void apply( InteractionContext interactionContext )
    {
        Interaction interaction = interactionContext.getInteraction();

        for ( Event event : interaction.getEvents() )
        {
            if (event instanceof CreatedTaskEvent)
            {
                InboxTask task = new InboxTask()
                {{
                    setCreatedOn( interactionContext.getTimestamp() );
                }};
                tasks.put( interaction.getIdentifier(), task);
            }
            else if (event instanceof ChangedDescriptionEvent)
            {
                ChangedDescriptionEvent changedDescriptionEvent = (ChangedDescriptionEvent) event;
                tasks.put( interaction.getIdentifier(), new InboxTask()
                {{
                        copy( tasks.get( interaction.getIdentifier() ) );
                        setDescription( changedDescriptionEvent.getDescription() );
                    }} );
            }
            else if (event instanceof DoneEvent)
            {
                DoneEvent doneEvent = (DoneEvent) event;
                tasks.put( interaction.getIdentifier(), new InboxTask()
                {{
                        copy( tasks.get( interaction.getIdentifier() ) );
                        setDone( doneEvent.isDone() );
                    }} );
            }
        }
    }

    public Map<Identifier, InboxTask> getTasks()
    {
        return tasks;
    }

    public class InboxTask
    {
        private String description;
        private boolean done;
        private Date createdOn;

        protected void copy(InboxTask task )
        {
            this.description = task.description;
            this.done = task.done;
            this.createdOn = task.createdOn;
        }

        protected void setDescription( String description )
        {
            this.description = description;
        }

        protected void setDone( boolean done )
        {
            this.done = done;
        }

        protected void setCreatedOn( Date createdOn )
        {
            this.createdOn = createdOn;
        }

        public String getDescription()
        {
            return description;
        }

        public boolean isDone()
        {
            return done;
        }

        public Date getCreatedOn()
        {
            return createdOn;
        }
    }
}
