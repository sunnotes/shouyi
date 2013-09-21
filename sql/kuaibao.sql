----消息按天
SELECT DATE(msgtime),COUNT(*)  FROM message GROUP BY DATE(msgtime);

----消息按小时
SELECT HOUR(msgtime),COUNT(*)  FROM message GROUP BY HOUR(msgtime);

---关注人按天
SELECT DATE(subscribe_time),COUNT(*)  FROM user GROUP BY DATE(subscribe_time);

---关注人按小时
SELECT HOUR(subscribe_time), COUNT(*)  FROM user GROUP BY HOUR(subscribe_time);

---关注人按区域
SELECT province, COUNT(*) AS num FROM user GROUP BY province ORDER BY num DESC ;

---取消订阅按天
SELECT DATE(msgtime),COUNT(*)  FROM message WHERE event ='unsubscribe'  GROUP BY DATE(msgtime);

---取消订阅按小时
SELECT HOUR(msgtime),COUNT(*)  FROM message WHERE event ='unsubscribe'  GROUP BY HOUR(msgtime);

---未激活用户数按天
SELECT DATE(subscribe_time),COUNT(*)  FROM user WHERE fakeid =0 GROUP BY DATE(subscribe_time);

---激活用户数按天
SELECT DATE(subscribe_time),COUNT(*)  FROM user WHERE fakeid !=0 GROUP BY DATE(subscribe_time);

---消息类型
SELECT type_detail ,COUNT(*)  FROM message GROUP BY type_detail;

--消息类型按天
SELECT 	b.day,
SUM(IF(b.type = '天气查询',b.num , 0)) AS '天气查询'	,
SUM(IF(b.type = '查询收益',b.num , 0)) AS '查询收益'	,
SUM(IF(b.type = '计算收益',b.num , 0)) AS '计算收益'	,
SUM(IF(b.type = '请求帮助',b.num , 0)) AS '请求帮助'	,
SUM(IF(b.type = '问题或建议',b.num , 0)) AS '问题或建议'	,
SUM(IF(b.type = '其他',b.num , 0)) AS '其他'	,
SUM(IF(b.type = '异常',b.num , 0)) AS '异常'			
FROM (SELECT DATE(msgtime) AS DAY ,type_detail AS TYPE ,COUNT(*) AS num FROM message a GROUP BY DATE(a.msgtime),a.type_detail)b 
GROUP BY b.day

---每天的活跃人数
SELECT DATE(msgtime) ,COUNT(DISTINCT(fakeid)) FROM  message GROUP BY DATE(msgtime);


SELECT sex,DATE(subscribe_time),COUNT(*)  FROM user  GROUP BY DATE(subscribe_time),sex 

SELECT *  FROM user WHERE sex = 0 ORDER BY subscribe_time DESC

SELECT 	b.day,
SUM(IF(b.type = '天气查询',b.num , 0)) AS '天气查询'	,
SUM(IF(b.type = '查询收益',b.num , 0)) AS '查询收益'	,
SUM(IF(b.type = '计算收益',b.num , 0)) AS '计算收益'	,
SUM(IF(b.type = '请求帮助',b.num , 0)) AS '请求帮助'	,
SUM(IF(b.type = '问题或建议',b.num , 0)) AS '问题或建议'	,
SUM(IF(b.type = '其他',b.num , 0)) AS '其他'	,
SUM(IF(b.type = '异常',b.num , 0)) AS '异常'			
FROM (SELECT DATE(msgtime) AS DAY ,type_detail AS TYPE ,COUNT(*) AS num FROM message a GROUP BY DATE(a.msgtime),a.type_detail)b 
GROUP BY b.day

SELECT * FROM message WHERE type_detail = '' AND msg_type = 1 ORDER BY msgtime DESC


GROUP BY DATE(msgtime);

SUM(CASE WHEN SUBJECT='计算收益' THEN COUNT(*) END)AS '计算收益',

SUM(CASE WHEN SUBJECT='天气查询' THEN COUNT(*) END)AS '天气查询'

SELECT DATE(msgtime),type_detail ,COUNT(*)  FROM message GROUP BY type_detail,DATE(msgtime);




SELECT type_detail, COUNT( * ) 
FROM message
GROUP BY type_detail

SELECT * FROM message WHERE type_detail = ''

SELECT type_detail, COUNT( * ) 
FROM message
GROUP BY type_detail


UPDATE `message` SET `type_detail`='请求帮助' WHERE content = 'help' AND type_detail = ''




SELECT * FROM message WHERE  content = 't' AND type_detail = ''








DELETE  FROM message WHERE id =  6894




SELECT * FROM message WHERE event ='unsubscribe'  





---功能函数
UPDATE `message` SET `type_detail`=REPLACE(`type_detail`,' ','');

UPDATE `message` SET `type_detail`='计算收益' WHERE type_detail = 2

UPDATE `message` SET `type_detail`='查询收益' WHERE type_detail='计算收益信息'