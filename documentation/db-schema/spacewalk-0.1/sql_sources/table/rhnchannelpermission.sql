-- created by Oraschemadoc Fri Jun 13 14:05:50 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE TABLE "RHNSAT"."RHNCHANNELPERMISSION" 
   (	"CHANNEL_ID" NUMBER CONSTRAINT "RHN_CPERM_CID_NN" NOT NULL ENABLE, 
	"USER_ID" NUMBER CONSTRAINT "RHN_CPERM_UID_NN" NOT NULL ENABLE, 
	"ROLE_ID" NUMBER CONSTRAINT "RHN_CPERM_RID_NN" NOT NULL ENABLE, 
	"CREATED" DATE DEFAULT (sysdate) CONSTRAINT "RHN_CPERM_CREATED_NN" NOT NULL ENABLE, 
	"MODIFIED" DATE DEFAULT (sysdate) CONSTRAINT "RHN_CPERM_MODIFIED_NN" NOT NULL ENABLE, 
	 CONSTRAINT "RHN_CPERM_CIDFFK" FOREIGN KEY ("CHANNEL_ID")
	  REFERENCES "RHNSAT"."RHNCHANNEL" ("ID") ON DELETE CASCADE ENABLE, 
	 CONSTRAINT "RHN_CPERM_UID_FK" FOREIGN KEY ("USER_ID")
	  REFERENCES "RHNSAT"."WEB_CONTACT" ("ID") ON DELETE CASCADE ENABLE, 
	 CONSTRAINT "RHN_CPERM_RID_FK" FOREIGN KEY ("ROLE_ID")
	  REFERENCES "RHNSAT"."RHNCHANNELPERMISSIONROLE" ("ID") ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 32 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS" 
 
/
