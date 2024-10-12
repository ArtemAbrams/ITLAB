package org.example.webs.rest.api;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.webs.database.Database;
import org.example.webs.database.DatabaseReader;
import org.example.webs.rest.exceptions.ApiException;
import org.example.webs.rest.exceptions.ErrorCode;
import org.example.webs.rest.service.DatabaseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestDatabaseController {
    public static final String DOWNLOAD_DEFAULT_FILENAME = "DB.json";
    private Database database;
    private final DatabaseService databaseService;

    @PostMapping("/database/create")
    @Operation(description = "Method for initial create DB upload")
    public ResponseEntity<Void> createDatabase(@RequestParam String path) {
        if (path == null) {
            throw new ApiException(ErrorCode.PATH_NOT_FOUND);
        }

        if (!path.endsWith(".json")) {
            path += ".json";
        }

        File newDatabaseFile = new File(path);

        if (!newDatabaseFile.exists()) {
            try {
                newDatabaseFile.createNewFile();
                // Ініціалізація нової бази даних та запис її в файл.
                this.database = new Database(path);
                this.database.save();
                this.database = new DatabaseReader(path).read();
                return ResponseEntity.ok().build();
            } catch (IOException e) {
                throw new ApiException(ErrorCode.FILE_CREATION_FAILED);
            }
        } else {
            throw new ApiException(ErrorCode.FILE_ALREADY_EXISTS);
        }
    }

    @PostMapping("/database/upload")
    @Operation(description = "Method for initial existing DB upload")
    public ResponseEntity<Collection<String>> uploadDatabase(@RequestParam String path) {
        try {
            database = new DatabaseReader(path).read();
        } catch (Exception e) {
            throw new ApiException(ErrorCode.FILE_NOT_FOUND);
        }
        return ResponseEntity.ok(database.getTableNames());
    }

    @PostMapping("/database/download")
    @Operation(description = "Method for downloading database state")
    public ResponseEntity<byte[]> downloadDatabase() throws ApiException {
        final var serviceDatabase = databaseService.getDatabase();
        if (serviceDatabase == null) {
            throw new ApiException(ErrorCode.NO_ACTIVE_DATABASE);
        }
        return ResponseEntity.ok()
                .header("content-disposition", "attachment; filename=" + DOWNLOAD_DEFAULT_FILENAME)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(serviceDatabase.download());
    }
}
