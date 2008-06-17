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

create or replace view rhnChannelFamilyServers as
	select	rs.org_id			customer_id,
		rcfm.channel_family_id		channel_family_id,
		rsc.server_id			server_id,
		rsc.created			created,
		rsc.modified			modified
	from	rhnChannelFamilyMembers		rcfm,
		rhnChannelFamily		rcf,
		rhnServerChannel		rsc,
		rhnServer			rs
	where
		rcfm.channel_id = rsc.channel_id
		and rcfm.channel_family_id = rcf.id
		and rsc.server_id = rs.id;

-- $Log$
-- Revision 1.3  2003/04/11 20:46:21  cturner
-- bugzilla: 85923.  begone purchasable flag
--
-- Revision 1.2  2002/05/15 21:30:09  pjones
-- id/log
--
