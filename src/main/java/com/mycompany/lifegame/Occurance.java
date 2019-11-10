/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lifegame;

/**
 *
 * @author ales
 */
public class Occurance {

    private int[] id;
    private int countOfTotalOccurances;
    private int borderLength;

    public Occurance(int[] id) {
        this.id = id;
        this.countOfTotalOccurances = 0;
        this.borderLength = 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Occurance occurance = (Occurance) obj;
        for (int i = 0; i < this.id.length; i++) {
            if (this.id[i] != occurance.id[i]) {
                return false;
            }
        }
        return true;
    }

    public void incrementCountOfTotalOccurances() {
        this.countOfTotalOccurances++;
    }

    public int getCountOfTotalOccurances() {
        return this.countOfTotalOccurances;
    }

    public int[] getId() {
        return id;
    }

    public int getBorderLength() {
        return this.borderLength;
    }

    public void incrementBorder() {
        this.borderLength++;
    }
}
