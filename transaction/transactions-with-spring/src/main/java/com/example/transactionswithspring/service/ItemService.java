package com.example.transactionswithspring.service;

import com.example.transactionswithspring.entity.Item;
import com.example.transactionswithspring.entity.Log;
import com.example.transactionswithspring.exception.DuplicateItemNameException;
import com.example.transactionswithspring.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    private final LogService logService;

    public ItemService(ItemRepository itemRepository, LogService logService) {
        this.itemRepository = itemRepository;
        this.logService = logService;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void checkNameDuplicate(String name) {
        if (itemRepository.findAll().stream().map(Item::getName).anyMatch(n -> n.equals(name))) {
            throw new DuplicateItemNameException("Item with name " + name + " already exists");
        }
    }

    @Transactional
    public void addItem(String name, LocalDate creationDate) {
        logService.log("adding item with name " + name);
        checkNameDuplicate(name);
        itemRepository.save(new Item(name, creationDate));
    }

    @Transactional(noRollbackFor = DuplicateItemNameException.class)
    public void addItemNoRollback(String name, LocalDate creationDate) {
        logService.save(new Log("adding log in method with no rollback for item " + name));
        checkNameDuplicate(name);
        itemRepository.save(new Item(name, creationDate));
    }

    @Transactional
    public void addLogs() {
        logService.addSeparateLogsNotSupported();
    }

    @Transactional
    public void showLogs() {
        logService.showLogs();
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}
