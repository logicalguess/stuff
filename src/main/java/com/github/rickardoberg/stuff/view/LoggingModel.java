package com.github.rickardoberg.stuff.view;

import java.io.IOException;
import java.io.StringWriter;

import com.github.rickardoberg.cqrs.domain.InteractionContext;
import com.github.rickardoberg.cqrs.event.InteractionContextSink;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

public class LoggingModel
    implements InteractionContextSink
{
    ObjectMapper mapper;
    MappingJsonFactory jsonFactory = new MappingJsonFactory();

    public LoggingModel(ObjectMapper mapper)
    {
        this.mapper = mapper;
    }


    @Override
    public void apply( InteractionContext interactionContext )
    {
        try
        {
            StringWriter sw = new StringWriter();   // serialize
            JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw);
            mapper.writeValue(jsonGenerator, interactionContext);
            sw.close();

            System.out.println(sw.toString());
        }
        catch ( IOException e )
        {
            e.printStackTrace(  );
        }
    }
}
