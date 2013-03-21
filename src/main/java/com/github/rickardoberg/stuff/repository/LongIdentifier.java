package com.github.rickardoberg.stuff.repository;

import com.github.rickardoberg.cqrs.domain.Identifier;

public class LongIdentifier
    implements Identifier
{
    private long id;

    public LongIdentifier( long id )
    {
        this.id = id;
    }

    public long getId()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return Long.toString( id );
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        LongIdentifier that = (LongIdentifier) o;

        if ( id != that.id )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return (int) (id ^ (id >>> 32));
    }
}
