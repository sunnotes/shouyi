package com.sunnotes.shouyibao.model;

import java.util.Date;

public class FundDetail {
	// 基金码
	private String code;
	// 基金名
	private String name;
	// 别名
	private String nickname;
	// 日期
	private Date date;
	// 万份收益
	private String profit;
	// 七日年化
	private String rate;
	// 类型
	private String type;

	// 购买信息
	private String buyinfo;
	// 合同生效日
	private String startdate;
	// 基金托管人
	private String custodianbank;
	// 投资范围
	private String inverstmentrange;
	// 投资目标
	private String inverstmentaim;
	// 业绩比较基准
	private String benchmark;
	// 风险收益特征
	private String risk;
	// 备注
	private String memo;
	// 更新时间
	private String updatetime;

	public FundDetail() {
		super();

	}
	
	

	public FundDetail(String code, String name, String nickname, Date date,
			String profit, String rate, String type, String buyinfo,
			String startdate, String custodianbank, String inverstmentrange,
			String inverstmentaim, String benchmark, String risk, String memo,
			String updatetime) {
		super();
		this.code = code;
		this.name = name;
		this.nickname = nickname;
		this.date = date;
		this.profit = profit;
		this.rate = rate;
		this.type = type;
		this.buyinfo = buyinfo;
		this.startdate = startdate;
		this.custodianbank = custodianbank;
		this.inverstmentrange = inverstmentrange;
		this.inverstmentaim = inverstmentaim;
		this.benchmark = benchmark;
		this.risk = risk;
		this.memo = memo;
		this.updatetime = updatetime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
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

	public String getBuyinfo() {
		return buyinfo;
	}

	public void setBuyinfo(String buyinfo) {
		this.buyinfo = buyinfo;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getCustodianbank() {
		return custodianbank;
	}

	public void setCustodianbank(String custodianbank) {
		this.custodianbank = custodianbank;
	}

	public String getInverstmentrange() {
		return inverstmentrange;
	}

	public void setInverstmentrange(String inverstmentrange) {
		this.inverstmentrange = inverstmentrange;
	}

	public String getInverstmentaim() {
		return inverstmentaim;
	}

	public void setInverstmentaim(String inverstmentaim) {
		this.inverstmentaim = inverstmentaim;
	}

	public String getBenchmark() {
		return benchmark;
	}

	public void setBenchmark(String benchmark) {
		this.benchmark = benchmark;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
