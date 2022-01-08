package hcmus.adp.damproject.entity;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import hcmus.adp.damproject.enums.OP_RELATION;
import hcmus.adp.damproject.statement.CondStmt;

public class Entity {
    private Connection conn;
    private String tableName;
    private ArrayList<EntityField> values;
    private boolean _isDeleted;

    public Entity(Connection conn, String tableName, ArrayList<EntityField> values) {
        this.conn = conn;
        this.tableName = tableName;
        this.values = values;
        this._isDeleted = false;
    }

    public Object get(String fieldName) {
        for (var each: values) {
            if (each.name.equals(fieldName)) {
                return each.value;
            }
        }
        return null;
    }
    
    // Generate the WHERE clause used in UPDATE and DELETE
    // (for identifying the exact row in table)
    private String getWhereClause() {
        // Get all primary fields,
        // create a corresponding CondStmt object,
        // then turn it into a string
        ArrayList<String> primaryFields = new ArrayList<>();
        for (var each: this.values) {
            if (each.isPrimary) {
                primaryFields.add(new CondStmt(each.name, OP_RELATION.EQ, each.value).toString());
            }
        }

        // Join all those "condition" strings together with AND
        String finalString = String.join(
            " AND ",
            primaryFields
        );

        // Return the final string
        return finalString;
    }

    public boolean set(String fieldName, Object value) {
        // Get current value
        Object currentValue = this.get(fieldName);

        // Quit when: Field does not exist
        if (currentValue == null) {
            return false;
        }

        // Quit when: New value is of incompatiable type
        if (!value.getClass().getName().equals(currentValue.getClass().getName())) {
            return false;
        }

        // Generate SQL query
        String sql = String.format(
            "UPDATE %s SET %s WHERE %s",
            this.tableName,
            new CondStmt(fieldName, OP_RELATION.EQ, value),
            this.getWhereClause()
        );

        // Execute this query
        try {
            Statement stmt = this.conn.createStatement();
            int affectedRows = stmt.executeUpdate(sql);
            if (affectedRows == 1) {
                // Updated successfully; Proceed to update value of affected field
                int targetIndex = -1;
                EntityField targetField = null;
                for (int i = 0; i < this.values.size(); i++) {
                    if (this.values.get(i).name.equals(fieldName)) {
                        targetIndex = i;
                        targetField = this.values.get(i);
                    }
                }
                targetField.value = value;
                this.values.set(targetIndex, targetField);
                return true;
            } else {
                return false;
            }
        } catch (Exception exc) {
            return false;
        }
    }

    public boolean delete() { 
        try {
            String sql = String.format(
                "DELETE FROM %s WHERE %s",
                this.tableName,
                this.getWhereClause()
            );
            Statement stmt = this.conn.createStatement();
            int affectedRows = stmt.executeUpdate(sql);
            if (affectedRows == 1) {
                this._isDeleted = true;
                return true;
            } else {
                return false;
            }
        } catch (Exception exc) {
            return false;
        }        
    }

    public boolean isDeleted() {
        return this._isDeleted;
    }
}
