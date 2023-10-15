package com.example.transactionswithspring.service;

import com.example.transactionswithspring.entity.Log;
import com.example.transactionswithspring.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LogService {

    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String message) {
        logRepository.save(new Log(message));
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addSeparateLogsNotSupported() {
        logRepository.save(new Log("check from not supported 1"));
        if (true) throw new RuntimeException();
        logRepository.save(new Log("check from not supported 2"));
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void addSeparateLogsSupports() {
        logRepository.save(new Log("check from supports 1"));
        if (true) throw new RuntimeException();
        logRepository.save(new Log("check from supports 2"));
    }


    @Transactional(propagation = Propagation.NEVER)
    public void showLogs() {
        System.out.println("Current log:");
        logRepository.findAll().forEach(System.out::println);
    }


    public void save(Log log) {
        logRepository.save(log);
    }

    public List<Log> findAll() {
        return logRepository.findAll();
    }
}
