/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lifegame.CellularAutomataRules;

/**
 *
 * @author ales
 */
public class MooreRule implements CellularAutomataRule {

    int[][] coordinates = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    @Override
    public int[][] getCoordinatesArray() {
        return coordinates;
    }
}
