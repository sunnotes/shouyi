package com.sunnotes.shouyibao.model;

public class FundValue {
	// 基金码
	private String code;
	// 基金名
	private String name;
	// 日期
	private String date;
	// 更新时间
	private String updatetime;
	// 万份收益
	private String profit;
	// 七日年化
	private String rate;

	public FundValue() {
		super();
	}

	public FundValue(String code, String name, String date, String updatetime,
			String profit, String rate) {
		super();
		this.code = code;
		this.name = name;
		this.date = date;
		this.updatetime = updatetime;
		this.profit = profit;
		this.rate = rate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}


}
