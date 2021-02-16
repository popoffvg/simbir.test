package ru.popoffvg.simbir;

import java.util.Map;

public class StatisticViewer {
    public void print(Map<String, Integer> statistic) {
        statistic.forEach((k,v) -> System.out.println(k.toUpperCase() + " - " + v));
    }
}
