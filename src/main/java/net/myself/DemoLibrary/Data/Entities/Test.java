package net.myself.DemoLibrary.Data.Entities;

import java.io.Serializable;

public class Test implements Serializable
{
    private final String test;
    private final Long id;

    public Test(String test, Long id)
    {
        this.test = test;
        this.id = id;
    }
}
