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
--$Id$
--
--

--reference table
--redirect_types current prod row count = 4
create table 
rhn_redirect_types
(
    name            varchar2 (20)
        constraint rhn_rdrtp_name_nn not null
        constraint rhn_rdrtp_name_pk primary key
            using index tablespace [[64k_tbs]]
            storage( pctincrease 1 freelists 16 )
            initrans 32,
    description     varchar2 (255),
    long_name       varchar2 (80)
)
    storage ( freelists 16 )
    initrans 32;

comment on table rhn_redirect_types 
    is 'rdrtp  redirect types';

create sequence rhn_redirect_types_recid_seq;

--$Log$
--Revision 1.2  2004/04/16 21:49:57  kja
--Adjusted small table sizes.  Documented small tables that are primarily static
--as "reference tables."  Fixed up a few syntactical errs.
--
--Revision 1.1  2004/04/16 02:21:57  kja
--More monitoring schema.
--
