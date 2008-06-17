-- created by Oraschemadoc Fri Jun 13 14:05:53 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE TABLE "RHNSAT"."RHNPACKAGEPROVIDES" 
   (	"PACKAGE_ID" NUMBER CONSTRAINT "RHN_PKG_PROVIDES_PID_NN" NOT NULL ENABLE, 
	"CAPABILITY_ID" NUMBER CONSTRAINT "RHN_PKG_PROVIDES_CID_NN" NOT NULL ENABLE, 
	"SENSE" NUMBER DEFAULT (0) -- comes from RPMSENSE_*
                        CONSTRAINT "RHN_PKG_PROVIDES_SENSE_NN" NOT NULL ENABLE, 
	"CREATED" DATE DEFAULT (sysdate) CONSTRAINT "RHN_PKG_PROVIDES_CTIME_NN" NOT NULL ENABLE, 
	"MODIFIED" DATE DEFAULT (sysdate) CONSTRAINT "RHN_PKG_PROVIDES_MTIME_NN" NOT NULL ENABLE, 
	 CONSTRAINT "RHN_PKG_PROVIDES_PACKAGE_FK" FOREIGN KEY ("PACKAGE_ID")
	  REFERENCES "RHNSAT"."RHNPACKAGE" ("ID") ENABLE, 
	 CONSTRAINT "RHN_PKG_PROVIDES_CAPABILITY_FK" FOREIGN KEY ("CAPABILITY_ID")
	  REFERENCES "RHNSAT"."RHNPACKAGECAPABILITY" ("ID") ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 32 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS" 
 
/
