--
-- Copyright (c) 2008 Red Hat, Inc.
--
-- This software is licensed to you under the GNU General Public License,
-- version 2 (GPLv2). There is NO WARRANTY for this software, express or
-- implied, including the implied warranties of MERCHANTABILITY or FITNESS
-- FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
-- along with this software; if not, see
-- http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
-- 
-- Red Hat trademarks are not licensed under GPLv2. No permission is
-- granted to use or replicate Red Hat trademarks that are incorporated
-- in this software or its documentation. 
--
--
-- $Id$
--

create or replace function
is_user_org_admin
(
    user_id_in IN number
)
return number
is
    org_id_val		number;
    app_group_val	number;
    org_admin           number;
begin
    select org_id into org_id_val
      from web_contact
     where id = user_id_in;

    select id into app_group_val
      from rhnUserGroup
     where org_id = org_id_val
       and group_type = (
		select	id
		from	rhnUserGroupType
		where	label = 'org_admin'
	   );
       
    select count(1) into org_admin
      from rhnUserGroupMembers
     where user_group_id = app_group_val
       and user_id = user_id_in;
       
   return org_admin;
end;
/
show errors

-- $Log$
-- Revision 1.4  2002/05/13 22:53:38  pjones
-- cvs id/log
-- some (note enough) readability fixes
--
