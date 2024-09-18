#!/bin/sh
base="$(dirname "$(realpath "$0")")"
libdir=/usr/local/lib

sudo cp "$base/libfwlib32-linux-armv7.so" "$libdir/libfwlib32.so"
sudo ldconfig

# Function to check if updates are available
are_updates_available() {
    apt list --upgradable 2> /dev/null | grep -q upgradable
}
# Update system package information if updates are available
if are_updates_available; then
    echo "Updating package information..."
    sudo apt-get update
    # Perform system upgrade only if updates are available
    echo "Upgrading packages..."
    sudo apt upgrade -y
else
    echo "No package updates available."
fi

# Check and install qemu qemu-user qemu-user-static if not installed
if ! ldconfig -p | grep -q qemu qemu-user qemu-user-static ; then
    sudo apt-get install qemu qemu-user qemu-user-static -y
fi

if dpkg --print-foreign-architectures | grep -q "^armhf$"; then
    echo "The armhf architecture is already added."
else
    echo "Adding the armhf architecture."
    sudo dpkg --add-architecture armhf
    sudo apt-get update
fi

# Check and install qtcreator if not installed
if ! ldconfig -p | grep -q qtcreator ; then
    sudo apt-get install qtcreator -y
fi

# Check and install libQtWebSockets if not installed
if ! ldconfig -p | grep -q libQt5WebSockets.so.5 ; then
    # Install libQtWebSockets package
    sudo apt-get install libqt5websockets5 -y
    sudo apt-get install libqt5websockets5:armhf -y
fi

# Check and install libQtSerialBus if not installed
if ! ldconfig -p | grep -q libQt5SerialBus.so.5 ; then
    sudo apt-get install libqt5serialbus5 -y
    sudo apt-get install libqt5serialbus5:armhf -y
fi

# Check and install libQtSerialBus if not installed
if ! ldconfig -p | grep -q libqt5serialport5.so.5 ; then
    sudo apt-get install libqt5serialport5 -y
    sudo apt-get install libqt5serialport5:armhf -y
fi

# Check and install libQtSerialBus if not installed
if ! ldconfig -p | grep -q libqt5widgets5.so.5 ; then
    sudo apt-get install libqt5widgets5 -y
    sudo apt-get install libqt5widgets5:armhf -y
fi

sudo ./FanucBridge2

