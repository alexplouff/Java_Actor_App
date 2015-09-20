/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lowLevel;

import lowLevel.Actor_DAO_Strategy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Actor;
import model.ActorStrategy;

/**
 *
 * @author alex
 */
public class Actor_SQL_DAO implements Actor_DAO_Strategy {

    private SQL_Accessor sql_Accessor;
    private final static String TABLE = "actorDB.actor_table";
    private final static String PRIMARY_KEY_COLUMN = "id";
    private final static String FIRST_NAME_COLUMN = "first_name";
    private final static String LAST_NAME_COLUMN = "last_name";
    private final static String NULL_REPLACEMENT_VALUE = "Not Entered";
    private static List COLUMNS;
    private static List actorValues;

    public Actor_SQL_DAO(SQL_Accessor accessor) {
        setSql_Accessor(accessor);
        COLUMNS = new ArrayList<>();
        COLUMNS.add(PRIMARY_KEY_COLUMN);
        COLUMNS.add(FIRST_NAME_COLUMN);
        COLUMNS.add(LAST_NAME_COLUMN);
        actorValues = new ArrayList();
    }

    public SQL_Accessor getSql_accessor() {
        return sql_Accessor;
    }

    public final void setSql_Accessor(SQL_Accessor sql_Accessor) {
        this.sql_Accessor = sql_Accessor;
    }

    @Override
    public List<ActorStrategy> getAllActors() throws SQLException, ClassNotFoundException {
        List<ActorStrategy> actorList = new ArrayList<>();
        List<Map<String, Object>> rawData = sql_Accessor.getRecords(TABLE);

        for (Map<String, Object> values : rawData) {
            Object o = values.get("id");
            int id = (o == null) ? 0 : Integer.valueOf(o.toString());
            o = values.get("first_name");
            String firstName = (o == null) ? NULL_REPLACEMENT_VALUE : o.toString();
            o = values.get("last_name");
            String lastName = (o == null) ? NULL_REPLACEMENT_VALUE : o.toString();

            ActorStrategy actor = new Actor(firstName, lastName, id);
            actorList.add(actor);

        }
        return actorList;
    }

    @Override
    public ActorStrategy getRecordByID(String id) throws SQLException, ClassNotFoundException {
        ActorStrategy actor = null;

        if (id != null && id.matches("\\d+")) {
            List<Map<String, Object>> recordValues;

            String whereClause = " WHERE " + PRIMARY_KEY_COLUMN + " = " + id;
            recordValues
                    = new ArrayList(
                            sql_Accessor.getRecords(TABLE.concat(whereClause)));

            for (Map<String, Object> value : recordValues) {
                Object o = value.get("id");
                int actorID = (o == null) ? 0 : Integer.valueOf(o.toString());
                o = value.get(FIRST_NAME_COLUMN);
                String firstName = (o == null) ? NULL_REPLACEMENT_VALUE : o.toString();
                o = value.get(LAST_NAME_COLUMN);
                String lastName = (o == null) ? NULL_REPLACEMENT_VALUE : o.toString();

                actor = new Actor(firstName, lastName, actorID);
            }
        } else {
            throw new IllegalArgumentException("Bad Input, Numbers Only");
        }

        return actor;
    }

    @Override
    public void createNewActor(ActorStrategy actor) throws SQLException, ClassNotFoundException {

        if (actor != null) {
            actorValues.add(actor.getFirstName());
            actorValues.add(actor.getLastName());
            COLUMNS.remove(PRIMARY_KEY_COLUMN);
            sql_Accessor.createRecord(TABLE, COLUMNS, actorValues);
        } else {
            throw new IllegalArgumentException("Actor could not be created.");
        }
    }

    @Override
    public void deleteRecordsFromDatabase(List primaryKeys) throws SQLException, ClassNotFoundException {
        //List of ActorStrategy Objects instead????

        if (primaryKeys.size() > 0) {

            try {
                sql_Accessor.deleteRecords(TABLE, PRIMARY_KEY_COLUMN, primaryKeys);
            } catch (SQLException ex) {
                System.out.println(ex.getLocalizedMessage());
            }

        } else {
            throw new IllegalArgumentException("Possible Bad Value in P.K List");
        }
    }

    @Override
    public void updateRecord(ActorStrategy actor) throws SQLException, ClassNotFoundException {

        if(actor != null){
        actorValues.add(actor.getId());
        actorValues.add(actor.getFirstName());
        actorValues.add(actor.getLastName());

        sql_Accessor.updateRecord(TABLE, COLUMNS, actorValues, PRIMARY_KEY_COLUMN, actor.getId());
        } else {
            throw new IllegalArgumentException("Update Failed");
        }
    }
}
