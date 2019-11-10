/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lifegame.CellularAutomataRules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ales
 */
public class HexagonalRandom implements CellularAutomataRule {

    int[][] posibilites = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
    int[][] coordinates;

    public HexagonalRandom() {
        Random rand = new Random();

        List<int[]> posibilitiesList = new ArrayList<>(Arrays.asList(posibilites));
        coordinates = new int[7][2];
        coordinates[0][0] = 0;
        coordinates[0][1] = 0;
        for (int i = 1; i < 7; i++) {
            int randomIndex = rand.nextInt(posibilitiesList.size());
            coordinates[i] = posibilitiesList.get(randomIndex);
            posibilitiesList.remove(randomIndex);
        }

    }

    @Override
    public int[][] getCoordinatesArray() {

        return coordinates;
    }

}
