/*
  AD7091R8.cpp - 
*/

#include <Arduino.h>
#include <SPI.h>
#include "AD7091R.h"
SPIClass * hspi = NULL;
AD7091R8class AD7091R8;

void AD7091R8class::AD7091R8_SPI_Configuration(void)
{	
	// Initialise second instance of the SPIClass for ADC IC

    hspi = new SPIClass(HSPI);
    hspi->begin(HSPI_SCLK, HSPI_MISO, HSPI_MOSI, HSPI_SS); //SCLK, MISO, MOSI, SS
    pinMode(HSPI_SS, OUTPUT); //HSPI SS	
    
	delay(100);
}

void AD7091R8class::Adc_Conv()
{
	digitalWrite(AD7091R8_CONVST, LOW);
  delayMicroseconds(5);
  digitalWrite(AD7091R8_CONVST, HIGH);
  delayMicroseconds(2);
}


uint16_t AD7091R8class::spiCommand(uint16_t data)
{
  uint16_t recvedVal16;
  hspi->beginTransaction(SPISettings(10000, MSBFIRST, SPI_MODE0));
  digitalWrite(AD7091R8_SS, LOW); //pull SS slow to prep other end for transfer
  recvedVal16 = hspi->transfer16(data);
  digitalWrite(AD7091R8_SS, HIGH); //pull ss high to signify end of data transfer
  hspi->endTransaction();
  return recvedVal16;
}
uint8_t AD7091R8class::spiCommand8(uint8_t data)
{
  uint8_t recvedVal8;
  hspi->beginTransaction(SPISettings(10000, MSBFIRST, SPI_MODE0));
 // digitalWrite(AD7091R8_SS, LOW); //pull SS slow to prep other end for transfer
  recvedVal8 = hspi->transfer(data);
  //digitalWrite(AD7091R8_SS, HIGH); //pull ss high to signify end of data transfer
  hspi->endTransaction();
  return recvedVal8;
}

uint16_t AD7091R8class::readAd7091 (uint16_t ui16address)
{	
	uint16_t RegVal = 0;    // 16bit Register value;
	
	
//	digitalWrite(AD7091R8_SS,LOW);
	spiCommand(ui16address << 11);
	RegVal = spiCommand(0x00);
//	digitalWrite(AD7091R8_SS,HIGH);
	// Serial.print("ADC Data Register Read : ");  //Debug serial prints
	// Serial.printf("0x%4x\r\n", RegVal);	
	return RegVal;
	
}

void AD7091R8class::writeAd7091 (uint16_t ui16address, uint16_t ui16value)
{
	
//	digitalWrite(AD7091R8_SS,LOW);
	spiCommand(ui16address << 11 | 0b10000000000 | ui16value);	
//	digitalWrite(AD7091R8_SS,HIGH);	
	
	// Serial.print("ADC Register Write ");		//Debug serial prints
	// Serial.printf("0x%2x  ", ui16address);	
	// Serial.print("With Register Value ");
	// Serial.printf("0x%4x ", ui16value);
	// Serial.println("Written");	
	
	//Serial.print(ui16address << 10 | 0b10000000000 | ui16value);
}

void AD7091R8class::Ad7091_soft_reset()
{
	for (int i=0; i<67; i++)
	{
		  digitalWrite(AD7091R8_CONVST, LOW);
  		delayMicroseconds(2);
  		digitalWrite(AD7091R8_CONVST, HIGH);
  		delayMicroseconds(2);	
	}	
}
