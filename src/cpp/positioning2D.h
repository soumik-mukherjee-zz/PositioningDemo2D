/*
 * Filename: positioning2D.h
 *
 * Copyright (C) 2016 Soumik Mukherjee <miky.mukherjee@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 *  Created on: 22-Jan-2016
 *      Author: Soumik Mukherjee
 */

#include <stdint.h>

#ifndef POSITIONING2D_H_
#define POSITIONING2D_H_

const uint8_t HDR_PING_TX = 0xA0;
const uint8_t HDR_PING_RX_ACK = 0xB0;
const uint8_t HDR_PAIRING_PING_TX = 0xA1;
const uint8_t HDR_PAIRING_PING_RX_ACK = 0xB1;
const uint8_t HDR_COORD_TX = 0xA2;
const uint8_t HDR_COORD_RX_ACK = 0xB2;
const byte MAX_GW_PING_CNT = 4;
typedef struct __attribute__ ((packed)) coords_t
{
  //uint8_t headerId;
  uint32_t x_uS;
  uint32_t y_uS;
} Coordinates;
typedef struct __attribute__ ((packed)) header_t
{
  uint8_t id;
} Header;
#endif /* POSITIONING2D_H_ */
