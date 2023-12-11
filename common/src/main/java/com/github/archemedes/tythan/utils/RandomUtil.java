package com.github.archemedes.tythan.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public final class RandomUtil {
    public static boolean roll(int maxNumber, int requiredRoll, int numberOfDiceToRoll){
        int totalRoll = 0;
        for(int i = 0; i < numberOfDiceToRoll; i++){
            totalRoll += roll(maxNumber);
        }
        if(totalRoll >= requiredRoll) return true;
        return false;
    }
    public static int roll(int maxNumber){
        return ThreadLocalRandom.current().nextInt(1, maxNumber+1);
    }

}
