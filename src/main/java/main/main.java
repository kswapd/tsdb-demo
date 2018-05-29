package main;




import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.influxdb.BatchOptions;
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
		InfluxDB influxDB = InfluxDBFactory.connect("http://10.88.2.105:8086", "root", "root");
		String dbName = "kxw_metrics_v2";
		String rpName = "rp_3h";
		boolean isStart = true;
		if(!influxDB.databaseExists(dbName))
		{
			influxDB.createDatabase(dbName);

			//String rpName = "aRetentionPolicy";
			//influxDB.createRetentionPolicy(rpName, dbName, "30d", "30m", 2, true);
			//influxDB.setRetentionPolicy(rpName);
		}
		influxDB.setDatabase(dbName);
		influxDB.setRetentionPolicy(rpName);

		//influxDB.enableBatch();

		influxDB.enableBatch(BatchOptions.DEFAULTS.actions(1).flushDuration(100));

		while(isStart) {
			int randIdle = (int)(Math.random()*30);
			int randUser = (int)(Math.random()*20);
			int randSys = (int)(Math.random()*10);
			int randDiskUsed = (int)(Math.random()*50);
			int randDiskFree = (int)(Math.random()*50);
			influxDB.write(Point.measurement("cpu")
					.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
					.addField("idle", 90L + randIdle)
					.addField("user", 9L + randUser)
					.addField("system", 1L+randSys)
					.tag("host", "kxw_v1")
					.build());

			influxDB.write(Point.measurement("disk")
					.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
					.addField("used", 80L +randDiskUsed)
					.addField("free", 300L + randDiskFree)
					.tag("host", "kxw_v1")
					.build());

			try {
				Thread.sleep(1000);
			}catch(InterruptedException e){

			}







			System.out.println("ok:"+ new Date().toString());

		}
		/*Query query = new Query("SELECT * FROM cpu", dbName);
		QueryResult qr = influxDB.query(query);
		System.out.println(qr.getResults().toString());
		influxDB.dropRetentionPolicy(rpName, dbName);
		influxDB.deleteDatabase(dbName);*/
		influxDB.close();
	}

}
