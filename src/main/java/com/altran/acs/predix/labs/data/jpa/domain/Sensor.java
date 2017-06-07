package com.altran.acs.predix.labs.data.jpa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Sensor implements Serializable {

	private static final long serialVersionUID = -8351625367255131992L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    @NotNull
	private Long deviceId;
    
	private Date tmStmp;	
	/**
	 * @return the tmStmp
	 */
	public Date getTmStmp() {
		return this.tmStmp;
	}
	/**
	 * @param tmStmp the tmStmp to set
	 */
	public void setTmStmp(Date tmStmp) {
		this.tmStmp = tmStmp;
	}
	private String name;
	private Double val;
	
	
	/**
	 * @return the val
	 */
	public Double getVal() {
		return this.val;
	}
	/**
	 * @param val the val to set
	 */
	public void setVal(Double val) {
		this.val = val;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
}
