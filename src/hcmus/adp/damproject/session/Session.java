package hcmus.adp.damproject.session;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import hcmus.adp.damproject.annotations.PrimaryField;
import hcmus.adp.damproject.annotations.Row;
import hcmus.adp.damproject.entity.Entity;
import hcmus.adp.damproject.entity.EntityField;
import hcmus.adp.damproject.statement.SelectStatement;

public class Session {
    private Connection _conn;

    // target class; used for getting table name and columns
    private Class<?> _dataClass;

    // table name
    private String _tableName;

    // get all columns in dataClass
    private ArrayList<Field> _dataClassColumns;

    // same as dataClassColumns, but converted to EntityField format
    private ArrayList<EntityField> _entityFields;

    public Session(Connection conn, Class<?> dataclass) {
        this._conn = conn;
        this._dataClass = dataclass;
        this._dataClassColumns = new ArrayList<>();
        this._entityFields = new ArrayList<>();

        // Get table name
        this.autoSetTableName();

        // Get fields
        for (var each : this._dataClass.getDeclaredFields()) {
            // Make the field accessible
            each.setAccessible(true);

            // Push to _dataClassColumns
            this._dataClassColumns.add(each);

            // Push to _entityFields
            this._entityFields.add(
                    new EntityField(each.getName(), each.isAnnotationPresent(PrimaryField.class), null));
        }
    }

    private void autoSetTableName() {
        String tableName = _dataClass.getAnnotation(Row.class).tableName().toLowerCase();
        if (tableName.equals("")) {
            tableName = _dataClass.getSimpleName().toLowerCase();
        }
        this._tableName = tableName;
    }

    // Return an array of Entity based on a SelectStatement
    public ArrayList<Entity> select(SelectStatement stmt) {
        try {
            // Create result list
            ArrayList<Entity> entities = new ArrayList<>();

            // Check if target table of the given SelectStatement
            // matches what Session expects
            if (stmt.getDataClass().getName().equals(this._dataClass.getName())) {

                // Execute query
                Statement _statement = this._conn.createStatement();
                ResultSet rs = _statement.executeQuery(stmt.toString());

                // For each row...
                while (rs.next()) {

                    // Deep-copy _entityFields to prepare it for pushing to a new Entity()
                    ArrayList<EntityField> entityFields = new ArrayList<>();
                    for (var each : _entityFields) {
                        entityFields.add(each.clone());
                    }

                    // For each column of that row...
                    for (var col : this._dataClassColumns) {

                        // Find matching EntityField (both object and index)
                        EntityField efTarget = null;
                        for (int i = 0; i < entityFields.size(); i++) {
                            if (entityFields.get(i).name.equals(col.getName())) {
                                efTarget = entityFields.get(i);
                                break;
                            }
                        }

                        // Set the value of that EntityField
                        try {
                            // Check the type of the field
                            switch (col.getType().getName()) {
                                // "It's a boolean": get boolean from result set correctly
                                case "boolean":
                                case "java.lang.Boolean":
                                    efTarget.value = rs.getBoolean(col.getName());
                                    break;

                                // "It's something else": set it as is
                                default:
                                    efTarget.value = rs.getObject(col.getName());
                                    break;
                            }
                        } catch (Exception exc) {
                            throw exc;
                        }
                    }

                    // Create a new Entity, then push it to the entites array
                    entities.add(new Entity(this._conn, this._tableName, entityFields));
                }

                return entities;
            }
        } catch (Exception exc) {
            // Do nothing. It should jump to the empty return below.
        }
        return new ArrayList<Entity>();
    }

    // Return whether the object has been inserted into the table
    public boolean insert(Object newItem) {
        try {
            // Check newItem is of type _dataClass (newItem.getClass())
            if (!newItem.getClass().equals(this._dataClass)) {
                return false;
            }

            // Generate list of fields and string-ified values
            ArrayList<String> _dataClassColumnsFields = new ArrayList<>();
            ArrayList<String> _dataClassColumnsValues = new ArrayList<>();
            for (var col : this._dataClassColumns) {
                // Field name
                _dataClassColumnsFields.add(col.getName());

                // Boolean/String edge
                switch (col.getType().getName()) {
                    case "boolean":
                    case "java.lang.Boolean":
                        _dataClassColumnsValues.add(col.getBoolean(newItem) ? "true" : "false");
                        break;

                    case "java.lang.String":
                        _dataClassColumnsValues.add(
                                String.format("'%s'", col.get(newItem).toString()));
                        break;

                    default:
                        _dataClassColumnsValues.add(col.get(newItem).toString());
                        break;
                }
            }

            String sql = String.format(
                    "INSERT INTO %s (%s) VALUES (%s);",
                    this._tableName,
                    String.join(", ", _dataClassColumnsFields),
                    String.join(", ", _dataClassColumnsValues));

            Statement stmt = this._conn.createStatement();
            int affectedRows = stmt.executeUpdate(sql);
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            // Do nothing. It should jump to the empty return below.
        }
        return false;
    }

    // Escape hatch: execute raw SQL queries directly

    public boolean executeSql(String sql) throws SQLException {
        Statement stmt = this._conn.createStatement();
        return stmt.execute(sql);
    }

    public ResultSet executeSqlQuery(String sql) throws SQLException {
        Statement stmt = this._conn.createStatement();
        return stmt.executeQuery(sql);
    }

    public int executeSqlUpdate(String sql) throws SQLException {
        Statement stmt = this._conn.createStatement();
        return stmt.executeUpdate(sql);
    }

    // return entities with aggregate value;
    public HashMap<Entity, Float> aggregate(SelectStatement stmt) throws SQLException {
        // Create result list
        HashMap<Entity,Float> res = new HashMap<>();

        try {
            // Check if target table of the given SelectStatement
            // matches what Session expects
            if (stmt.getDataClass().getName().equals(this._dataClass.getName())) {

                // Execute query
                Statement _statement = this._conn.createStatement();
                ResultSet rs = _statement.executeQuery(stmt.toString());

                // For each row...
                while (rs.next()) {

                    // Deep-copy _entityFields to prepare it for pushing to a new Entity()
                    ArrayList<EntityField> entityFields = new ArrayList<>();
                    for (var each : _entityFields) {
                        entityFields.add(each.clone());
                    }

                    // For each column of that row...
                    for (var col : this._dataClassColumns) {

                        // Find matching EntityField (both object and index)
                        EntityField efTarget = null;
                        for (int i = 0; i < entityFields.size(); i++) {
                            if (entityFields.get(i).name.equals(col.getName())) {
                                efTarget = entityFields.get(i);
                                break;
                            }
                        }

                        // Set the value of that EntityField
                        try {
                            // Check the type of the field
                            switch (col.getType().getName()) {
                                // "It's a boolean": get boolean from result set correctly
                                case "boolean":
                                case "java.lang.Boolean":
                                    efTarget.value = rs.getBoolean(col.getName());
                                    break;

                                // "It's something else": set it as is
                                default:
                                    efTarget.value = rs.getObject(col.getName());
                                    break;
                            }
                        } catch (Exception exc) {
                            throw exc;
                        }
                    }

                    // Create a new Entity, then push it to the entites array
                    res.put(new Entity(this._conn, this._tableName, entityFields), rs.getFloat(stmt.getAggregateColName()));
                }

            }
        } catch (Exception exc) {
            // Do nothing. It should jump to the empty return below.
        }
        return res;
    }
}