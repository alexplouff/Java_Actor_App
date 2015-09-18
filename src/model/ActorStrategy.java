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
public interface ActorStrategy {

    public abstract String getFirstName();

    public abstract int getId();

    public abstract String getLastName();

    @Override
    public abstract String toString();
    
}
