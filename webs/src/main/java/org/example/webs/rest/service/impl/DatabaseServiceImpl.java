package org.example.webs.rest.service.impl;


import org.example.webs.database.Database;
import org.example.webs.database.DatabaseReader;
import org.example.webs.rest.exceptions.ApiException;
import org.example.webs.rest.exceptions.ErrorCode;
import org.example.webs.rest.service.DatabaseService;
import org.springframework.stereotype.Service;

@Service
public class DatabaseServiceImpl implements DatabaseService {
    @Override
    public Database getDatabase() {
        var database = DatabaseReader.getDatabase();
        if (database == null) {
            throw new ApiException(ErrorCode.NO_ACTIVE_DATABASE);
        }
        return database;
    }
}
