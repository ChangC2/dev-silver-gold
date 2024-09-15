#include "libs.h"
#include "webpage.h"
#include "vars.h"
#include "digital_read.h"
#include "common_funcs.h"
#include "ethernet_webserver_funcs.h"

void setup() {
  isRestartedOnMidnight = true;
  Serial.begin(115200);
  setDigitalPins();
  loadPref();
  initWiFi();
  checkOTAUpdate();  // OTA update and get Sensor details
  setupEthernetWebserver();
  initAd7091();
  delay(1000);
}

void loop() {
  checkWiFi();
  loopEthernetWebserver();
  calc_ADC();
  /////////////////////// Api Call For Gantt and Iot Data Per 5 seconds////////////////////////////////
  passedTime = passedTime + 1;
  if (passedTime % 10 == 0) {
    checkMidnight();
    Serial.printf("%d Sec\n", (int)passedTime / 10);
  }
  if (passedTime > readingCycle * 10) {
    postGanttData();
    passedTime = 0;
  }
  delay(100);
  /////////////////////////////////////////////////////////////////////////////////////
}

void initAd7091() {
  pinMode(AD7091R8_CONVST, OUTPUT);  //
  delay(100);                        // Wait untill reference capatitor fully charged.
  adc.AD7091R8_SPI_Configuration();
  adc.Ad7091_soft_reset();

  adc.writeAd7091(CONFIG, 0x0271);  // Active Software Reset, BUSY output, CMOS output,
  adc.writeAd7091(ADC_CH, 0x00ff);  // ADC channel 0
  adc.writeAd7091(ADC_RES, 0x00);   // Initialize ADC result register
  adc.writeAd7091(CH0L_LIM, 0x0000);
  adc.writeAd7091(CH0H_LIM, 0x01ff);
  adc.writeAd7091(CH0_HYS, 0x01ff);

  adc.writeAd7091(CH1L_LIM, 0x0000);
  adc.writeAd7091(CH1H_LIM, 0x01ff);
  adc.writeAd7091(CH1_HYS, 0x01ff);

  adc.writeAd7091(CH2L_LIM, 0x0000);
  adc.writeAd7091(CH2H_LIM, 0x01ff);
  adc.writeAd7091(CH2_HYS, 0x01ff);

  adc.writeAd7091(CH3L_LIM, 0x0000);
  adc.writeAd7091(CH3H_LIM, 0x01ff);
  adc.writeAd7091(CH3_HYS, 0x01ff);

  Serial.println("Written Register values checking.....  ");
  adc.readAd7091(CONFIG);
  adc.readAd7091(ADC_CH);
  adc.readAd7091(CH0H_LIM);
  Serial.println("Written Register values checking.....  ");
}

void calc_ADC() {
  adc.Adc_Conv();
  adc_temp = adc.readAd7091(0);
  int ch_num = (adc_temp & 0xF000) >> 13;
  int adc_val = (adc_temp & 0x0FFF);
  // Serial.printf("ADC_TEMP= %x\r\n", adc_temp);
  // Serial.printf("CH_NUM = %d\r\n", ch_num);
  // Serial.printf("ADC_VAL = %x\r\n", adc_val);

  switch (ch_num) {
    case 0:
      vin[3] = adc_val;
      break;
    case 1:
      ain[0] = adc_val;
      break;
    case 2:
      ain[1] = adc_val;
      break;
    case 3:
      ain[2] = adc_val;
      break;
    case 4:
      ain[3] = adc_val;
      break;
    case 5:
      vin[0] = adc_val;
      break;
    case 6:
      vin[1] = adc_val;
      break;
    case 7:
      vin[2] = adc_val;
      break;
  }
  // Calculate Vin voltage in [V]
  for (int i = 0; i < 4; i++) {
    vin_val[i] = ((vin[i] * Vcoef) - voff[i]);
    if (vin_val[i] < 0) {
      vin_val[i] = 0;
    }
    // Serial.printf("Vin[%d] = %x\r\n", i, vin[i]);
    // Serial.printf("Vin_val[%d] = %.2f\r\n", i + 1, vin_val[i]);
  }
  // Calculate Ain current in [mA]
  for (int i = 0; i < 4; i++) {
    ain_val[i] = ((ain[i] * Acoef) - aoff[i]);
    if (ain_val[i] < 0) {
      ain_val[i] = 0;
    }
    // Serial.printf("Ain[%d] = %x\r\n", i, ain[i]);
    // Serial.printf("Ain_val[%d] = %.2f\r\n", i + 1, ain_val[i]);
  }
}

String getValue(String data, char separator, int index) {
  int found = 0;
  int strIndex[] = {
    0, -1
  };
  int maxIndex = data.length() - 1;
  for (int i = 0; i <= maxIndex && found <= index; i++) {
    if (data.charAt(i) == separator || i == maxIndex) {
      found++;
      strIndex[0] = strIndex[1] + 1;
      strIndex[1] = (i == maxIndex) ? i + 1 : i;
    }
  }
  return found > index ? data.substring(strIndex[0], strIndex[1]) : "";

  delay(250);
}


