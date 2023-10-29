#!/bin/bash
###########################################################
# Retrieve some configuration details of network interfaces
###########################################################
# Outputs lines for all interfaces even if it is DOWN: interface_name,ip_address,netmask,gateway_address,mac_address,state
# Example:
#   eth0,192.168.1.6,24,192.168.1.254,aa:dd:01:23:45:67,UP
#   wlan0,192.168.1.7,24,192.168.1.254,bb:ee:89:01:23:45,DOWN
###########################################################
# Tested on:
#  - Raspbian
#  - CentOS 7
#  - OpenSuSE
###########################################################
if ! type "ifconfig" > /dev/null
then
  >&2 echo "Command ifconfig not found"
  exit 1
fi
default_gw=$(route -n | grep "^0.0.0.0" | head -n1 | awk '{print $2}')
ifconfig -a | while IFS= read -r line
do
  #echo "#$line"
  if [[ "$line" == *"flags="* ]]; then
    iface=$(echo $line | cut -d':' -f1 | xargs)
    state=$(echo $line | grep -o -P '(?<=<).*(?=)' | cut -d',' -f1 | xargs)
  else
    if [[ "$line" == *"Link encap:Ethernet"* ]]; then
      iface=$(echo $line | /bin/sed -e 's/\(.*\)Link .*$/\1/' | xargs)
      mac=$(echo $line | /bin/sed -e 's#.*HWaddr \(\)#\1#' | xargs)
    else
      if [[ "$line" == *"ether "* ]]; then
        mac=$(echo $line | cut -d' ' -f2 | xargs)
      else
        if [[ "$line" == *"inet "* ]]; then
          ip=$(echo $line | grep -o -P '(?<=inet ).*(?=netmask)' | xargs)
          netmask=$(echo $line | grep -o -P '(?<=netmask ).*(?=broadcast)' | xargs)
        fi
      fi
    fi
  fi
  if [ -z "$line" ]; then
    if [ ! -z "$iface" ] && [ ! -z "$mac" ]; then
      gw=$(route -n | grep "^0.0.0.0" | grep $iface | head -n1 | awk '{print $2}')
      if [ -f "/var/lib/dhcp/dhclient.leases" ]; then
        dhcp=$(grep $iface /var/lib/dhcp/dhclient.leases|uniq|wc -l)
        if [[ "$dhcp" -gt "0" ]]; then
          dhcp='ENABLED'
        else
          dhcp='DISABLED'
        fi
      fi
      if [ -z "$gw" ]; then
        gw=$default_gw
      fi
      if [ -z "$ip" ] && [ -z "$netmask" ]; then
        state='DOWN'
      fi
      echo "$iface,$ip,$netmask,$gw,$mac,$state,$dhcp"
    fi
    iface=
    mac=
    ip=
    gw=
    netmask=
    state=
    dhcp=
  fi
done
exit 0
