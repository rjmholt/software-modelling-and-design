package com.unimelb.swen30006.mailroom;

/**
 * Parses and sets commandline arguments
 */
public interface CommandLineSettings <T>
{
    void takeArguments(String[] args);
}
