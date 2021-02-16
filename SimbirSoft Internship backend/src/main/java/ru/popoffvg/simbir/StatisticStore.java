package ru.popoffvg.simbir;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;

public class StatisticStore {

    private final EntityManager em;

    public StatisticStore() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ru.popoffvg.data.jpa.hibernate");
        em = entityManagerFactory.createEntityManager();
    }

    public StatisticStore(Map<String, String> persistenceSettings) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ru.popoffvg.data.jpa.hibernate", persistenceSettings);
        em = entityManagerFactory.createEntityManager();
    }

    public Map<String, Integer> store(Map<String, Integer> statistic) {
        em.getTransaction().begin();
        statistic.forEach((key, value) -> {
            WordStatistic ws = new WordStatistic();
            ws.setWord(key);
            ws.setUsage(value);
            em.persist(ws);
        });
        em.getTransaction().commit();
        return statistic;
    }
}

