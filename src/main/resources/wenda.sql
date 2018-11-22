
-- 记录用户个人信息
CREATE TABLE  user 	
(
	user_id				int(18)			NOT NULL	AUTO_INCREMENT,
	name			varchar(30)		NOT NULL 	COMMENT'用户名',
	password			varchar(128)	NOT NULL	COMMENT'用户密码',
	salt			varchar(30)		NOT NULL	COMMENT'加密用',
	sex			int(2)		 	 	COMMENT'性别：未知0，男1，女2',
	introduction	varchar(128)			COMMENT'个人介绍',
	head_url			varchar(128)			 COMMENT'头像路径',
	department		varchar(30)			 	COMMENT'所在院系',
	profession		varchar(30)			 	COMMENT'专业',
	startYear		date		COMMENT'入学年份',
	endYear		date		COMMENT'毕业年份',

	phone  varchar(13)   COMMENT'手机号',

	email varchar(30)   COMMENT'邮箱',

	PRIMARY KEY (user_id)
)ENGINE=InnoDB;



-- -- 记录用户的身份验证信息
-- CREATE TABLE proof
-- (
-- 	proof_id	int(18)			NOT NULL	AUTO_INCREMENT,
-- 	type  varchar(10)		NOT NULL	COMMENT'身份类型',
-- 	real_name  varchar(10)		NOT NULL	COMMENT'身份名称',
-- 	user_id		int(18)		NOT NULL,
--
-- 	primary key (proof_id)
-- )ENGINE=InnoDB;



-- 记录用户提出的问题
CREATE TABLE question  
(
	question_id				int(18)			NOT NULL	AUTO_INCREMENT,
	title 		varchar(128)	NOT NULL COMMENT'标题',

	content	varchar(1000)			COMMENT'问题描述',

	-- question_image			varchar(128)	COMMENT'问题配图',

	user_id				int(18)			NOT NULL,

	anonymous			int(3)   NOT NULL 	DEFAULT 0		COMMENT'是否匿名,是1，否0',


	tag_id 				int(18)			NOT NULL COMMENT'问题标签',

	createTime	date NOT NULL,

	comment_count int(18) NOT NULL  COMMENT'回答数',

	look_count int(18) NOT NULL  COMMENT'浏览数',

	primary key (question_id)
)ENGINE=InnoDB;


-- 私信
CREATE TABLE message
(
  message_id	int(18)			NOT NULL	AUTO_INCREMENT,

  from_id int(18)			NOT NULL COMMENT'谁发的',

  to_id int(18)			NOT NULL COMMENT'发给谁',

  has_read int(3)			NOT NULL COMMENT'是否读',

  content varchar(1000)			COMMENT'内容',

  create_date timestamp NOT NULL,

  conversation_id int(18)	NOT NULL COMMENT'双向联系',

  primary key (message_id)
)ENGINE=InnoDB;


-- 私信
CREATE TABLE comment
(
  comment_id int(18)			NOT NULL	AUTO_INCREMENT,

  entity_id int(18)			NOT NULL	COMMENT'实体id',

  entity_type int(5)			NOT NULL	COMMENT'实体类型',

  user_id  int(18)			NOT NULL	COMMENT'用户id',

  comment_status int(5)			NOT NULL	COMMENT'状态（删除）',

  create_date timestamp NOT NULL,

  content varchar(1000)			COMMENT'内容',

   primary key (comment_id)
)ENGINE=InnoDB;



--
-- -- 问题的标签，用于归类话题
-- CREATE TABLE tag
-- (
--
--
-- 	tag_id 		int(18)			NOT NULL 	,
--
-- 	tag_name  	varchar(20)		NOT NULL 	COMMENT'标签名称',
--
--
-- 	primary key (tag_id)
-- )ENGINE=InnoDB;


-- -- 每个问题下的回答
-- CREATE TABLE answer
-- (
--
-- 	answer_id				int(18)			NOT NULL	AUTO_INCREMENT,
--
-- 	question_id				int(18)			NOT NULL,
--
-- 	user_id				int(18)			NOT NULL,
--
-- 	anonymous			int(3)   NOT NULL 	DEFAULT 0		COMMENT'是否匿名,是1，否0',
--
--
-- 	content		varchar(9000)			NOT NULL COMMENT'回答内容',
--
-- 	createTime	date NOT NULL,
--
-- 	updateTime	date NOT NULL,
--
-- 	comment_count				int(18)			NOT NULL COMMENT'评论数',
--
-- 	agree_count		int(18)			NOT NULL COMMENT'点赞数',
--
-- 	primary key (answer_id)
-- )ENGINE=InnoDB;



-- -- 记录某个回答的点赞数
-- CREATE TABLE answer_agree  
-- (

-- 	answer_agree_id				int(18)			NOT NULL	AUTO_INCREMENT,

-- 	agree_count		int(18)			NOT NULL COMMENT'点赞数量',	

		
-- 	primary key (answer_agree_id)
-- )ENGINE=InnoDB;



-- -- 记录某个回答点赞的是谁
-- CREATE TABLE user_answer_agree
-- (
--
-- 	user_answer_agree_id				int(18)			NOT NULL	AUTO_INCREMENT,
--
-- 	answer_id				int(18)			NOT NULL,
--
-- 	user_id				int(18)			,
--
-- 	primary key (user_answer_agree_id)
-- )ENGINE=InnoDB;



-- -- 记录某个回答的评论数
-- CREATE TABLE answer_comment 
-- (

-- 	answer_comment_id				int(18)			NOT NULL	AUTO_INCREMENT,


-- 	comment_count		int(18)			NOT NULL COMMENT'评论数量',	
	
	
-- 	primary key (answer_comment_id)
-- )ENGINE=InnoDB;






-- -- 记录某个回答评论的是谁、评论的内容
-- CREATE TABLE user_answer_comment
-- (
--
-- 	user_answer_comment_id				int(18)			NOT NULL	AUTO_INCREMENT,
--
-- 	answer_id				int(18)			NOT NULL,
--
-- 	user_id				int(18)			NOT NULL		,
--
-- 	content		varchar(200)			NOT NULL COMMENT'评论内容',
--
-- 	comment_time	date NOT NULL COMMENT'评论时间',
--
--
-- 	primary key (user_answer_comment_id)
-- )ENGINE=InnoDB;


-- 记录某个评论下的回复






-- -- 对于某个问题，关注的人
-- CREATE TABLE question_concern
-- (
--
-- 	question_concern_id				int(18)			NOT NULL	AUTO_INCREMENT,
--
-- 	question_id  int(18)			NOT NULL,
--
-- 	user_id		int(18)			NOT NULL ,
--
-- 	primary key (question_concern_id)
-- )ENGINE=InnoDB;

