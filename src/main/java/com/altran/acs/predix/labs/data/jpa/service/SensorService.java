package com.altran.acs.predix.labs.data.jpa.service;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altran.acs.predix.labs.data.jpa.domain.Location;
import com.altran.acs.predix.labs.data.jpa.domain.Sensor;

@Service
@SuppressWarnings("unchecked")
@Transactional
public class SensorService {

    public static final String SENSORS = "sensors";

    @PersistenceContext
    private EntityManager em;

    public Sensor createSensor(Sensor sensor) {
        em.persist(sensor);
        return sensor;
    }

    public Collection<Sensor> getSensorsByDeviceId(Long id) {
        String sql = "select c.* from Sensor c where (c.deviceId = " + id + ")";
        return em.createNativeQuery(sql, Sensor.class)
                .getResultList();
    }
    
    @Transactional(readOnly = true)
    public  List<Sensor> getLastEvent() {
    	return em.createQuery("FROM Sensor c WHERE c.id IN (SELECT max(cc.id) FROM Sensor cc GROUP BY name)").getResultList();
    }

    @Transactional(readOnly = true)
    public List<Sensor> getAllSensors() {
        return em.createQuery("FROM Sensor").getResultList();
    }
    
    @Cacheable(SENSORS)
    @Transactional(readOnly = true)
    public Sensor getSensorById(Long id) {
        return em.find(Sensor.class, id);
    }

    @CacheEvict(SENSORS)
    public void deleteSensor(Long id) {
    	Sensor sensor = getSensorById(id);
        em.remove(sensor);
    }
}
