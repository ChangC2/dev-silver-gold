1. Open the terminal on project folder

2. Make the folder permission executable
sudo chmod -R 777 ./

3. Check if the RPI architecture is armv7 or not.
command: uname -m

4. Run the setup sh file
if RPI is armv7
    sudo ./setup_armv7.sh
if not
    sudo ./setup.sh