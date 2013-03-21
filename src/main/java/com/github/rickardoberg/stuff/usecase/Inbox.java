package com.github.rickardoberg.stuff.usecase;

import java.util.function.Function;
import java.util.function.Supplier;

import com.github.rickardoberg.cqrs.event.InteractionSource;
import com.github.rickardoberg.stuff.domain.Task;

public class Inbox
{
    private Supplier<Task> taskFactory;

    private Task selected;

    public Inbox( Supplier<Task> taskFactory )
    {
        this.taskFactory = taskFactory;
    }

    public void bind(Task task)
    {
        this.selected = task;
    }

    public static class NewTask
    {
        public ChangeDescription changeDescription;
    }

    public static Function<Inbox, Function<NewTask, Task>> newTask()
    {
        return inbox -> newTask ->
                {
                    Task task = inbox.taskFactory.get();
                    inbox.bind( task );
                    changeDescription().apply( inbox ).apply( newTask.changeDescription );
                    return task;
                };
    }

    public static class ChangeDescription
    {
        public String description;
    }

    public static Function<Inbox, Function<ChangeDescription, InteractionSource>> changeDescription()
    {
        return inbox -> changeDescription ->
                {
                    inbox.selected.changeDescription(changeDescription.description);

                    return inbox.selected;
                };
    }

    public static class TaskDone
    {
        public boolean done;
    }

    public static Function<Inbox, Function<TaskDone, InteractionSource>> done()
    {
        return inbox -> done ->
                {
                    inbox.selected.setDone( done.done);
                    return inbox.selected;
                };
    }
}
