package service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ActorStrategy;
import lowLevel.Actor_DAO_Strategy;
import lowLevel.Actor_SQL_DAO;
import lowLevel.SQL_Accessor;
import model.Actor;
/**
 *
 * @author alex
 */
public class ActorService {
    
    private Actor_DAO_Strategy dao;
    
    public ActorService(Actor_DAO_Strategy dao){
        setDao(dao);
    }

    public Actor_DAO_Strategy getDao() {
        return dao;
    }

    public final void setDao(Actor_DAO_Strategy dao) {
        this.dao = dao;
    }

    public void createActorRecord(ActorStrategy actor) throws SQLException, ClassNotFoundException{
        dao.createNewActor(actor);
    }
    
    public void updateActorRecord(ActorStrategy actor) throws SQLException, ClassNotFoundException{
        dao.updateRecord(actor);
    }
    
    public void deleteActorRecords(List<String> keys) throws SQLException, ClassNotFoundException{
        dao.deleteRecordsFromDatabase(keys);
    }
    
    public List<ActorStrategy> getAllActors() throws SQLException, ClassNotFoundException{
        return dao.getAllActors();
    }
    
    public ActorStrategy getActorByID(String id) throws SQLException, ClassNotFoundException{
        return dao.getRecordByID(id);
    }
    
    
}
