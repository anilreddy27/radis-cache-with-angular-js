package com.redissession.queue;

public interface MessagePublisher {

    void publish(final String message);
}
