/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lifegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ales
 */
public class OccuranceFinder {
    Random randomGenerator = new Random();
    private List<Occurance> occurances;

    public OccuranceFinder() {
        occurances = new ArrayList<>();
    }

    public void addOccurance(Occurance occurance) {
        if (occurance != null) {
            if(!this.isOccuranceInOccurances(occurance)){
                this.occurances.add(occurance);
            }
        }
    }

    private boolean isOccuranceInOccurances(Occurance newOccurance) {
        for (Occurance occurance : this.occurances) {
            if (occurance.equals(newOccurance)) {
                occurance.incrementCountOfTotalOccurances();
                return true;
            }
        }
        return false;
    }
    
    public Occurance getStrongestOccurance(){
        List<Occurance> temp = new ArrayList<>();
        this.occurances.forEach((occurance) -> {
            if(temp.isEmpty()){
                temp.add(occurance);
            }else{
                if(occurance.getCountOfTotalOccurances() > temp.get(0).getCountOfTotalOccurances()){
                    temp.clear();
                    temp.add(occurance);
                }
                if(occurance.getCountOfTotalOccurances() == temp.get(0).getCountOfTotalOccurances()){
                    temp.add(occurance);
                }
            }
        });
        if(temp.isEmpty()){
            return null;
        }
        if(temp.size() == 1){
            return temp.get(0);
        }else{
            int id = randomGenerator.nextInt(temp.size());
            return temp.get(id);
        }
    }
    

}
