#!/bin/bash
###########################################################
# Retrieve some configuration details of network interfaces
###########################################################
# Outputs lines for all interfaces even if it is DOWN: interface_name,ip_address,netmask,gateway_address,mac_address,state,dhcp_active
# Example :
#   eth0,192.168.1.6,24,192.168.1.254,aa:dd:01:23:45:67,UP,ENABLED
#   wlan0,192.168.1.7,24,192.168.1.254,bb:ee:89:01:23:45,DOWN,DISABLED
###########################################################
# Tested on:
#  - Raspbian
#  - CentOS 7
#  - OpenSuSE
###########################################################
if ! type "ip" > /dev/null
then
  >&2 echo "Command ip not found"
  exit 1
fi
cdr2mask() {
  # Number of args to shift, 255..255, first non-255 byte, zeroes
  set -- $(( 5 - ($1 / 8) )) 255 255 255 255 $(( (255 << (8 - ($1 % 8))) & 255 )) 0 0 0
  [ $1 -gt 1 ] && shift $1 || shift
  netmask=${1-0}.${2-0}.${3-0}.${4-0}
}
default_gw=$(ip route show | grep -i default | head -n1 | cut -d' ' -f3 | xargs)
ip addr show | xargs | sed 's/ \([0-9]*: \)/\n\1/g' | while IFS= read -r line
do
  if [[ $line == *"state"* ]]; then
    iface=$(echo $line | cut -d':' -f2 | xargs)
    state=$(echo $line | grep -i -o -P '(?<=state ).*(?=)' | cut -d' ' -f1 | xargs)
    gw=$(ip route show | grep -i default | grep -i $iface | head -n1 | cut -d' ' -f3 | xargs)
    if [ -z "$gw" ]; then
      gw=$default_gw
    fi
  fi
  if [[ $line == *"ether "* ]]; then
    mac=$(echo $line | grep -i -o -P '(?<=ether ).*(?= brd)' | cut -d' ' -f1 | xargs)
  fi
  if [[ $line == *"inet "* ]] && [[ $line != *"peer "* ]]; then
    ip=$(echo $line | grep -i -o -P '(?<=inet ).*(?= brd)' | cut -d'/' -f1 | xargs)
    netmask=$(echo $line | grep -i  -o -P '(?<=inet ).*(?= brd)' | cut -d'/' -f2 | xargs)
    if [ ! -z "$netmask" ]; then
      cdr2mask $netmask
    fi
  fi
  dhcp='DISABLED'
  if [ ! -z "$iface" ] && [ ! -z "$mac" ]; then
    if [[ $(ip a | grep -i $iface | grep -i dynamic | wc -l) -ge "1" ]]; then
      dhcp='ENABLED'
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
done
exit 0
