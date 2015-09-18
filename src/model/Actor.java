/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author alex
 */
public class Actor implements ActorStrategy {
    
    private String firstName;
    private String lastName;
    private int id;
    
    public Actor(){}
    
    public Actor(String firstName, String lastName){
        setFirstName(firstName);
        setLastName(lastName);
    }
    
    public Actor(String firstName, String lastName, int id){
        setFirstName(firstName);
        setLastName(lastName);
        setId(id);
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public final void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public final void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString(){
        return "First Name: " + firstName + 
                "\nLast Name: " + lastName + 
                "\nActor ID: " + id;
    }
    
}
