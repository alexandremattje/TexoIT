package com.texoit.alexandre.mattje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupEventListener {
     
    private final StartupData startupData;
     
    @Autowired
    public StartupEventListener(StartupData startupData) {
        this.startupData = startupData;
    }      
     
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("Startei");
        startupData.importFile("movielist.csv");
    }
}