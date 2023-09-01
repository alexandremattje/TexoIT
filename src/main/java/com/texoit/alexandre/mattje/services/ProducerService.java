package com.texoit.alexandre.mattje.services;

import com.texoit.alexandre.mattje.model.Producer;
import com.texoit.alexandre.mattje.repositories.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private final ProducerRepository producerRepository;

    @Autowired
    public ProducerService(ProducerRepository producerRepository){
        this.producerRepository = producerRepository;
    }
    public Producer findOrCreateProducer(String producerName) {
        Producer producer = this.producerRepository.findByName(producerName.trim());
        if (producer != null) {
            return producer;
        }
        return this.producerRepository.save(Producer.builder().name(producerName.trim()).build());
    }

}
