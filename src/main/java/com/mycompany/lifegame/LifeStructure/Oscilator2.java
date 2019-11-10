/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lifegame.LifeStructure;

/**
 *
 * @author ales
 */
public class Oscilator2 implements LifeStructure{
    
    public Oscilator2(){};
    
    @Override
    public boolean [][] getLifeArea(){
        boolean [][] glider = new boolean[6][6];
        glider[1][1] = true;
        glider[1][2] = true;
        glider[2][1] = true;
        glider[3][4] = true;
        glider[4][3] = true;
        glider[4][4] = true;
        return glider;
    }
}
