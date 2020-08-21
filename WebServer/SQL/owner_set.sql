create table owner_info( 
	owner_email char(32), 
	owner_pwd char(16) not null, 
	owner_name char(16) not null, 
	owner_phone char(16) not null, 
    owner_rs_id int(11),

	PRIMARY KEY(owner_email), 
	UNIQUE KEY(owner_phone) 
);

create table restaurant(
	rs_id INT(11) not null AUTO_INCREMENT,
    rs_name char(50) not null,
    rs_intro char(300),
    rs_open char(5) not null,
    rs_close char(5) not null,
    rs_owner char(32) not null,
    
    PRIMARY KEY(rs_id)
);