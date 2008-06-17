-- created by Oraschemadoc Fri Jun 13 14:05:52 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE TABLE "RHNSAT"."RHNKICKSTARTCOMMANDNAME" 
   (	"ID" NUMBER CONSTRAINT "RHN_KSCOMMANDNAME_ID_NN" NOT NULL ENABLE, 
	"NAME" VARCHAR2(128) CONSTRAINT "RHN_KSCOMMAND_NAME_NN" NOT NULL ENABLE, 
	"USES_ARGUMENTS" CHAR(1) CONSTRAINT "RHN_KSCOMMANDNAME_USES_ARGS_NN" NOT NULL ENABLE, 
	"SORT_ORDER" NUMBER CONSTRAINT "RHN_KSCOMMANDNAME_SORTORDR_NN" NOT NULL ENABLE, 
	"REQUIRED" CHAR(1) DEFAULT 'N' CONSTRAINT "RHN_KSCOMMANDNAME_REQRD_NN" NOT NULL ENABLE, 
	 CONSTRAINT "RHN_KSCOMMANDNAME_USES_ARGS_CK" CHECK (uses_arguments in ('Y','N')) ENABLE, 
	 CONSTRAINT "RHN_KSCOMMANDNAME_REQRD_CK" CHECK ( required in ('Y', 'N') ) ENABLE, 
	 CONSTRAINT "RHN_KSCOMMANDNAME_ID_PK" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS"  ENABLE, 
	 CONSTRAINT "RHN_KSCOMMANDNAME_NAME_UQ" UNIQUE ("NAME")
  USING INDEX PCTFREE 10 INITRANS 32 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS"  ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 32 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS" 
 
/
