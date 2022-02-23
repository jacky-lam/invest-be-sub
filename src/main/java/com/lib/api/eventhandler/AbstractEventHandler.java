package com.lib.api.eventhandler;

import com.investment.helper.AuthenticationHelper;
import com.lib.api.event.AbstractResponseEvent;
import com.lib.api.event.ISelfDispatchRequestEvent;
import com.lib.database.wrapper.AbstractDatabaseWrapper;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * Base for all event-handlers (makes its easier for events to access common attributes)
 *
 * @param <T> Database used by this event handler (assumption - event handlers only use one database. it will be complex if event-handle access multiple databases)
 */
public abstract class AbstractEventHandler <T extends AbstractDatabaseWrapper> {

    private final Logger logger;
    private final T dbWrapper;

    @Autowired
    private AuthenticationHelper authenticationHelper;
    public AuthenticationHelper getAuthenticationHelper() {
        return authenticationHelper;
    }

    public AbstractEventHandler(Logger logger, T dbWrapper){
        this.logger = logger;
        this.dbWrapper = dbWrapper;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Terminating database connection - " + logger.getName());
            dbWrapper.terminateAllConnections();
        }, "Shutdown-thread - " + logger.getName()));
    }

    public ResponseEntity<AbstractResponseEvent> processSelfDispatchEvent(ISelfDispatchRequestEvent event) throws ResponseStatusException {
        logger.info("Retrieved self-dispatch event: " + event);

        //todo: spin a new thread to manage this event
        // - so method should return void
        ResponseEntity<AbstractResponseEvent> result = event.processEvent(this);
        logger.info("Returning self-dispatch event: " + result);
        return result;
    }

    public Logger getLogger(){
        return this.logger;
    }

    public T getDbWrapper() {
        return this.dbWrapper;
    };
}
