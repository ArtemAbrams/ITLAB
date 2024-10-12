package org.example.webs.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class Element implements Serializable {

    private String value;
    private String column;

    Element(String value, String column) {
        this.value = value;
        this.column = column;
    }

    @JsonIgnore
    public Integer getAsInteger() {
        formatValue();
        return parseInt(value);
    }

    @JsonIgnore
    public Float getAsFloat() {
        formatValue();
        return Float.parseFloat(value);
    }

    @JsonIgnore
    public char getAsCharacter() throws Exception {
        formatValue();
        if (value.length() != 1) throw new Exception("Invalid character value");
        return value.charAt(0);
    }

    @JsonIgnore
    public String getAsString() {
        formatValue();
        return value;
    }

    @JsonIgnore
    public Date getAsDate() throws Exception {
        formatValue();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(value);
        } catch (Exception e) {
            throw new Exception("Invalid date");
        }
    }

    @JsonIgnore
    public List<Date> getAsDateInv() throws Exception {
        formatValue();

        List<Date> dateList = new ArrayList<>();
        String[] dateValues = value.split(" ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (String dateStr : dateValues) {
            try {
                dateList.add(sdf.parse(dateStr.trim()));
            } catch (Exception e) {
                throw new Exception("Invalid date interval value");
            }
        }

        // 2004-02-18 2004-02-29
        if (!dateList.get(0).before(dateList.get(1))) {
            throw new Exception("Invalid date interval value");
        }

        return dateList;
    }


    public String getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    void validate(Table table) throws Exception {
        Column column = table.getColumn(this.column);

        if (value == null) {
            if (column.isNullAllowed()) return;
            throw new Exception("Null value is not allowed");
        }

        try {
            switch (column.getType()) {
                case INT:
                    getAsInteger();
                    break;
                case FLOAT:
                    getAsFloat();
                    break;
                case CHAR:
                    getAsCharacter();
                    break;
                case STR:
                    getAsString();
                    break;
                case DATE:
                    getAsDate();
                    break;
                case DATE_INV:
                    getAsDateInv();
                    break;
            }
        } catch (Exception e) {
            throw new Exception(String.format("Invalid element value '%s': %s", value, e.getMessage()));
        }
    }

    public boolean equals(String other) {
        if (other == null) return value == null;
        if (value == null) return other.equals("null");
        return value.equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return Objects.equals(value, element.value) &&
                Objects.equals(column, element.column);
    }

    private void formatValue() {
        if (value != null) {
            this.value = value.replace("'", "");
        }
    }
}
