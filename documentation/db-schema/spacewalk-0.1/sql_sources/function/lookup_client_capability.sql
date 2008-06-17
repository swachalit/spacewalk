-- created by Oraschemadoc Fri Jun 13 14:06:11 2008
-- visit http://www.yarpen.cz/oraschemadoc/ for more info

  CREATE OR REPLACE FUNCTION "RHNSAT"."LOOKUP_CLIENT_CAPABILITY" (name_in IN VARCHAR2)
RETURN NUMBER
DETERMINISTIC
IS
	PRAGMA AUTONOMOUS_TRANSACTION;
	cap_name_id		NUMBER;
BEGIN
	SELECT id
          INTO cap_name_id
          FROM rhnClientCapabilityName
         WHERE name = name_in;
	RETURN cap_name_id;
EXCEPTION
        WHEN NO_DATA_FOUND THEN
            INSERT INTO rhnClientCapabilityName (id, name)
                VALUES (rhn_client_capname_id_seq.nextval, name_in)
                RETURNING id INTO cap_name_id;
            COMMIT;
	RETURN cap_name_id;
END;
 
/
