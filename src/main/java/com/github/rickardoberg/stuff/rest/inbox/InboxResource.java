package com.github.rickardoberg.stuff.rest.inbox;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Supplier;

import com.github.rickardoberg.cqrs.domain.Repository;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.stuff.domain.Task;
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

public class InboxResource extends Restlet
{
    private final VelocityEngine velocity;
    private Supplier<Task> taskFactory;
    private Repository repository;
    private TaskResource taskResource;
    private InboxModel model;
    private Models models;

    public InboxResource( VelocityEngine velocity, Supplier<Task> taskFactory, Repository repository, TaskResource
            taskResource, InboxModel model, Models models )
    {
        this.velocity = velocity;
        this.taskFactory = taskFactory;
        this.repository = repository;
        this.taskResource = taskResource;
        this.model = model;
        this.models = models;
    }

    @Override
    public void handle( Request request, Response response )
    {
        super.handle( request, response );

        if (request.getAttributes().containsKey( "task" ))
        {
            if (!request.getMethod().isSafe())
            {
                Inbox inbox = new Inbox(taskFactory);
                request.getAttributes().put( "inbox", inbox );
            }

            taskResource.handle( request, response );
        } else
        {
            if (request.getMethod().isSafe())
            {
                if (request.getAttributes().containsKey( "command" ))
                {
                    response.setEntity( new WriterRepresentation( MediaType.TEXT_HTML )
                    {
                        @Override
                        public void write( Writer writer ) throws IOException
                        {
                            VelocityContext context = new VelocityContext();
                            velocity.getTemplate( "inbox/"+request.getAttributes().get( "command" )+".html" ).merge( context, writer );
                        }
                    } );
                } else
                {
                    response.setEntity( new WriterRepresentation( MediaType.TEXT_HTML )
                    {
                        @Override
                        public void write( Writer writer ) throws IOException
                        {
                            VelocityContext context = new VelocityContext();
                            context.put( "tasks", model.getTasks().entrySet() );
                            velocity.getTemplate( "inbox/inbox.html" ).merge( context, writer );
                        }
                    } );
                }
            } else
            {
                Inbox inbox = new Inbox(taskFactory);

                Form form = new Form(request.getEntity());

                switch (request.getAttributes().get( "command" ).toString())
                {
                    case "newtask":
                    {
                        Inbox.ChangeDescription changeDescription = new Inbox.ChangeDescription(  );
                        changeDescription.description = form.getFirstValue( "description" );
                        Inbox.NewTask newTask = new Inbox.NewTask( );
                        newTask.changeDescription = changeDescription;

                        Task task = Inbox.newTask().
                                apply( inbox ).
                                apply( newTask );

                        InteractionContext interactionContext = repository.create( ).apply( "task" ).apply( task );
                        models.apply( interactionContext );

                        break;
                    }

                    default:
                    {
                        response.setStatus( Status.CLIENT_ERROR_BAD_REQUEST, "Unknown command:"+form.getFirstValue( "command" ) );
                        return;
                    }
                }

                response.redirectSeeOther( request.getOriginalRef().getParentRef() );
            }
        }
    }
}
