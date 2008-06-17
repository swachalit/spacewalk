# Macros

%define cvs_package SatConfig/autokick
%define install_prefix /etc/rc.d/np.d

# Package specific stuff
Name:         SatConfig-autokick
Source9999: version
Version: %(echo `awk '{ print $1 }' %{SOURCE9999}`)
Release: %(echo `awk '{ print $2 }' %{SOURCE9999}`)
Summary:      Configures a satellite from a DHCP query
Source:	      %name-%PACKAGE_VERSION.tar.gz
BuildArch:    noarch
Group:        unsorted
Copyright:    Red Hat, Inc (c) 2001-2003
Vendor:       Red Hat, Inc.
Prefix:	      %install_prefix
Buildroot:    %{_tmppath}/%cvs_package

#Prereq:     perl SatConfig-cluster np-config /bin/mount /sbin/ifconfig ld.so kernel kernel-utils etcskel dev libc libnet losetup util-linux


%description

Configures SatConfig-general and np-config stuff during a kickstart

%prep
%setup

%build

%install
mkdir -p $RPM_BUILD_ROOT%install_prefix
install -o root -g root -m 700 autokick $RPM_BUILD_ROOT%install_prefix

%point_scripts_to_correct_perl
%make_file_list 

%files -f %{name}-%{version}-%{release}-filelist


%clean
%abstract_clean_script
