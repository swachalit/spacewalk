package ColdFusion::Database;

use strict;

use NOCpulse::Probe::DataSource::WindowsCommand;

my %METRIC_COUNTER = (dbhits  => 'DB Hits / Sec',
                      avgdbtime  => 'Avg DB Time (msec)',
                      );
sub run {
    my %args = @_;

    my $result = $args{result};
    my %params = %{$args{params}};

    my $command = $args{data_source_factory}->windows_command(%params);

    $command->require_version('3.0');

    my $instance = $params{instance};

    $result->context("ColdFusion Server $instance");

    foreach my $metric_name (qw(dbhits avgdbtime)) {

        my $value = $command->perf_data('ColdFusion Server', $METRIC_COUNTER{$metric_name}, $instance);

        if ($value) {
            $result->metric_value($metric_name, $value, '%.2f');
        } else {
            $result->user_data_not_found('ColdFusion Server', $instance);
            $result->context(undef);
            last;
        }
    }
}
