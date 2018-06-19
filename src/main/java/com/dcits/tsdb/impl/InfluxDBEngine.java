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

/**
 * Created by kongxiangwen on 6/19/18 w:25.
 */
public class InfluxDBEngine implements TSDBEngine{

	private String address;
	private String user;
	private String password;

	private static InfluxDBEngine influxDBengine = null;

	private  InfluxDB influxDB = null;
	private String dbName = "kxw_metrics_v2";
	private String rpName = "rp_30d";
	private int maxBatchSize = 5;
	private int maxBatchInterval = 100;


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


	public void write(String dbName, String rpName, Point data)
	{
		influxDB.write(dbName, rpName, data);
	}

	public QueryResult query(String dbName, String queryLang)
	{
		QueryResult queryResult = influxDB.query(new Query(queryLang, dbName));
		return queryResult;

	}
	public <T> List<T> queryPOJOs(String dbName, String queryLang, final Class<T> clazz)
	{
		QueryResult queryResult = query(dbName, queryLang);
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
	}


}
