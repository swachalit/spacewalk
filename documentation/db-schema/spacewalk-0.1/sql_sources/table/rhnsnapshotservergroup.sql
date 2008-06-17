-- created by Oraschemadoc Fri Jun 13 14:05:55 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE TABLE "RHNSAT"."RHNSNAPSHOTSERVERGROUP" 
   (	"SNAPSHOT_ID" NUMBER CONSTRAINT "RHN_SNAPSHOTSG_SID_NN" NOT NULL ENABLE, 
	"SERVER_GROUP_ID" NUMBER CONSTRAINT "RHN_SNAPSHOTSG_SGID_NN" NOT NULL ENABLE, 
	 CONSTRAINT "RHN_SNAPSHOTSG_SID_FK" FOREIGN KEY ("SNAPSHOT_ID")
	  REFERENCES "RHNSAT"."RHNSNAPSHOT" ("ID") ON DELETE CASCADE ENABLE, 
	 CONSTRAINT "RHN_SNAPSHOTSG_SGID_FK" FOREIGN KEY ("SERVER_GROUP_ID")
	  REFERENCES "RHNSAT"."RHNSERVERGROUP" ("ID") ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 32 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 16 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "DATA_TBS" 
 
/
