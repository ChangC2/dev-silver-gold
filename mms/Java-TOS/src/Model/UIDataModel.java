package Model;

public class UIDataModel {

    public boolean TeachMode = false;                       // 1
    public boolean AdaptiveFeedrateEnable = false;          // 2
    public boolean LeadInFeedrateEnable = false;            // 3
    public boolean MacroInterruptEnable = false;            // 4
    public boolean OptimizeToPLC = false;                   // 5
    public boolean MacroInterruptSignal = false;            // 6
    public boolean ToolWearSignal = false;                  // 7

    // From Fanuc Bridge
    public int Optimize = 0;                                // 41001    : This is monitor status, we never use Monitor status in user, so user doesn't have option to control
    public int Tool = 0;                                    // 41003
    public int Section = 0;                                 // 41005
    public int Channel = 0;                                 // 41007
    public int ProgramNumber = 0;                           // 41009
    public int InCycle = 0;                                 // 41011

    public int FeedHoldStatus = 0;                          // 41013 Check Feed Hold Status and in case of 1, should stop the elapsed timer
    public int machineConnectStatus = 0;                    // 41015 Machine (Fanuc) Connect Status 1 / 0

    public int currentSequenceNumber = 0;                   // 41017 Fanuc Machine current sequence number

    public FanucModalModel tModal = new FanucModalModel();  // 41019 ~ 41021
    public FanucModalModel sModal = new FanucModalModel();  // 41022 ~ 41024
    public FanucModalModel fModal = new FanucModalModel();  // 41025 ~ 41027
    public FanucModalModel mModal = new FanucModalModel();  // 41028 ~ 41030

    // Meta Data for indicating the source of Program Number
    public boolean isManualProgramNumber = false;

    public float HP = 0;                                    // 40001
    public float Target = 0;                                // 40002
    public float HighLimit = 0;                             // 40003
    public float WearLimit = 0;                             // 40004
    public float LowLimit = 0;                              // 40005 => Write
    public float SensorScale = 0;                           // 40006
    public float LearnedMonitorTime = 0;                    // 40007
    public float ElapsedMonitorTime = 0;                    // 40008

    public float LowLimitTimer = 0;                         // 40009

    // Meta Data for Calculating Max Low Limit Timer during teach mode
    public float MaxLowLimitTimer = 0;


    public float TotalWork = 0;                             // 40010
    public float Work = 0;                                  // 40011
    public float HighLimitDelay = 0;                        // 40012
    public float WearLimitDelay = 0;                        // 40013
    public float DefaultLowLimitErrorPercentage = 0;        // 40014
    public float PIDGain = 0;                               // 40015
    public float PIDReset = 0;                              // 40016
    public float PIDRate = 0;                               // 40017
    public float Filter = 0;                                // 40018
    public float TMHighOverTarget = 0;                      // 40019
    public float TMWearOverTarget = 0;                      // 40020
    public float TMTargetBelowPeak = 0;                     // 40021
    public float TMSensorScaleAboveHigh = 0;                // 40022
    public float LeadInTrigger = 0;                         // 40023
    public float StartDelay = 0;                            // 40024 dko8m2D*0D*bd

    public float TimeSavingsSecond = 0;                     // 40025
    public float TimeSavingsPercent = 0;                    // 40026

    public int Sensor1MobusAddress = 0;                     // 40027
    public int Sensor2MobusAddress = 0;                     // 40028
    public int Sensor3ModbusAddress = 0;                    // 40029
    public int AdaptiveFeedrateMin = 0;                     // 40030
    public int AdaptiveFeedrateMax = 0;                     // 40031
    public int AdaptiveHighLimit = 0;                       // 40032
    public int AdaptiveWearLimit = 0;                       // 40033
    public int LeadInFeedrate = 0;                          // 40034

    public float Idle = 0;                                  // 40035
    public float Feedrate = 0;                              // 40036

    // Write SensorScale                                    // 40037 => Write
    // Write Channel to PLC                                 // 40038 => Write
    public int Alarm = 0;                                   // 40039 Read/Write
    // Write Trigger to PLC                                 // 40040 => Write

    public int PLCMonitor = 0;                              // 40041 => Read
    public int PLCTool = 0;                                 // 40042 => Read
    public int PLCSection = 0;                              // 40043 => Read
    public int PLCChannel = 0;                              // 40044 => Read
    public int PLCProgramN = 0;                             // 40045 => Read

    public float SensorScaleSend = 0;                       // 40100 => Write

    public String Status = "";                              // 40200
    public String MachineName = "";                         // 40300

    public boolean fanucMacroInterruptSignal = false;       // 0 => In Fanuc Bridge Coil 1
    public boolean fanucAdaptiveEnableSignal = false;       // 1 => In Fanuc Bridge Coil 2

    public boolean fanucToolWearSignal = false;             // 41101
    public int toolnumForTheLastWearSignal = 0;             // Used for manage the wear signal

    public float HP2 = 0;                                   // Used for Rpi HP

    public UIDataModel() {}

}
