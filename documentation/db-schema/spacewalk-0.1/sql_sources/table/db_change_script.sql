-- created by Oraschemadoc Fri Jun 13 14:05:49 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE TABLE "RHNSAT"."DB_CHANGE_SCRIPT" 
   (	"BUG_ID" NUMBER CONSTRAINT "DC_SCRIPT_BID_NN" NOT NULL ENABLE, 
	"SEQ_NO" NUMBER CONSTRAINT "DC_SCRIPT_SN_NN" NOT NULL ENABLE, 
	"OWNER" VARCHAR2(255) CONSTRAINT "DC_SCRIPT_OWNER_NN" NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(4000), 
	"RELEASE" VARCHAR2(255), 
	"RUN_AS" VARCHAR2(255), 
	"EXPECT_FAIL" CHAR(1) DEFAULT 0, 
	 CONSTRAINT "DC_SCRIPT_BID_SN_PK" PRIMARY KEY ("BUG_ID", "SEQ_NO")
  USING INDEX PCTFREE 10 INITRANS 32 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS"  ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS" 
 
/
