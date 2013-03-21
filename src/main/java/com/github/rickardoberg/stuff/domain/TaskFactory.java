package com.github.rickardoberg.stuff.domain;

import java.util.function.Supplier;

public class TaskFactory
    implements Supplier<Task>
{
    private long nextId = 0;

    @Override
    public Task get()
    {
        return new Task(new LongIdentifier( nextId++ ));
    }
}
