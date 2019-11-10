/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lifegame;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author ales
 */
public class NextStepSolver implements Runnable{
    AutomataCell [][] cellArea;
    AutomataCell [][] tempArea;
    AtomicBoolean isEnd;
    int step;
    int threadsCount;
    String neighberhoodRule;
    
    public NextStepSolver(int step, int threadsCount, AutomataCell [][] cellArea, 
            AutomataCell[][] tempArea, AtomicBoolean isEnd, String neighberhoodRule){
        this.step = step;
        this.threadsCount = threadsCount;
        this.cellArea = cellArea;
        this.tempArea = tempArea;
        this.isEnd = isEnd;
        this.neighberhoodRule = neighberhoodRule;
        
        int a = ((this.cellArea.length / this.threadsCount) * this.step);
        int b = (this.cellArea.length / (this.threadsCount - this.step));
        System.out.println("START: " + a + "  |  END: " + b);
    }
    
    @Override
    public void run(){
        for (int i = ((cellArea.length / threadsCount) * step); i < (cellArea.length / (threadsCount - step)); i++) {
            for (int j = 0; j < cellArea[i].length; j++) {
                int[] tempId = getStrongestNeighborId(i, j);
                if (tempId != null) {
                    tempArea[i][j].setIsGrain(true);
                    tempArea[i][j].setGrainId(tempId);
                } else{
                    if(cellArea[i][j].getIsGrain()){
                        tempArea[i][j].setIsGrain(true);
                        tempArea[i][j].setGrainId(cellArea[i][j].getGrainId());
                    }
                }
            }
        }
        isEnd.set(true);
    }
    
    private int[] getStrongestNeighborId(int x, int y) {
        switch (neighberhoodRule) {
            case "vonNeumann":
                return getNeighborIdByVonNeumann(x, y);
        }
        return null;
    }
    
    private int[] getNeighborIdByVonNeumann(int x, int y) {
        OccuranceFinder occuranceFinder = new OccuranceFinder();

        if ((x - 1) > 0) {
            if (cellArea[x - 1][y].getIsGrain()) {
                occuranceFinder.addOccurance(new Occurance(cellArea[x - 1][y].getGrainId()));
            }
        }
        if (x + 1 < cellArea.length) {
            if (cellArea[x + 1][y].getIsGrain()) {
                occuranceFinder.addOccurance(new Occurance(cellArea[x + 1][y].getGrainId()));
            }
        }
        if (y - 1 > 0) {
            if (cellArea[x][y - 1].getIsGrain()) {
                occuranceFinder.addOccurance(new Occurance(cellArea[x][y - 1].getGrainId()));
            }
        }
        if (y + 1 < cellArea.length) {
            if (cellArea[x][y + 1].getIsGrain()) {
                occuranceFinder.addOccurance(new Occurance(cellArea[x][y + 1].getGrainId()));
            }
        }
        Occurance occurance = occuranceFinder.getStrongestOccurance();

        if (occurance != null) {
            return occurance.getId();
        } else {
            return null;
        }
    }
}
