package com.ev3.startup;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Singleton
@Startup
public class StartupEventNotifier {


    @Inject
    Event<StartupEvent> event;

    @PostConstruct
    public void init() {
        event.fire(new StartupEvent(){
        });
    }
}
