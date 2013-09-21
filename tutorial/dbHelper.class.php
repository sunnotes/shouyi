
<?php 
class DBHelper
{
	private $mysql;

	public function __construct()
	{
		$this->mysql = new SaeMysql();
	}

	/**
	 * 查询最新的余额宝收益信息
	 * @return unknown
	 */
	public function getlast()
	{
		$sql = "SELECT date  , profit ,rate  FROM fund ORDER BY DATE DESC LIMIT 1;";
		$data = $this->mysql->getData( $sql );
		$record['date'] = $data[0]['date'];
		$record['profit'] = $data[0]['profit'];
		$record['rate'] = $data[0]['rate'];
		return $record;
	}
	
	public function getHistory()
	{
		$sql = "SELECT date  , profit ,rate  FROM fund ORDER BY DATE DESC LIMIT 7;";
		$data = $this->mysql->getData( $sql );
		$weekarray=array('周日','周一','周二','周三','周四','周五','周六');
		$content ="收益历史播报:\n\n";
		foreach ($data as $record){
			$record_date = strtotime($record['date']);
			$content = $content.date('n月j号',$record_date).
					'('.$weekarray[date('w',$record_date)].')'.
					"\n万份收益:".$record['profit']."元\n七日年化:".
								$record['rate']."%\n\n";
		}		
		return $content;
		
	}

	public function insert( $date, $profit, $rate)
	{
		$sql = "INSERT  INTO `fund` ( `date` , `profit` , `rate` ,`updatetime` ) VALUES ( '$date' , '$profit' , '$rate'   , NOW() ) ";
		$this->mysql->runSql( $sql );
	}

	public function query_user_id( $wxid )
	{
		$sql_select = "select  id FROM  user WHERE wxid = '$wxid'";
		$data = $this->mysql->getData( $sql_select );
		if ($data == '') {
			$sql_insert = "INSERT  INTO user ( wxid , subscribe_time  ) VALUES ( '$wxid'  , NOW() ) ";
			$this->mysql->runSql($sql_insert);
			$data = $this->mysql->getData( $sql_select );
		}
		if($data != '')
			return $data[0]['id'];
		else
			return false;
	}

	public function query_user( $wxid )
	{
		$sql_select = "select id, fakeid, sex, nickname, current_city FROM  user WHERE wxid = '$wxid'";
		$data = $this->mysql->getData( $sql_select );
		if ($data == '') {
			$sql_insert = "INSERT  INTO user ( wxid , subscribe_time  ) VALUES ( '$wxid'  , NOW() ) ";
			$this->mysql->runSql($sql_insert);
			$data = $this->mysql->getData( $sql_select );
		}
		if($data == ''){
			return false;
		}
		$record['userid'] = $data[0]['id'];
		$record['fakeid'] = $data[0]['fakeid'];
		$record['sex'] = $data[0]['sex'];
		$record['nickname'] = $data[0]['nickname'];
		$record['city'] = $data[0]['current_city'];
		return $record;
	}

	public function get_city_info( $wxid )
	{
		$city = '';
		$sql = "select  current_city FROM  user WHERE wxid =  '$wxid' ";
		$data = $this->mysql->getData( $sql );
		$city = $data[0]['city'];
		return $city;
	}

	public function update_city_info( $wxid ,$city)
	{
		$sql = "update user set current_city = '$city' where wxid =  '$wxid' ";
		//echo $sql;
		$this->mysql->runSql( $sql );
	}

	public function add_msg_text( $object, $userinfo, $type)
	{
		$sql = "insert into message (userid,touser,fromuser,fakeid,
				nickname,sex,msgtime,create_time,msg_type,type_detail,msg_id,content)
				VALUES ( ' ".$userinfo['userid'] .
				"'  , '$object->ToUserName','$object->FromUserName', ".
				"'".$userinfo['fakeid'].
				"'  , '".$userinfo['nickname'].
				"'  , '".$userinfo['sex'].
				"' , now(),".
				"'$object->CreateTime',1,
				'$type','$object->MsgId','$object->Content')";
		$this->mysql->runSql( $sql );
		return $sql;
	}

	public function add_msg_location( $object, $uid)
	{
		$sql = "insert into message (userid,touser,fromuser,create_time,msgtime,msg_type,msg_id,location_x,location_y,scale,label)
			VALUES ( '$uid' ,'$object->ToUserName','$object->FromUserName',
			'$object->CreateTime',now(),3,'$object->MsgId','$object->Location_X','$object->Location_Y',
			'$object->Scale','$object->Label')";
			$this->mysql->runSql( $sql );
		return $sql;
	}

	public function add_msg_event( $object, $uid)
	{
		$sql = "insert into message (userid,touser,fromuser,create_time,msg_type,msg_id,event,event_key)
		VALUES ( '$uid' ,'$object->ToUserName','$object->FromUserName',
		'$object->CreateTime',5,'$object->MsgId','$object->Event','$object->EventKey')";
		$this->mysql->runSql( $sql );
		return $sql;
	}


	public function update_user_info( $wxid, $userinfo)
	{
		if (trim($userinfo['Province']) == '北京'||trim($userinfo['Province']) == '上海'
				||trim($userinfo['Province']) == '天津'||trim($userinfo['Province']) == '重庆'){
			$current_city  = trim($userinfo['Province']);
		}else{
			$current_city  = trim($userinfo['City']);
		}
		$sql = " update user set fakeid = '".trim($userinfo['FakeId']).
		"'  , nickname = '".trim($userinfo['NickName']).
		"'  , remarkname = '".trim($userinfo['ReMarkName']).
		"'  , username = '".trim($userinfo['Username']).
		"'  , country = '".trim($userinfo['Country']).
		"'  , province = '".trim($userinfo['Province']).
		"'  , city = '".trim($userinfo['City']).
		"'  , current_city = '".trim($current_city).
		"'  , sex = ' ".trim($userinfo['Sex']).
		"'  , signature = '".trim($userinfo['Signature']).
		"'  where wxid = "."'$wxid'";
		$this->mysql->runSql( $sql );
		return $sql;
	}
}
