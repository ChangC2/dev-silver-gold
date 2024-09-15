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

if dpkg -l | grep -q '^ii  libqt5serialbus5 '; then
    echo "Package libqt5serialbus5 is already installed."
else
    echo "Package libqt5serialbus5 is not installed. Installing now..."
    sudo apt-get install libqt5serialbus5 -y
fi

if dpkg -l | grep -q '^ii  libqt5serialport5 '; then
    echo "Package libqt5serialport5 is already installed."
else
    echo "Package libqt5serialport5 is not installed. Installing now..."
    sudo apt-get install libqt5serialport5 -y
fi

if dpkg -l | grep -q '^ii  libqt5websockets5 '; then
    echo "Package libqt5websockets5 is already installed."
else
    echo "Package libqt5websockets5 is not installed. Installing now..."
    sudo apt-get install libqt5websockets5 -y
fi

if dpkg -l | grep -q '^ii  libqt5widgets5 '; then
    echo "Package libqt5widgets5 is already installed."
else
    echo "Package libqt5widgets5 is not installed. Installing now..."
    sudo apt-get install libqt5widgets5 -y
fi

sudo chmod +x ./FanucBridge3

# Variables
APP_NAME="FanucBridge3"
COMMENT="FanucBridge3"
ICON_NAME="assets/favicon.png"  # Your icon file name
EXEC_NAME="FanucBridge3"  # Your application's executable file name
TERMINAL=false
CATEGORY="Utility"
SERVICE_NAME="start-fanucbridge3"

# Get current username and directory of the script
if [ "$SUDO_USER" ]; then
  CURRENT_USER=$SUDO_USER
else
  CURRENT_USER=$(whoami)
fi
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Define paths
ICON_PATH="${SCRIPT_DIR}/${ICON_NAME}"
EXEC_PATH="${SCRIPT_DIR}/${EXEC_NAME}"
DESKTOP_DIR="/home/${CURRENT_USER}/Desktop"

# Create the .desktop file on the Desktop
cat > "${DESKTOP_DIR}/${APP_NAME}.desktop" <<EOL
[Desktop Entry]
Type=Application
Name=${APP_NAME}
Comment=${COMMENT}
Icon=${ICON_PATH}
Exec=${EXEC_PATH}
Terminal=${TERMINAL}
Categories=${CATEGORY};
EOL

# Update permissions to ensure the shortcut is executable
chmod +x "${DESKTOP_DIR}/${APP_NAME}.desktop"

# Create the systemd service file
sudo tee /etc/systemd/system/${SERVICE_NAME}.service > /dev/null <<EOL
[Unit]
Description=Start ${APP_NAME} at Startup
After=network.target

[Service]
ExecStart=${EXEC_PATH}
WorkingDirectory=${SCRIPT_DIR}
User=${CURRENT_USER}
Environment=DISPLAY=:0
Environment=QT_QPA_PLATFORM=xcb
Restart=on-failure
RestartSec=5s
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=FanucBridge3

[Install]
WantedBy=multi-user.target
EOL

# Reload systemd to recognize the new service and enable it
sudo systemctl daemon-reload
sudo systemctl enable ${SERVICE_NAME}.service

# Optionally start the service immediately
sudo systemctl start ${SERVICE_NAME}.service

# Notify the user that the shortcut and service have been created
echo "Shortcut for ${APP_NAME} created on the Desktop and startup service configured."