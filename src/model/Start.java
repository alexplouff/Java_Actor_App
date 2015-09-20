/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import lowLevel.Actor_SQL_DAO;
import lowLevel.SQL_Accessor;
import service.ActorService;

/**
 *
 * @author alex
 */
public class Start {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        ActorService actor_service = new ActorService(new Actor_SQL_DAO(new SQL_Accessor("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/Client?zeroDateTimeBehavior=convertToNull",
                "root", "root")));

//      actor_service.createActorRecord(new Actor("Gary", "Eubank"));     //C
//      System.out.println(actor_service.getAllActors());                 //R

//      System.out.println(actor_service.getActorByID("3"));        **
//      actor_service.updateActorRecord(new Actor("Guy", "LaForge", 3));  //U
        List deleteList = new ArrayList();
        deleteList.add(8);
        deleteList.add(9);
        actor_service.deleteActorRecords(deleteList);                     //D
    }

}
