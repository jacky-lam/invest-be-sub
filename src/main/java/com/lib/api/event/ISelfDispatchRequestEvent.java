package com.lib.api.event;

import com.lib.api.eventhandler.AbstractEventHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * Request where the event is managed within itself
 *
 */
public interface ISelfDispatchRequestEvent<T extends AbstractEventHandler> {
    ResponseEntity<AbstractResponseEvent> processEvent(T eventHandler) throws ResponseStatusException;

    String toString(); //must implement toString
}
