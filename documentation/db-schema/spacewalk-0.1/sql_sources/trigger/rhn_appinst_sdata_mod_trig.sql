-- created by Oraschemadoc Fri Jun 13 14:06:09 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE OR REPLACE TRIGGER "RHNSAT"."RHN_APPINST_SDATA_MOD_TRIG" 
before insert or update on rhnAppInstallSessionData
for each row
begin
	:new.modified := sysdate;
end rhn_appinst_sdata_mod_trig;
ALTER TRIGGER "RHNSAT"."RHN_APPINST_SDATA_MOD_TRIG" ENABLE
 
/
