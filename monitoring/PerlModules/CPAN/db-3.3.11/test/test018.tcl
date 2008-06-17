# See the file LICENSE for redistribution information.
#
# Copyright (c) 1996-2001
#	Sleepycat Software.  All rights reserved.
#
# $Id: test018.tcl,v 1.1.1.1 2002-01-11 00:21:39 apingel Exp $
#
# DB Test 18 {access method}
# Run duplicates with small page size so that we test off page duplicates.
proc test018 { method {nentries 10000} args} {
	puts "Test018: Off page duplicate tests"
	eval {test011 $method $nentries 19 18 -pagesize 512} $args
}
