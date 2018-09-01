-- ============================================================
--   SGBD      		  :  PostgreSql                     
-- ============================================================




-- ============================================================
--   Sequences                                      
-- ============================================================
create sequence SEQ_HEATER
	start with 1000 cache 20; 

create sequence SEQ_PROTOCOL
	start with 1000 cache 20; 

create sequence SEQ_WEEKLY_CALENDAR
	start with 1000 cache 20; 


-- ============================================================
--   Table : HEATER                                        
-- ============================================================
create table HEATER
(
    HEA_ID      	 NUMERIC     	not null,
    NAME        	 VARCHAR(100)	,
    DNS_NAME    	 VARCHAR(100)	,
    ACTIVE      	 BOOL        	,
    WCA_ID      	 NUMERIC     	not null,
    PRO_CD      	 VARCHAR(100)	not null,
    constraint PK_HEATER primary key (HEA_ID)
);

comment on column HEATER.HEA_ID is
'Id';

comment on column HEATER.NAME is
'Name';

comment on column HEATER.DNS_NAME is
'DNS Name';

comment on column HEATER.ACTIVE is
'Active';

comment on column HEATER.WCA_ID is
'WeeklyCalendar';

comment on column HEATER.PRO_CD is
'Protocol';

-- ============================================================
--   Table : PROTOCOL                                        
-- ============================================================
create table PROTOCOL
(
    PRO_CD      	 VARCHAR(100)	not null,
    LABEL       	 VARCHAR(100)	,
    constraint PK_PROTOCOL primary key (PRO_CD)
);

comment on column PROTOCOL.PRO_CD is
'Id';

comment on column PROTOCOL.LABEL is
'Name';

-- ============================================================
--   Table : WEEKLY_CALENDAR                                        
-- ============================================================
create table WEEKLY_CALENDAR
(
    WCA_ID      	 NUMERIC     	not null,
    NAME        	 VARCHAR(100)	,
    JSON_VALUE  	 TEXT        	,
    constraint PK_WEEKLY_CALENDAR primary key (WCA_ID)
);

comment on column WEEKLY_CALENDAR.WCA_ID is
'Id';

comment on column WEEKLY_CALENDAR.NAME is
'Name';

comment on column WEEKLY_CALENDAR.JSON_VALUE is
'Value as json';



alter table HEATER
	add constraint FK_HEA_PRO_PROTOCOL foreign key (PRO_CD)
	references PROTOCOL (PRO_CD);

create index HEA_PRO_PROTOCOL_FK on HEATER (PRO_CD asc);

alter table HEATER
	add constraint FK_HEA_WCA_WEEKLY_CALENDAR foreign key (WCA_ID)
	references WEEKLY_CALENDAR (WCA_ID);

create index HEA_WCA_WEEKLY_CALENDAR_FK on HEATER (WCA_ID asc);


