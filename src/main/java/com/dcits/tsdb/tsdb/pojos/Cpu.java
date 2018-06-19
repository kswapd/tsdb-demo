package com.dcits.tsdb.tsdb.pojos;

import com.dcits.tsdb.tsdb.annotations.Column;
import com.dcits.tsdb.tsdb.annotations.Measurement;

/**
 * Created by kongxiangwen on 6/19/18 w:25.
 */



@Measurement(name = "cpu")
public class Cpu {
	@Column(name = "time")
	private String time;

	@Column(name = "idle")
	private Integer idle;

	@Column(name = "user")
	private Integer user;

	@Column(name = "system")
	private Integer system;

	public String toString(){
		return String.format("cpu info:[time:%s, user:%d, system:%d, idle:%d", time, user,system, idle);
	}

	// getters (and setters if you need)
}
