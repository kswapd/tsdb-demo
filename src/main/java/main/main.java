package main;




import com.dcits.tsdb.impl.InfluxDBEngine;
import com.dcits.tsdb.interfaces.TSDBEngine;
import com.dcits.tsdb.pojos.Cpu;
import com.dcits.tsdb.utils.InfluxDBResultMapper;
import java.util.List;
import java.util.concurrent.TimeUnit;
//import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by kongxiangwen on 5/28/18 w:22.
 */
public class main {

	public static void main(String args[]){

		String dbName = "kxw_metrics_v2";
		String rpName = "rp_30d";
		boolean isStart = true;
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"tsdb.xml"});
		context.start();
		InfluxDBEngine engine = (InfluxDBEngine)context.getBean("tsdbEngine");

		while(isStart) {
			int randIdle = (int)(Math.random()*30);
			int randUser = (int)(Math.random()*20);
			int randSys = (int)(Math.random()*10);
			int randDiskUsed = (int)(Math.random()*50);
			int randDiskFree = (int)(Math.random()*50);
			engine.write(dbName,rpName, Point.measurement("cpu")
					.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
					.addField("idle", 90L + randIdle)
					.addField("user", 9L + randUser)
					.addField("system", 1L+randSys)
					.tag("host", "kxw_v2")
					.build());

			engine.write(dbName,rpName, Point.measurement("disk")
					.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
					.addField("used", 80L +randDiskUsed)
					.addField("free", 300L + randDiskFree)
					.tag("host", "kxw_v2")
					.build());
			try {
				List<Cpu> pojoList = engine.queryPOJOs(dbName, "SELECT * FROM cpu WHERE time > now() - 5s order by time desc limit 10", Cpu.class);
				for(Cpu cpu:pojoList){
					System.out.println(cpu.toString());
				}
			}
			catch (RuntimeException e){
				System.out.println(e.getMessage());
			}
			try {
				Thread.sleep(1000);
			}catch(InterruptedException e){

			}
		}
	}

}
