#
# Copyright (c) 2008 Red Hat, Inc.
#
# This software is licensed to you under the GNU General Public License,
# version 2 (GPLv2). There is NO WARRANTY for this software, express or
# implied, including the implied warranties of MERCHANTABILITY or FITNESS
# FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
# along with this software; if not, see
# http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
# 
# Red Hat trademarks are not licensed under GPLv2. No permission is
# granted to use or replicate Red Hat trademarks that are incorporated
# in this software or its documentation. 
#

import sys
sys.path.append("/usr/share/rhn/")

import os

from virtualization import domain_control, poller, schedule_poller

from virtualization.errors           import VirtualizationException, \
                                            UUIDError
from virtualization.constants        import StateType,               \
                                            IdentityType,            \
                                            PropertyType
from virtualization.notification     import Plan,                    \
                                            EventType,               \
                                            TargetType
from virtualization.domain_config    import DomainConfig
from virtualization.domain_directory import DomainDirectory
from virtualization.util             import dehyphenize_uuid

try:
    import libvirt
except:
    # The libvirt library is not installed.  That's ok, we can't assume it will
    # be on every system.
    libvirt = None

###############################################################################
# Public Interface
###############################################################################

def refresh():
    """
    Refreshes the virtualization info for this host and any subdomains on the
    server.
    """
    if _is_host_domain():
        domain_identity = IdentityType.HOST
        my_uuid = _fetch_host_uuid()
    else:
        # Not a host.  No-op.
        return

    # Now that we've gathered some preliminary information, create a plan of
    # actions that we will eventually pass to the server.
    plan = Plan()

    # First, declare our own existence.
    plan.add(
        EventType.EXISTS, 
        TargetType.SYSTEM, 
        { PropertyType.IDENTITY : domain_identity,
          PropertyType.UUID     : my_uuid          })

    # Now, crawl each of the domains on this host.
    domains = poller.poll_hypervisor()
    domain_list = domains.values()
    domain_uuids = domains.keys()

    domain_dir = DomainDirectory()
    domain_dir.save_unknown_domain_configs(domain_uuids)

    plan.add(EventType.CRAWL_BEGAN, TargetType.SYSTEM)
    for domain_properties in domain_list:
        plan.add(EventType.EXISTS, TargetType.DOMAIN, domain_properties)
    plan.add(EventType.CRAWL_ENDED, TargetType.SYSTEM)

    # Finally, execute the actions queued up in the plan.
    plan.execute()

def shutdown(uuid):
    """
    Shuts down the domain with the given UUID.
    """
    domain_control.shutdown(uuid)

def start(uuid):
    """
    Starts up the domain with the given UUID.
    """
    domain_control.start(uuid)

def suspend(uuid):
    """
    Suspends the domain with the given UUID.
    """
    domain_control.suspend(uuid)

def resume(uuid):
    """
    Resumes the domain with the given UUID.
    """
    domain_control.resume(uuid)

def reboot(uuid):
    """
    Reboots the domain with the given UUID.
    """
    domain_control.reboot(uuid)

def destroy(uuid):
    """
    Destroys the domain with the given UUID.
    """
    domain_control.destroy(uuid)

def setMemory(uuid, memory):
    """
    Sets the max memory usage for the domain with the given UUID.
    """
    domain_dir = DomainDirectory()
    config = domain_dir.load_config(uuid)
    config.setConfigItem(DomainConfig.MEMORY, memory)
    config.save()
    domain_control.setMemory(uuid, memory)

def setVCPUs(uuid, vcpus):
    """
    Sets the number of vcpus for the domain with the given UUID.
    """
    domain_dir = DomainDirectory()
    config = domain_dir.load_config(uuid)
    config.setConfigItem(DomainConfig.VCPU, vcpus)
    config.save()
    domain_control.setVCPUs(uuid, vcpus)

def schedulePoller(minute, hour, dom, month, dow):
    """
    Sets when poller should run.
    """
    return schedule_poller.schedule_poller(minute, hour, dom, month, dow)

###############################################################################
# Helper Routines
###############################################################################

def _is_host_domain():
    """
    This function returns true if this system is currently a host domain.  
    Simply having virtualization enabled is sufficient.

    We can figure out if Xen is running by checking for the existence of 
    /proc/xen.
    """
    return os.path.exists('/proc/xen') and libvirt

def _fetch_host_uuid():
    """
    This function returns the UUID of the host system.  This will always be 
    16 zeros.
    """
    return '0000000000000000'


###############################################################################
# Test Routine
###############################################################################

if __name__ == "__main__":
    #refresh()
    print _retrieve_virtual_domain_list()

