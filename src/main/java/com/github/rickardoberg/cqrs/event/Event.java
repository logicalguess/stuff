package com.github.rickardoberg.cqrs.event;

import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="type")
public class Event
{
}
