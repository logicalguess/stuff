package com.github.rickardoberg.stuff.restlet.inbox;

import java.io.IOException;
import java.io.Writer;

import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.cqrs.domain.Repository;
import com.github.rickardoberg.stuff.domain.Task;
import com.github.rickardoberg.stuff.repository.LongIdentifier;
import com.github.rickardoberg.stuff.usecase.Inbox;
import com.github.rickardoberg.stuff.view.InboxModel;
import com.github.rickardoberg.stuff.view.Models;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.WriterRepresentation;

public class TaskResource
        extends Restlet
{
    private VelocityEngine velocity;
    private Repository repository;
    private InboxModel model;
    private Models models;

    public TaskResource( VelocityEngine velocity, Repository repository, InboxModel model, Models models )
    {
        this.velocity = velocity;
        this.repository = repository;
        this.model = model;
        this.models = models;
    }

    @Override
    public void handle( Request request, Response response )
    {
        super.handle( request, response );

        try
        {
            final Identifier identifier = new LongIdentifier( Long.parseLong( request.getAttributes().get( "task" ).toString()));

            if ( request.getMethod().isSafe())
            {
                final InboxModel.InboxTask value = model.getTasks().get( identifier );

                if (value == null)
                {
                    response.setStatus( Status.CLIENT_ERROR_NOT_FOUND );
                    return;
                }

                response.setEntity( new WriterRepresentation( MediaType.TEXT_HTML )
                {
                    @Override
                    public void write( Writer writer ) throws IOException
                    {
                        VelocityContext context = new VelocityContext();
                        context.put( "task", value );
                        velocity.getTemplate( "inbox/task/task.html" ).merge( context, writer );
                    }
                } );
            }
            else
            {
                Inbox inbox = (Inbox) request.getAttributes().get( "inbox" );

                InteractionContext interactionContext = repository.<Task>update().apply("task" ).apply( identifier ).apply(
                    task ->
                    {
                        inbox.bind( task );

                        Form form = new Form( request.getEntity() );

                        String command = request.getAttributes().get( "command" ).toString();
                        switch ( command )
                        {
                            case "changedescription":
                            {
                                Inbox.ChangeDescription changeDescription = new Inbox.ChangeDescription();
                                changeDescription.description = form.getFirstValue( "description" );

                                Inbox.changeDescription().
                                        apply( inbox ).
                                        apply( changeDescription );
                                break;
                            }

                            case "done":
                            {
                                Inbox.TaskDone taskDone = new Inbox.TaskDone( );
                                taskDone.done = form.getFirstValue( "done" ) != null;

                                Inbox.done().
                                        apply( inbox ).
                                        apply( taskDone );
                                break;
                            }

                            default:
                            {
                                response.setStatus( Status.CLIENT_ERROR_BAD_REQUEST,
                                        "Unknown command:" + command );
                                return;
                            }
                        }
                    }
                );


                if (interactionContext.getInteraction().getEvents().iterator().hasNext())
                {
                    models.apply( interactionContext );
                }

                response.redirectSeeOther( request.getReferrerRef() );
            }
        }
        catch ( Exception e )
        {
            response.setStatus( Status.SERVER_ERROR_INTERNAL, e );
        }
    }
}
