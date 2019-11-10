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
public class Glider implements LifeStructure{
    
    public Glider(){};
    
    @Override
    public boolean [][] getLifeArea(){
        boolean [][] glider = new boolean[5][5];
        glider[2][1] = true;
        glider[3][2] = true;
        glider[1][3] = true;
        glider[2][3] = true;
        glider[3][3] = true;
        return glider;
    }
}
