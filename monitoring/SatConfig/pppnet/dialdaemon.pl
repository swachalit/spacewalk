#!/usr/bin/perl

# $Id: dialdaemon.pl,v 1.1.1.1 2002-08-20 22:58:28 cvs Exp $

$DIALSCRIPT   = "/etc/ppp/ppp-on";
$HANGUPSCRIPT = "/etc/ppp/ppp-off";
$IFCONFIG     = "/sbin/ifconfig";

$LOG_HOST     = "smon.dev.nocpulse.net";
$PING_HOST    = "";
$PORT         = 443;
$PROTO        = 'tcp';

$INTERVAL     = 30;   # seconds between checks
$TIMEOUT      = 30;		# seconds to wait for connect
$TIMEOUTMSG   = "Timed out\n";

use Socket;
use NOCpulse::Debug;
use Getopt::Long;

# Check command line options
my $debugopt;
&GetOptions('debug+' => \$debugopt);
$debugopt++ if (defined($debugopt));

# Prepare debug
my $debug = new NOCpulse::Debug;
if ($debugopt) {
  my $debugstream = $debug->addstream(LEVEL => 1);
}


# Prepare socket
my $logiaddr  = inet_aton($LOG_HOST);
my $logpaddr  = sockaddr_in($PORT, $logiaddr);
my $pingiaddr = inet_aton($PING_HOST);
my $pingpaddr = sockaddr_in($PORT, $pingiaddr);
my $proto     = getprotobyname($PROTO);


# Main loop
while(1) 
{

  my ($conn_log,$conn_ping) = 0;

  eval
  {
    local $SIG{'ALRM'} = sub {die $TIMEOUTMSG};
    alarm($TIMEOUT); 
    close(STDERR);
    my $conn_stat;

    socket(SOCK, PF_INET, SOCK_STREAM, $proto);
    $conn_log  = connect(SOCK, $logpaddr);
    close SOCK;
    if ($conn_log) { $conn_stat = 'ok'; }
    else { $conn_stat = 'failed'; }
    $debug->dprint(1, "smon connect attempt is '$conn_stat'.\n");

    socket(SOCK, PF_INET, SOCK_STREAM, $proto);
    $conn_ping = connect(SOCK, $pingpaddr);
    close SOCK;
    if ($conn_ping) { $conn_stat = 'ok'; }
    else { $conn_stat = 'failed'; }
    $debug->dprint(1, "smon-test connect attempt is '$conn_stat'.\n");
    
    open(STDERR);
    alarm(0);

  };   # end of eval block

  if ($@ && $@ eq $TIMEOUTMSG)    # a connect timed out.
  {
    $debug->dprint(1, $TIMEOUTMSG);
    if (&ppp_up()) 
    {
      $debug->dprint(1, "connect timed out...\n",
                        "  already dialed up.  going back to sleep.\n");
    } 
    else 
    {
      $debug->dprint(1, "ppp down...\n",
                        "  will attempt dialup.\n");
      system("$DIALSCRIPT");
    }
  } 
  elsif ($@)    # Something else went wrong.  What to do?
  {
    $debug->dprint(1, "this isn't good $@\n");
    alarm(0);
    die "die...i don't know why: $!\n";
  } 
  else          # No fatal errors were detected.
  {
    if (&ppp_up()) 
    {
      if ( $conn_ping )
      {
        $debug->dprint(1, "ppp is up, but can connect to $PING_HOST...\n",
                          "  gonna hangup.\n");
        system("$HANGUPSCRIPT");
      } 
      else 
      {
        $debug->dprint(1, "ppp is up, but we can't connect to $PING_HOST\n");
      }
    } 
    else 
    {
      if ( ! $conn_log and ! $conn_ping ) 
      {
        $debug->dprint(1,"can't connect to either $PING_HOST or $LOG_HOST...\n",
                         "  time to dial.\n");
        system("$DIALSCRIPT");
      } 
      else 
      {
        $debug->dprint(1, "ppp is down, and everything can connect. :-)\n",
                          "  going back to sleep.\n");
      }
    }
  }

  sleep $INTERVAL;

}

##############################################################################
###############################  Subroutines  ################################
##############################################################################

sub ppp_up
{
    my($up)=0;
    open(F, "$IFCONFIG |");
    while(<F>)
    { $up = 1 if (/^ppp\d/); }
    close F;
    $up;
}
