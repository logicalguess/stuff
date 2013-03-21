package com.github.rickardoberg.stuff.restlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.github.rickardoberg.cqrs.domain.Repository;
import com.github.rickardoberg.cqrs.domain.memory.InMemoryRepository;
import com.github.rickardoberg.cqrs.event.InteractionContextSink;
import com.github.rickardoberg.stuff.repository.TaskFactory;
import com.github.rickardoberg.stuff.restlet.inbox.InboxResource;
import com.github.rickardoberg.stuff.restlet.inbox.TaskResource;
import com.github.rickardoberg.stuff.view.InboxModel;
import com.github.rickardoberg.stuff.view.LoggingModel;
import com.github.rickardoberg.stuff.view.Models;
import org.apache.velocity.app.VelocityEngine;
import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Reference;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.routing.Template;

public class StuffApplication
    extends Application
{
    public StuffApplication()
    {
    }

    @Override
    public synchronized void start() throws Exception
    {
        Router router = new Router(getContext());


        Properties props = new Properties();
        props.load( getClass().getResourceAsStream( "/velocity.properties" ) );

        VelocityEngine velocity = new VelocityEngine( props );

        Repository repository = new InMemoryRepository();

        List<InteractionContextSink> modelList = new ArrayList<>(  );
        InboxModel inboxModel = new InboxModel();
        modelList.add( inboxModel );

        ObjectMapper mapper = new ObjectMapper();
        modelList.add( new LoggingModel(mapper) );
        Models models = new Models(modelList);

        InboxResource inboxResource = new InboxResource( velocity, new TaskFactory(), repository,
                                                         new TaskResource( velocity, repository, inboxModel, models ), inboxModel, models );
        router.attach( "inbox/{task}/{command}", inboxResource );
        router.attach( "inbox/{task}/", inboxResource );
        router.attach( "inbox/{command}", inboxResource );
        router.attach( "inbox/", inboxResource );

        Reference staticContent = new Reference( new File(getClass().getResource( "/htdocs/index.html" ).getFile()).getParentFile().toURI() );
        router.attachDefault( new Directory( getContext(), staticContent ) ).setMatchingMode( Template.MODE_STARTS_WITH );

        setInboundRoot( router );


        super.start();
    }

    @Override
    public void handle( Request request, Response response )
    {
        super.handle( request, response );
    }
}
