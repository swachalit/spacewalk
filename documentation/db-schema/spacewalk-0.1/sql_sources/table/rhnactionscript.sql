-- created by Oraschemadoc Fri Jun 13 14:05:49 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE TABLE "RHNSAT"."RHNACTIONSCRIPT" 
   (	"ID" NUMBER CONSTRAINT "RHN_ACTSCRIPT_ID_NN" NOT NULL ENABLE, 
	"ACTION_ID" NUMBER CONSTRAINT "RHN_ACTSCRIPT_AID_NN" NOT NULL ENABLE, 
	"USERNAME" VARCHAR2(32) CONSTRAINT "RHN_ACTSCRIPT_USER_NN" NOT NULL ENABLE, 
	"GROUPNAME" VARCHAR2(32) CONSTRAINT "RHN_ACTSCRIPT_GROUP_NN" NOT NULL ENABLE, 
	"SCRIPT" BLOB, 
	"TIMEOUT" NUMBER, 
	"CREATED" DATE DEFAULT (sysdate) CONSTRAINT "RHN_ACTSCRIPT_CREAT_NN" NOT NULL ENABLE, 
	"MODIFIED" DATE DEFAULT (sysdate) CONSTRAINT "RHN_ACTSCRIPT_MOD_NN" NOT NULL ENABLE, 
	 CONSTRAINT "RHN_ACTSCRIPT_ID_PK" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS"  ENABLE, 
	 CONSTRAINT "RHN_ACTSCRIPT_AID_FK" FOREIGN KEY ("ACTION_ID")
	  REFERENCES "RHNSAT"."RHNACTION" ("ID") ON DELETE CASCADE ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 32 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS" 
 LOB ("SCRIPT") STORE AS (
  TABLESPACE "DATA_TBS" ENABLE STORAGE IN ROW CHUNK 8192 PCTVERSION 10
  NOCACHE LOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)) 
 
/
