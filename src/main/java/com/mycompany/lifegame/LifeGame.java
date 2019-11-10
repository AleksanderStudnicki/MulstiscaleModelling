/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lifegame;

import com.mycompany.lifegame.LifeStructure.LifeStructure;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author ales
 */
public class LifeGame {
    AtomicBoolean [][] lifeArea;
    
    public LifeGame(int width, int height){
        this.lifeArea = new AtomicBoolean[width][height];
        for(AtomicBoolean[] arr : this.lifeArea){
            for(int i = 0; i < arr.length; i++){
                arr[i] = new AtomicBoolean(false);
            }
        }
    }
    
    public void nextStep(){
        AtomicBoolean [][] temp = new AtomicBoolean[lifeArea.length][lifeArea[0].length];
        initializeTemp(temp);
        for(int i = 0; i < lifeArea.length; i++){
            for(int j = 0; j < lifeArea[i].length; j++){
                checkNeighborhood(i, j, temp);
            }
        }
        lifeArea = temp;
    }
    
    private void checkNeighborhood(int x, int y, AtomicBoolean[][] temp){
        int neighbors = 0;
        for(int i = x-1; i <= x+1; i++){
            for(int j = y-1; j <= y+1; j++){
                if(i != x || j != y){
                        if(i >= 0 && j >= 0 && i < lifeArea.length && j < lifeArea[i].length){
                            if(lifeArea[i][j].get()){
                                neighbors++;
                            }
                    }
                }
            }
        }
        if(lifeArea[x][y].get()){
            temp[x][y].set(neighbors >= 2 && neighbors <= 3);
        }else{
            temp[x][y].set(neighbors == 3);
        }
        
    }
    
    public void insertLifeStructure(int x, int y, LifeStructure lifeStructure){
        boolean temp [][] = lifeStructure.getLifeArea();
        for(int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[i].length; j++){
                lifeArea[x + i][y + j].set(temp[i][j]);
            }
        }
    }
    
    public void setChaos(){
        for(int i = 0; i < lifeArea.length; i++){
            for(int j = 0; j < lifeArea[i].length; j++){
                lifeArea[i][j].set(Math.random() < 0.5);
            }
        }

    }
    
    private void initializeTemp(AtomicBoolean[][] temp){
        for(AtomicBoolean[] arr : temp){
            for(int i = 0; i < arr.length; i++){
                arr[i] = new AtomicBoolean(false);
            }
        }
    }
    
    public void reset(){
        for(AtomicBoolean[] arr : lifeArea){
            for (AtomicBoolean arr1 : arr) {
                arr1.set(false);
            }
        }
    }
    
    public void setCellAlive(int x, int y){
        lifeArea[x][y].set(true);
    }
    

}
