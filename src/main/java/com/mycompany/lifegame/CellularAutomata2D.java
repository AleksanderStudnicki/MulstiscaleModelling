/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lifegame;

import com.mycompany.lifegame.CellularAutomataRules.BorderRule;
import com.mycompany.lifegame.CellularAutomataRules.CellularAutomataRule;
import com.mycompany.lifegame.CellularAutomataRules.HexagonalLeftRule;
import com.mycompany.lifegame.CellularAutomataRules.HexagonalRandom;
import com.mycompany.lifegame.CellularAutomataRules.HexagonalRightRule;
import com.mycompany.lifegame.CellularAutomataRules.MooreRule;
import com.mycompany.lifegame.CellularAutomataRules.PentagonalLeftRule;
import com.mycompany.lifegame.CellularAutomataRules.PentagonalRandom;
import com.mycompany.lifegame.CellularAutomataRules.PentagonalRightRule;
import com.mycompany.lifegame.CellularAutomataRules.VonNeumannRule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author ales
 */
public class CellularAutomata2D {

    AutomataCell[][] cellArea;
    volatile String neighberhoodRule = "vonNeumann";
    boolean isPeriodical = false;
    AtomicBoolean isAllCellsGrains = new AtomicBoolean(false);
    
    HexagonalRandom hexRand;
    PentagonalRandom pentRand;

    public CellularAutomata2D(int width, int height) {
        this.cellArea = new AutomataCell[width][height];
        initializeAutomataCellArray(this.cellArea);
    }

    public void nextStep() {
        CellularAutomataRule rule = getRule();
        if (rule != null) {
            AutomataCell[][] temp = new AutomataCell[cellArea.length][cellArea[0].length];
            initializeAutomataCellArray(temp);
            for (int i = 0; i < cellArea.length; i++) {
                for (int j = 0; j < cellArea[i].length; j++) {
                    int[] tempId = getStrongestNeighborId(i, j, rule);
                    if (tempId != null) {
                        temp[i][j].setIsGrain(true);
                        temp[i][j].setGrainId(tempId);
                    } else {
                        if (cellArea[i][j].getIsGrain()) {
                            temp[i][j].setIsGrain(true);
                            temp[i][j].setGrainId(cellArea[i][j].getGrainId());
                        }
                    }
                }
            }
            this.cellArea = temp;
        }
    }

    public void nextStepWithThreads() {
        CellularAutomataRule rule = getRule();
        if (rule != null) {

        }
    }

    private CellularAutomataRule getRule() {
        switch (this.neighberhoodRule) {
            case "vonNeumann":
                return new VonNeumannRule();
            case "Moore":
                return new MooreRule();
            case "HexLeft":
                return new HexagonalLeftRule();
            case "HexRight":
                return new HexagonalRightRule();
            case "PentaLeft":
                return new PentagonalLeftRule();
            case "PentaRight":
                return new PentagonalRightRule();
            case "HexRand":
                return this.hexRand;
            case "PentRand":
                return this.pentRand;
            default:
                return null;
        }
    }

    private void initializeAtomicBooleanArray(AtomicBoolean[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new AtomicBoolean(false);
        }
    }

    public boolean isAllCellsGrains() {
        for (AutomataCell[] arr : this.cellArea) {
            for (AutomataCell cell : arr) {
                if (!cell.getIsGrain()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void initializeAutomataCellArray(AutomataCell[][] arr) {
        for (AutomataCell[] arr1 : arr) {
            for (int i = 0; i < arr1.length; i++) {
                arr1[i] = new AutomataCell();
            }
        }
    }

    public int[] getStrongestNeighborId(int x, int y, CellularAutomataRule rule) {
        OccuranceFinder occuranceFinder = new OccuranceFinder();
        for (int[] arr : rule.getCoordinatesArray()) {
            if (isPeriodical) {
                int newX = (x + arr[0] + cellArea.length) % cellArea.length;
                int newY = (y + arr[1] + cellArea.length) % cellArea.length;
                if (cellArea[newX][newY].getIsGrain()) {
                    occuranceFinder.addOccurance(new Occurance(cellArea[newX][newY].getGrainId()));
                }
            } else {
                if ((x + arr[0]) >= 0
                        && (y + arr[1]) >= 0
                        && (x + arr[0]) < cellArea.length
                        && (y + arr[1]) < cellArea.length) {
                    if (cellArea[x + arr[0]][y + arr[1]].getIsGrain()) {
                        occuranceFinder.addOccurance(new Occurance(cellArea[x + arr[0]][y + arr[1]].getGrainId()));
                    }
                }
            }
        }
        Occurance occurance = occuranceFinder.getStrongestOccurance();

        if (occurance != null) {
            return occurance.getId();
        } else {
            return null;
        }
    }

    public void setChaos() {
        for (AutomataCell[] arr : cellArea) {
            for (int i = 0; i < arr.length; i++) {
                arr[i].setIsGrain(Math.random() < 0.5);
                if (arr[i].getIsGrain()) {
                    arr[i].setRandomId();
                }
            }
        }
    }

    private boolean isAllThreadsEnded(AtomicBoolean[] arr) {
        for (AtomicBoolean b : arr) {
            if (!b.get()) {
                return false;
            }
        }
        return true;
    }

    public void setCellAlive(int x, int y, int[] id) {
        cellArea[x][y].setGrainId(id);
        cellArea[x][y].setIsGrain(true);
    }

    public void reset() {
        for (AutomataCell arr[] : this.cellArea) {
            for (int i = 0; i < arr.length; i++) {
                arr[i].setIsGrain(false);
            }
        }
    }

    public void generateCellArea(int grains, int radius) {
        reset();
        Random randomGenerator = new Random();
        for (int i = 0; i < grains; i++) {
            
            
            boolean isSet = false;
            
            while(!isSet){
            
            int x = randomGenerator.nextInt(cellArea.length);
            int y = randomGenerator.nextInt(cellArea[0].length);
                
            if (isAbleToSetGrain(x, y, radius)) {
                cellArea[x][y].setIsGrain(true);
                cellArea[x][y].setRandomId();
                isSet = true;
            }
            
            }
        }
    }

    public boolean isAbleToSetGrain(int x0, int y0, int radius) {
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                int x = x0 + i;
                int y = y0 + j;
                if (isPeriodical) {
                    if (inCircle(x, y, x0, y0, radius)) {
                        if (cellArea[(x + cellArea.length) % cellArea.length][(y + cellArea[0].length) % cellArea[0].length].getIsGrain()) {
                            return false;
                        }
                    }
                } else {
                    if (x >= 0 && y >= 0 && x < cellArea.length && y < cellArea.length) {
                        if (inCircle(x, y, x0, y0, radius)) {
                            if (cellArea[x][y].getIsGrain()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean inCircle(int x, int y, int x0, int y0, int radius) {
        int leftSide = ((x - x0) * (x - x0)) + ((y - y0) + (y - y0));
        int rightSide = radius * radius;
        return leftSide <= rightSide;
    }

    public List<Occurance> calculateField() {
        List<Occurance> groups = new ArrayList<>();
        for (int i = 0; i < cellArea.length; i++) {
            for (int j = 0; j < cellArea[0].length; j++) {
                Occurance occurance = new Occurance(cellArea[i][j].getGrainId());
                boolean border = isBorder(i, j);
                if (!isOccuranceInOccurances(occurance, groups, border)) {
                    groups.add(occurance);
                    occurance.incrementCountOfTotalOccurances();
                    if(border){
                        occurance.incrementBorder();
                    }
                }
            }
        }
        return groups;
    }

    private boolean isOccuranceInOccurances(Occurance newOccurance, List<Occurance> occurances, boolean border) {
        for (Occurance occurance : occurances) {
            if (occurance.equals(newOccurance)) {
                occurance.incrementCountOfTotalOccurances();
                if(border){
                    occurance.incrementBorder();
                }
                return true;
            }
        }
        return false;
    }
    
    private boolean isBorder(int x, int y){
        AutomataCell cell = cellArea[x][y];
        BorderRule rule = new BorderRule();
        
        for(int [] arr : rule.getCoordinatesArray()){
            if(isPeriodical){
                int newX = (x + arr[0] + cellArea.length) % cellArea.length;
                int newY = (y + arr[1] + cellArea.length) % cellArea.length;
                AutomataCell temp = cellArea[newX][newY];
                if(!isTheSameId(cell.getGrainId(), temp.getGrainId())){
                    return true;
                }
            }else{
                if((x + arr[0]) >= 0 && (y + arr[1]) >= 0 && (x + arr[0] < cellArea.length) && (y + arr[1]) < cellArea[0].length){
                    AutomataCell temp = cellArea[x + arr[0]][y + arr[1]];
                    if(!isTheSameId(cell.getGrainId(), temp.getGrainId())){
                    return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isTheSameId(int[] id1, int [] id2){
        for (int i = 0; i < id1.length; i++) {
            if (id1[i] != id2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public void newHexRand(){
        this.hexRand = new HexagonalRandom();
    }

    public void newPentRand(){
        this.pentRand = new PentagonalRandom();
    }
}
