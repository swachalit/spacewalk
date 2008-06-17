-- created by Oraschemadoc Fri Jun 13 14:05:57 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE TABLE "RHNSAT"."RHN_CONFIG_PARAMETER" 
   (	"GROUP_NAME" VARCHAR2(255) CONSTRAINT "RHN_CONFP_GROUP_NAME_NN" NOT NULL ENABLE, 
	"NAME" VARCHAR2(255) CONSTRAINT "RHN_CONFP_NAME_NN" NOT NULL ENABLE, 
	"VALUE" VARCHAR2(255), 
	"SECURITY_TYPE" VARCHAR2(255) CONSTRAINT "RHN_CONFP_SECURITY_TYPE_NN" NOT NULL ENABLE, 
	"LAST_UPDATE_USER" VARCHAR2(40), 
	"LAST_UPDATE_DATE" DATE, 
	 CONSTRAINT "RHN_CONFP_GROUP_NAME_NAME_PK" PRIMARY KEY ("GROUP_NAME", "NAME")
  USING INDEX PCTFREE 10 INITRANS 32 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS"  ENABLE, 
	 CONSTRAINT "RHN_CONFP_GRPNM_GROUP_NAME_FK" FOREIGN KEY ("GROUP_NAME")
	  REFERENCES "RHNSAT"."RHN_CONFIG_GROUP" ("NAME") ENABLE, 
	 CONSTRAINT "RHN_CONFP_SCRTY_SEC_TYPE_FK" FOREIGN KEY ("SECURITY_TYPE")
	  REFERENCES "RHNSAT"."RHN_CONFIG_SECURITY_TYPE" ("NAME") ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 32 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS" 
 
/
