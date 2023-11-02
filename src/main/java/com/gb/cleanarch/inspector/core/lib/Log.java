package com.gb.cleanarch.inspector.core.lib;

public interface Log {

    boolean isInfoEnabled();

    boolean isDebugEnabled();

    void info(String s);

    void debug(String s);

    void warn(String toString);

    void warn(String s, Throwable e);

    void debug(String s, Throwable e);
}
