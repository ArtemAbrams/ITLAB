package org.example;

import org.example.gui.database.Column;
import org.example.gui.database.Column.Type;
import org.example.gui.database.Database;
import org.example.gui.database.Result;
import org.example.gui.database.Result.Status;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;


class DatabaseTest {

    @Test
    void testCreateTable() throws Exception {
        Database db = new Database(null);

        Collection<Column> columns = new ArrayList<>();
        columns.add(new Column(Type.INT, "id"));
        columns.add(new Column(Column.Type.STR, "name"));

        db.createTable("test_table", columns);

        Collection<String> tableNames = db.getTableNames();
        assertTrue(tableNames.contains("test_table"));
    }

    @Test
    void testRemoveTable() throws Exception {
        Database db = new Database(null);

        Collection<Column> columns = new ArrayList<>();
        columns.add(new Column(Column.Type.INT, "id"));
        db.createTable("test_table", columns);

        db.removeTable("test_table");
        Collection<String> tableNames = db.getTableNames();
        assertFalse(tableNames.contains("test_table"));
    }

    @Test
    void testInsertRow() throws Exception {
        Database db = new Database(null);

        Collection<Column> columns = new ArrayList<>();
        columns.add(new Column(Column.Type.INT, "id"));
        columns.add(new Column(Column.Type.STR, "name"));
        db.createTable("test_table", columns);

        String query = "INSERT INTO test_table (id, name) VALUES (1, 'John')";
        Result result = db.query(query);

        assertEquals(Result.Status.OK, result.getStatus());
    }

    @Test
    void testSelectRow() throws Exception {
        Database db = new Database(null);

        Collection<Column> columns = new ArrayList<>();
        columns.add(new Column(Column.Type.INT, "id"));
        columns.add(new Column(Column.Type.STR, "name"));
        db.createTable("test_table", columns);

        db.query("INSERT INTO test_table (id, name) VALUES (1, 'John')");
        db.query("INSERT INTO test_table (id, name) VALUES (2, 'Jane')");

        String query = "SELECT id, name FROM test_table WHERE id = 1";
        Result result = db.query(query);

        assertEquals(Status.FAIL, result.getStatus());
    }

    @Test
    void testUpdateRow() throws Exception {
        Database db = new Database(null);

        Collection<Column> columns = new ArrayList<>();
        columns.add(new Column(Column.Type.INT, "id"));
        columns.add(new Column(Column.Type.STR, "name"));
        db.createTable("test_table", columns);

        db.query("INSERT INTO test_table (id, name) VALUES (1, 'John')");

        String query = "UPDATE test_table SET name = 'Johnny' WHERE id = 1";
        Result result = db.query(query);

        assertEquals(Status.FAIL, result.getStatus());
    }

    @Test
    void testDeleteRow() throws Exception {
        Database db = new Database(null);

        Collection<Column> columns = new ArrayList<>();
        columns.add(new Column(Column.Type.INT, "id"));
        columns.add(new Column(Column.Type.STR, "name"));
        db.createTable("test_table", columns);

        db.query("INSERT INTO test_table (id, name) VALUES (1, 'John')");

        String query = "DELETE FROM test_table WHERE id = 1";
        Result result = db.query(query);

        assertEquals(Status.FAIL, result.getStatus());
    }

    @Test
    void testSaveDatabase() throws Exception {
        Database db = new Database("test_db.json");

        Collection<Column> columns = new ArrayList<>();
        columns.add(new Column(Column.Type.INT, "id"));
        columns.add(new Column(Column.Type.STR, "name"));
        db.createTable("test_table", columns);

        db.query("INSERT INTO test_table (id, name) VALUES (1, 'John')");

        db.save();
    }
}
