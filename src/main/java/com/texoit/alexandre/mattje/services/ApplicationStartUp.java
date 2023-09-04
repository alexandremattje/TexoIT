package com.texoit.alexandre.mattje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartUp implements ApplicationRunner {

    private final String IMPORT_FILE_ARG = "importFile";
    private final StartupDataService startupData;
     
    @Autowired
    public ApplicationStartUp(StartupDataService startupData) {
        this.startupData = startupData;
    }      

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.containsOption(IMPORT_FILE_ARG)) {
            startupData.importFile(args.getOptionValues(IMPORT_FILE_ARG).get(0));
        } else {
            startupData.importFile("movielist.csv");
        }
    }
}