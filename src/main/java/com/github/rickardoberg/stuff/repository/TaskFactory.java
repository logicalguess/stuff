package com.github.rickardoberg.stuff.repository;

import java.util.function.Supplier;

import com.github.rickardoberg.stuff.domain.Task;

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
