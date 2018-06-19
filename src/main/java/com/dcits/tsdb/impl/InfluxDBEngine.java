package com.dcits.tsdb.impl;

import com.dcits.tsdb.interfaces.TSDBEngine;
import com.dcits.tsdb.pojos.Cpu;
import com.dcits.tsdb.utils.InfluxDBResultMapper;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Created by kongxiangwen on 6/19/18 w:25.
 */

@Repository("tsdbEngine")
public class InfluxDBEngine implements TSDBEngine{


	@Value("${influxdb.address}")
	private String address;
	@Value("${influxdb.user}")
	private String user;
	@Value("${influxdb.password}")
	private String password;

	@Value("${influxdb.dbName}")
	private String dbName = "kxw_metrics_v2";
	@Value("${influxdb.rpName}")
	private String rpName = "rp_30d";
	@Value("${influxdb.maxBatchSize}")
	private int maxBatchSize = 5;
	@Value("${influxdb.maxBatchInterval}")
	private int maxBatchInterval = 100;

	private static InfluxDBEngine influxDBengine = null;
	private  InfluxDB influxDB = null;


	@PreDestroy
	public void Destroy()
	{
		if(influxDB != null) {
			influxDB.close();
		}
	}
	@PostConstruct
	public void init()
	{


		System.out.println(address);
		influxDB = InfluxDBFactory.connect(address, user, password);
		influxDB.createDatabase(dbName);
		influxDB.enableBatch(maxBatchSize, maxBatchInterval, TimeUnit.MILLISECONDS);
	}

	@Override
	public void write(Point data)
	{

		influxDB.write(dbName, rpName, data);
	}

	@Override
	public QueryResult query(String queryLang)
	{
		QueryResult queryResult = influxDB.query(new Query(queryLang, dbName));
		return queryResult;

	}
	@Override
	public <T> List<T> queryPOJOs(String queryLang, final Class<T> clazz)
	{


		QueryResult queryResult = query(queryLang);
		InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
		List<T> pojoList = null;
		try {
			pojoList = resultMapper.toPOJO(queryResult, clazz);
		}
		catch (RuntimeException e){

		}

		return pojoList;
	}
	/*public static InfluxDBEngine createInstance()
	{
		if(engine == null){
			engine = new InfluxDBEngine();

		}
		return engine;

	}*/

/*
	public void setAddress(String address) {
		this.address = address;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public void setRpName(String rpName) {
		this.rpName = rpName;
	}

	public void setMaxBatchSize(int maxBatchSize) {
		this.maxBatchSize = maxBatchSize;
	}

	public void setMaxBatchInterval(int maxBatchInterval) {
		this.maxBatchInterval = maxBatchInterval;
	}*/


}
