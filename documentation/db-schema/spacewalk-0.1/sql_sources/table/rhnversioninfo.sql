-- created by Oraschemadoc Fri Jun 13 14:05:56 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE TABLE "RHNSAT"."RHNVERSIONINFO" 
   (	"LABEL" VARCHAR2(64) CONSTRAINT "RHN_VERSIONINFO_LABEL_NN" NOT NULL ENABLE, 
	"NAME_ID" NUMBER CONSTRAINT "RHN_VERSIONINFO_NID_NN" NOT NULL ENABLE, 
	"EVR_ID" NUMBER CONSTRAINT "RHN_VERSIONINFO_EID_NN" NOT NULL ENABLE, 
	"CREATED" DATE DEFAULT (sysdate) CONSTRAINT "RHN_VERSIONINFO_CREATED_NN" NOT NULL ENABLE, 
	"MODIFIED" DATE DEFAULT (sysdate) CONSTRAINT "RHN_VERSIONINFO_MODIFIED_NN" NOT NULL ENABLE, 
	 CONSTRAINT "RHN_VERSIONINFO_NID_FK" FOREIGN KEY ("NAME_ID")
	  REFERENCES "RHNSAT"."RHNPACKAGENAME" ("ID") ENABLE, 
	 CONSTRAINT "RHN_VERSIONINFO_EID_FK" FOREIGN KEY ("EVR_ID")
	  REFERENCES "RHNSAT"."RHNPACKAGEEVR" ("ID") ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 32 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS" 
 
/
