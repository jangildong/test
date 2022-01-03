#include <stdio.h>
#include <math.h>
#include <SoftwareSerial.h>
#include "EBYTE.h"
//로라 핀번호 디파인
#define PIN_RX 2
#define PIN_TX 3
#define PIN_M0 4
#define PIN_M1 5
#define PIN_AX 6


//와이파이 받는(수신)데이터
struct DATA {
  int Store_number;
  unsigned long Count;
  int State1;
  int State2;
  int State3;
};

int Chan;
DATA MyData;
unsigned long Last;

SoftwareSerial ESerial(PIN_RX, PIN_TX);

EBYTE Transceiver(&ESerial, PIN_M0, PIN_M1, PIN_AX);

//wifi모듈--------------------------------------------------------------
void printResponse();
void connectWifi();
void httpclient(String char_input);
void Lora();
// 3.3V 연결
SoftwareSerial ESP8266(10, 11); //ESP8266 : 와이파이모듈
String SSID = "hanul201";   //로라 망 이름
String PASSWORD = "hanul201"; //로라 망 비밀번호
int SENSOR_NUMBER = 111;
String cmd = "";
//String user_id;
int State1;
int State2;
int State3;
int Store_number;
int lastTimeMillis = 0;
int count = 0;

void setup() {


  Serial.begin(9600);
  ESP8266.begin(9600);
  connectWifi();    //와이파이 연결
  lastTimeMillis = millis() / 1000;
  
  ESerial.begin(9600);
  Serial.println("Starting Reader");

  Transceiver.init();

  Chan = 30;
  Transceiver.SetChannel(Chan);

  Transceiver.PrintParameters();
}

void loop() {
  
  if (count > 0) {
    Transceiver.SetMode(MODE_NORMAL);
  }

  if (ESerial.available()) {    

    Transceiver.GetStruct(&MyData, sizeof(MyData));  

    Serial.print("Count: "); Serial.println(MyData.Count);
    Serial.print("State1: "); Serial.println(MyData.State1);
    Serial.print("State2: "); Serial.println(MyData.State2);
    Serial.print("State3: "); Serial.println(MyData.State3);
    Serial.print("Store_number: "); Serial.println(MyData.Store_number);
    
    State1 = MyData.State1;
    State2 = MyData.State2;
    State3 = MyData.State3;
    Store_number = MyData.Store_number;
        
    Last = millis() / 1000;
    Transceiver.SetMode(MODE_PROGRAM);
    count++;
    if (SENSOR_NUMBER == 111) {

      sendToServer();
    }
    delay(1000);    

    Last = millis()/1000;

  }
  else {

    if ((millis()/1000 - Last) > 10) {
      Serial.println("Searching: ");
      Last = millis()/1000;
    }

  }
}

void sendToServer() {
  // n초마다 한번씩
  if (millis() / 1000 - lastTimeMillis > 10) {   //millis : 현재 초

    

    lastTimeMillis = millis() / 1000; // 마지막시간을 다시 셋팅

    
    String str_output = String(State1) + "&State2=" + String(State2) + "&State3=" + String(State3)+ "&Store_number=" + String(Store_number);

    delay(500);
    httpclient(str_output);
    delay(500);

  }  
}

void printResponse() {
  while (ESP8266.available()) {
    Serial.println(ESP8266.readStringUntil('\n'));
    //버퍼에서 읽어들인 '\n'까지의 전체 문자열을 반환합니다.
  }
}

void connectWifi() {

  int cnt = 0;
  while (1) {
    if(cnt > 10){
      String cmd = "AT+RST";  // 초기화
      ESP8266.println(cmd);
      delay(1000);
      printResponse(); 
      cnt = 0;
    }
    
    Serial.println(F("Wifi connecting ..."));
    String join = "AT+CWJAP=\"" + SSID + "\",\"" + PASSWORD + "\""; //AT+CWJAP="SSID","PWD" : AP접근
    Serial.println(F("Connect Wifi..."));
    ESP8266.println(join);
    delay(5000);

    if (ESP8266.find("OK")) {
      Serial.print(F("WIFI connect\n"));
      break;
    } else {
      printResponse();
      Serial.println(F("connect timeout\n"));
    }
    delay(1000);

    cnt++;
  }
}

void httpclient(String char_input) { 

  delay(100);
  Serial.println(F("connect TCP..."));
  String cmd1 = "AT+CIPSTART=\"TCP\",\"192.168.0.22\",8989"; //서버주소로 접속
  ESP8266.println(cmd1);
  delay(500);
  printResponse();  //ESP8266을 읽어서 '\n'까지의 전체 문자열을 반환 (엔터치기전까지의 문자열)

  //    if(ESP8266.find("ERROR")) return;
  Serial.println("Send data...");
  String url = char_input;  //lati의값 + longi의값
  String cmd = "GET /alphacar/ioTCarWash?State1=" + url + " HTTP/1.0\r\n HOST:http://192.168.0.22:8989\r\n\r\n";

  ESP8266.println("AT+CIPSEND=" + String(cmd.length() + 4));
  //delay(1000);
  printResponse();
  Serial.print("AT+CIPSEND=" + String(cmd.length() + 4));
  delay(5000);


  ESP8266.println(cmd);
  Serial.println(cmd);
  delay(1000);
  printResponse();

  if (Serial.find("ERROR")) return;
  ESP8266.println("AT+CIPCLOSE");
  delay(100);

}
