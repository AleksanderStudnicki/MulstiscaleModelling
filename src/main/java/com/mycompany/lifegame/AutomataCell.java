/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lifegame;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author ales
 */
public class AutomataCell {

    private AtomicBoolean isGrain;
    private AtomicInteger[] grainId;

    public AutomataCell() {
        this.isGrain = new AtomicBoolean(false);
        this.grainId = new AtomicInteger[3];
        for (int i = 0; i < 3; i++) {
            grainId[i] = new AtomicInteger();
        }
    }

    public void setIsGrain(boolean b) {
        this.isGrain.set(b);
    }

    public boolean getIsGrain() {
        return this.isGrain.get();
    }

    public void setGrainId(int[] id) {
        for (int i = 0; i < id.length; i++) {
            grainId[i].set(id[i]);
        }
    }

    public int[] getGrainId() {
        int[] arr = new int[3];
        for (int i = 0; i < 3; i++) {
            arr[i] = grainId[i].get();
        }
        return arr;
    }

    public void setRandomId() {
        byte[] random = new byte[3];
        ThreadLocalRandom.current().nextBytes(random);
        for (int i = 0; i < this.grainId.length; i++) {
            int temp = (random[i] & 0xFF);
            this.grainId[i].set(temp);
        }
    }
}
