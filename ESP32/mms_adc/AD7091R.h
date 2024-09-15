/*
  AD7091.h - 
*/
#ifndef AD7091_h
#define AD7091_h

#include <Arduino.h>

/*****************************************************************************/
/************************** ADC Address Definitions **************************/
/*****************************************************************************/

//ADC Write Commands
#define MODE_WRITE			0x10					// Write to the Mode Register
#define FILTER_WRITE		0x20					// Write to the Filter Register

//ADC Read Commands
#define STATUS_READ			0x08					// Read from the Status Register
#define MODE_READ			0x18					// Read from the Mode Register
#define FILTER_READ			0x28					// Read from the Filter Register
#define DATA_READ			0x00					// Read from the Data Register

#define RESET				0xFF					// Resets the chip to default

#define ADC_RES				0x00					// ADC Conversion Result Register
#define ADC_CH				0x01					// ADC Conversion Channel Register
#define CONFIG				0x02					// Configuration Register
#define ALERT				0x03					// Alert Indication Register

#define CH0L_LIM			0x04					// Channel 0 Low Limit Register
#define CH0H_LIM			0x05					// Channel 0 High Limit Register
#define CH0_HYS				0x06					// Channel 0 Hysteresis Register

#define CH1L_LIM			0x07					// Channel 1 Low Limit Register
#define CH1H_LIM			0x08					// Channel 1 High Limit Register
#define CH1_HYS				0x09					// Channel 1 Hysteresis Register

#define CH2L_LIM			0x0A					// Channel 2 Low Limit Register
#define CH2H_LIM			0x0B					// Channel 2 High Limit Register
#define CH2_HYS				0x0C					// Channel 2 Hysteresis Register

#define CH3L_LIM			0x0D					// Channel 3 Low Limit Register
#define CH3H_LIM			0x0E					// Channel 3 High Limit Register
#define CH3_HYS				0x0F					// Channel 3 Hysteresis Register

#define CH4L_LIM			0x10					// Channel 4 Low Limit Register
#define CH4H_LIM			0x11					// Channel 4 High Limit Register
#define CH4_HYS				0x12					// Channel 4 Hysteresis Register

#define CH5L_LIM			0x13					// Channel 5 Low Limit Register
#define CH5H_LIM			0x14					// Channel 5 High Limit Register
#define CH5_HYS				0x15					// Channel 5 Hysteresis Register

#define CH6L_LIM			0x16					// Channel 6 Low Limit Register
#define CH6H_LIM			0x17					// Channel 6 High Limit Register
#define CH6_HYS				0x18					// Channel 6 Hysteresis Register

#define CH7L_LIM			0x19					// Channel 7 Low Limit Register
#define CH7H_LIM			0x1A	   				// Channel 7 High Limit Register
#define CH7_HYS				0x1B					// Channel 7 Hysteresis Register




//Pins
// HSPI Pinouts for 12bit ADC (ADE7901R)

#define HSPI_MISO   12
#define HSPI_MOSI   13
#define HSPI_SCLK   14
#define HSPI_SS     26
#define AD7091R8_SS				26					// AD7091R8 SPI chip select
#define AD7091R8_CONVST			27

class AD7091R8class
{
	public:
			void AD7091R8_SPI_Configuration(void);
			uint16_t spiCommand(uint16_t data);
      uint8_t spiCommand8(uint8_t data);
			void writeAd7091 (uint16_t ui16address, uint16_t ui16value);
			uint16_t readAd7091 (uint16_t ui16address);			
			void Ad7091_soft_reset(void);
      void Adc_Conv();
	private:
};

//extern AD7091R8class AD7091R8;

#endif
