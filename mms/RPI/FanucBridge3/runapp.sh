#!/bin/sh
base="$(dirname "$(realpath "$0")")"
libdir=/usr/local/lib
export QT_QPA_PLATFORM=eglfs

sudo rm "$libdir/libfwlib32.so"
sudo rm "$libdir/libfwlib32.so.1"
sudo cp "$base/libfwlib32.so" "$libdir/libfwlib32.so"
sudo ldconfig

# Check if the libraries are already installed
if ! ldconfig -p | grep -q libQt5WebSockets.so.5 ; then
    # Install libQtWebSockets package
    sudo apt-get install libqt5websockets5 -y
fi

if ! ldconfig -p | grep -q libQt5SerialBus.so.5 ; then
    # Install libQtSerialBus package
    # Replace "YOUR_PACKAGE_MANAGER" with the appropriate package manager on your system (e.g., apt-get, yum, pacman)
    sudo apt-get install libqt5serialbus5 -y
fi

if ! ldconfig -p | grep -q libqt5gui5 ; then
    # Install libQtWebSockets package
    sudo apt-get install libqt5gui5 -y
fi

sudo ./FanucBridge3



