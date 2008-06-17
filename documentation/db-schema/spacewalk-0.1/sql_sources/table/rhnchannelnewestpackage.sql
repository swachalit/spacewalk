-- created by Oraschemadoc Fri Jun 13 14:05:50 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE TABLE "RHNSAT"."RHNCHANNELNEWESTPACKAGE" 
   (	"CHANNEL_ID" NUMBER CONSTRAINT "RHN_CNP_CID_NN" NOT NULL ENABLE, 
	"NAME_ID" NUMBER CONSTRAINT "RHN_CNP_NID_NN" NOT NULL ENABLE, 
	"EVR_ID" NUMBER CONSTRAINT "RHN_CNP_EID_NN" NOT NULL ENABLE, 
	"PACKAGE_ARCH_ID" NUMBER CONSTRAINT "RHN_CNP_PAID_NN" NOT NULL ENABLE, 
	"PACKAGE_ID" NUMBER CONSTRAINT "RHN_CNP_PID_NN" NOT NULL ENABLE, 
	 CONSTRAINT "RHN_CNP_CID_NID_UQ" UNIQUE ("CHANNEL_ID", "NAME_ID", "PACKAGE_ARCH_ID")
  USING INDEX PCTFREE 10 INITRANS 32 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS"  ENABLE, 
	 CONSTRAINT "RHN_CNP_CID_FK" FOREIGN KEY ("CHANNEL_ID")
	  REFERENCES "RHNSAT"."RHNCHANNEL" ("ID") ON DELETE CASCADE ENABLE, 
	 CONSTRAINT "RHN_CNP_NID_FK" FOREIGN KEY ("NAME_ID")
	  REFERENCES "RHNSAT"."RHNPACKAGENAME" ("ID") ENABLE, 
	 CONSTRAINT "RHN_CNP_EID_FK" FOREIGN KEY ("EVR_ID")
	  REFERENCES "RHNSAT"."RHNPACKAGEEVR" ("ID") ENABLE, 
	 CONSTRAINT "RHN_CNP_PAID_FK" FOREIGN KEY ("PACKAGE_ARCH_ID")
	  REFERENCES "RHNSAT"."RHNPACKAGEARCH" ("ID") ENABLE, 
	 CONSTRAINT "RHN_CNP_PID_FK" FOREIGN KEY ("PACKAGE_ID")
	  REFERENCES "RHNSAT"."RHNPACKAGE" ("ID") ON DELETE CASCADE ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 32 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS" 
 
/
