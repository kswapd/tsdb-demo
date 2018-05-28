package main;


import java.util.concurrent.TimeUnit;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

/**
 * Created by kongxiangwen on 5/28/18 w:22.
 */
public class main {

	public static void main(String args[]){
		InfluxDB influxDB = InfluxDBFactory.connect("http://10.88.2.100:8086", "root", "root");
		String dbName = "aTimeSeries";
		influxDB.createDatabase(dbName);
		//influxDB.c
		influxDB.setDatabase(dbName);
		String rpName = "aRetentionPolicy";
		influxDB.createRetentionPolicy(rpName, dbName, "30d", "30m", 2, true);
		influxDB.setRetentionPolicy(rpName);


		//influxDB.enableBatch(BatchOptions.DEFAULTS);

		influxDB.write(Point.measurement("cpu")
				.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.addField("idle", 90L)
				.addField("user", 9L)
				.addField("system", 1L)
				.build());

		influxDB.write(Point.measurement("disk")
				.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.addField("used", 80L)
				.addField("free", 1L)
				.build());

		Query query = new Query("SELECT * FROM cpu", dbName);
		QueryResult qr = influxDB.query(query);
		System.out.println(qr.getResults().toString());
		influxDB.dropRetentionPolicy(rpName, dbName);
		influxDB.deleteDatabase(dbName);
		influxDB.close();
	}

}
