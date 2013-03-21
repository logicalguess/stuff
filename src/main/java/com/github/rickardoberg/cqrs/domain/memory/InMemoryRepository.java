package com.github.rickardoberg.cqrs.domain.memory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Block;
import java.util.function.Function;

import com.github.rickardoberg.cqrs.domain.Entity;
import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.Interaction;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.cqrs.domain.Repository;

public class InMemoryRepository
    implements Repository
{
    private Map<String, Map<Identifier, RepositoryEntity<?>>> entities = new HashMap<>(  );

    @Override
    public synchronized <T extends Entity> Function<String, Function<Identifier, RepositoryEntity<T>>> findById()
    {
        return clazz -> id -> (RepositoryEntity<T>) entities.get( clazz ).get( id );
    }

    @Override
    public synchronized <T extends Entity> Function<String, Function<T, InteractionContext>> create( )
    {
        return type -> entity ->
                {
                    Map<Identifier, RepositoryEntity<?>> entityType = entities.get( type );
                    if (entityType == null)
                    {
                        entityType = new HashMap<>(  );
                        entities.put( type, entityType );
                    }

                    entityType.put( entity.getIdentifier(), new RepositoryEntity<T>(0, entity) );

                    return new InteractionContext( type, -1, new Date(), new HashMap<>(  ), entity.getInteraction());
                };
    }

    @Override
    public <T extends Entity> Function<String, Function<Identifier, Function<Block<T>, InteractionContext>>> update()
            throws IllegalArgumentException, IllegalStateException
    {
        return type -> id -> block ->
                {
                    Map<Identifier, RepositoryEntity<?>> entityType = entities.<Map<Identifier, RepositoryEntity>>get( type );
                    if (entityType == null)
                    {
                        throw new IllegalArgumentException( "Type not found:"+type );
                    }

                    RepositoryEntity<T> repositoryEntity = (RepositoryEntity<T>) entityType.get( id );

                    if (repositoryEntity == null)
                    {
                        throw new IllegalArgumentException( "Entity not found:"+ id );
                    }

                    // Write lock it
                    synchronized ( repositoryEntity )
                    {
                        block.accept( repositoryEntity.getEntity() );

                        Interaction interaction = repositoryEntity.getEntity().getInteraction();

                        long newVersion = repositoryEntity.getVersion() + 1;
                        InteractionContext interactionContext = new InteractionContext( type, newVersion, new Date(), new HashMap<>(  ), interaction);

                        entityType.put( id, new RepositoryEntity<>(newVersion, repositoryEntity.getEntity()) );
                        return interactionContext;
                    }
                };
    }
}
