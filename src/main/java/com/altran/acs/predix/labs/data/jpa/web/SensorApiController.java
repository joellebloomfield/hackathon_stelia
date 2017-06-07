package com.altran.acs.predix.labs.data.jpa.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.altran.acs.predix.labs.data.jpa.domain.Sensor;
import com.altran.acs.predix.labs.data.jpa.service.SensorService;

@ComponentScan
@RestController
public class SensorApiController {

    @Autowired  private SensorService sensorService;

    public static final String SENSOR = "/sensors";
    public static final String GET_SENSOR_BY_ID = SENSOR + "/{id}";
    public static final String GET_SENSOR_BY_DEVICE_ID = SENSOR + "/device/{id}";
	private static final String LAST_SENSORS = SENSOR + "/last";

    @RequestMapping(value = LAST_SENSORS, method = RequestMethod.GET)
    public Collection<Sensor> getLastEvent() throws Exception {
        Collection<Sensor> sensors = sensorService.getLastEvent();
        return sensors;
    }

    @RequestMapping(value = GET_SENSOR_BY_ID, method = RequestMethod.GET)
    public Sensor sensorById(@PathVariable  Long id) {
        return this.sensorService.getSensorById(id);
    }
    
    @RequestMapping(value = GET_SENSOR_BY_DEVICE_ID , method = RequestMethod.GET)
    public Collection<Sensor> locationByDeviceId(@PathVariable  Long id) {
    	return sensorService.getSensorsByDeviceId(id);
    }

    @RequestMapping(value = SENSOR, method = RequestMethod.GET)
    public Collection<Sensor> sensors() throws Exception {
        Collection<Sensor> sensors = sensorService.getAllSensors();
        return sensors;
    }

    @RequestMapping(value = SENSOR, method = RequestMethod.POST)
    public Long addLocation(@RequestBody Sensor sensor) {
        return sensorService.createSensor(sensor).getId();
    }
}