package com.texoit.alexandre.mattje.services;

import com.texoit.alexandre.mattje.model.Studio;
import com.texoit.alexandre.mattje.repositories.StudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudioService {

    private final StudioRepository studioRepository;

    @Autowired
    public StudioService (StudioRepository studioRepository){
        this.studioRepository = studioRepository;
    }
    public Studio findOrCreateStudio(String studioName) {
        Studio studio = this.studioRepository.findByName(studioName.trim());
        if (studio != null) {
            return studio;
        }
        return this.studioRepository.save(Studio.builder().name(studioName.trim()).build());
    }

}
