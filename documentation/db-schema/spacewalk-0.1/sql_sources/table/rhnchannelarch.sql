-- created by Oraschemadoc Fri Jun 13 14:05:50 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE TABLE "RHNSAT"."RHNCHANNELARCH" 
   (	"ID" NUMBER CONSTRAINT "RHN_CARCH_ID_NN" NOT NULL ENABLE, 
	"LABEL" VARCHAR2(64) CONSTRAINT "RHN_CARCH_LABEL_NN" NOT NULL ENABLE, 
	"ARCH_TYPE_ID" NUMBER CONSTRAINT "RHN_CARCH_ATID_NN" NOT NULL ENABLE, 
	"NAME" VARCHAR2(64) CONSTRAINT "RHN_CARCH_NAME_NN" NOT NULL ENABLE, 
	"CREATED" DATE DEFAULT (sysdate) CONSTRAINT "RHN_CARCH_CREATED_NN" NOT NULL ENABLE, 
	"MODIFIED" DATE DEFAULT (sysdate) CONSTRAINT "RHN_CARCH_MODIFIED_NN" NOT NULL ENABLE, 
	 CONSTRAINT "RHN_CARCH_ID_PK" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 32 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS"  ENABLE, 
	 CONSTRAINT "RHN_CARCH_LABEL_UQ" UNIQUE ("LABEL")
  USING INDEX PCTFREE 10 INITRANS 32 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS"  ENABLE, 
	 CONSTRAINT "RHN_CARCH_ATID_FK" FOREIGN KEY ("ARCH_TYPE_ID")
	  REFERENCES "RHNSAT"."RHNARCHTYPE" ("ID") ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 32 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS" 
 
/
