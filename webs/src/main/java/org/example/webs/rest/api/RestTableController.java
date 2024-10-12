package org.example.webs.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.webs.database.Result;
import org.example.webs.rest.exceptions.ApiException;
import org.example.webs.rest.service.DatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestTableController {
    private final DatabaseService databaseService;

    @GetMapping(value = "/tables")
    @Operation(description = "Retrieve tables from DB")
    public ResponseEntity<Result> tables() throws ApiException {
        final var database = databaseService.getDatabase();
        return ResponseEntity.ok(database.query("list tables"));
    }

    @DeleteMapping(value = "/tables/delete")
    @Operation(description = "Drop table in DB")
    public ResponseEntity<Result> dropTable(@RequestParam String tableName) throws ApiException {
        final var database = databaseService.getDatabase();
        return ResponseEntity.ok(database.query(String.format("remove table %s", tableName)));
    }

    @PostMapping(value = "/tables/create")
    @Operation(description = "Create table in DB")
    public ResponseEntity<Result> createTable(@RequestParam String columns,
                                              @RequestParam String tableName) throws ApiException {
        final var database = databaseService.getDatabase();
        return ResponseEntity.ok(database.query(String.format("create table %s (%s)", tableName, columns)));
    }


    @PostMapping(value = "/tables/subtract")
    @Operation(description = "Subtract tables in DB")
    public ResponseEntity<Result> subtract(@RequestParam String tableLeftName,
                                           @RequestParam String tableRightName) throws ApiException {
        final var database = databaseService.getDatabase();
        return ResponseEntity.ok(database.query(String.format("subtract %s from %s", tableLeftName, tableRightName)));
    }
}
