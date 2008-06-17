-- created by Oraschemadoc Fri Jun 13 14:05:52 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE TABLE "RHNSAT"."RHNFAQCLASS" 
   (	"ID" NUMBER CONSTRAINT "RHN_FAQ_CLASS_ID_NN" NOT NULL ENABLE, 
	"NAME" VARCHAR2(128), 
	"LABEL" VARCHAR2(32) CONSTRAINT "RHN_FAQ_CLASS_LABEL_NN" NOT NULL ENABLE, 
	"ORDERING" NUMBER CONSTRAINT "RHN_FAQ_CLASS_OR_NN" NOT NULL ENABLE, 
	 CONSTRAINT "RHN_FAQ_CLASS_ID_PK" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS"  ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 32 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS" 
 
/
