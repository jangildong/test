#include <String.h>
#include <SoftwareSerial.h>
#include "EBYTE.h"


//로라통신 핀번호 정의
#define PIN_RX 2
#define PIN_TX 3
#define PIN_M0 4
#define PIN_M1 5
#define PIN_AX 6


//보내는 데이터
struct DATA {  
  int Store_number;    // 4byte
  unsigned long Count;      // 4  
  int State1;
  int State2;
  int State3;



};
//채널변수 정의
int Chan;

//보내는 데이터 초기화
DATA MyData;

//로라통신 Eserial통신 초기화
SoftwareSerial ESerial(PIN_RX, PIN_TX);
EBYTE Transceiver(&ESerial, PIN_M0, PIN_M1, PIN_AX);

int ledR1 = 22;
int ledG1 = 24;
int ledB1 = 26;

int ledR2 = 23;
int ledG2 = 25;
int ledB2 = 27;

int ledR3 = 28;
int ledG3 = 30;
int ledB3 = 32;

int trig1 = 34;
int echo1 = 35;

int trig2 = 36;
int echo2 = 37;

int trig3 = 38;
int echo3 = 39;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  ESerial.begin(9600);
  Serial.println("Starting Sender");  //로라

  //로라 송신시작
  Transceiver.init();
//  Transceiver.SetAddressH(0);
//  Transceiver.SetAddressL(0);
  Chan = 30;//로라채널
  Transceiver.SetChannel(Chan);//로라 채널세팅
  Transceiver.PrintParameters();//로라
  pinMode(ledR1, OUTPUT);
  pinMode(ledG1, OUTPUT);
  pinMode(ledB1, OUTPUT);
  pinMode(ledR2, OUTPUT);
  pinMode(ledG2, OUTPUT);
  pinMode(ledB2, OUTPUT);
  pinMode(ledR3, OUTPUT);
  pinMode(ledG3, OUTPUT);
  pinMode(ledB3, OUTPUT);
//  pinMode(ledR4, OUTPUT);
//  pinMode(ledG4, OUTPUT);
//  pinMode(ledB4, OUTPUT);
  pinMode(trig1, OUTPUT);
  pinMode(trig2, OUTPUT);
  pinMode(trig3, OUTPUT);
//  pinMode(trig4, OUTPUT);
  pinMode(echo1, INPUT);
  pinMode(echo2, INPUT);
  pinMode(echo3, INPUT);
//  pinMode(echo4, INPUT);

  MyData.Store_number = 234;
}

void loop() {
  // measure some data and save to the structure
  //매장 번호
  
  //카운트 횟수
  MyData.Count++;   
  
  digitalWrite(trig1, HIGH);
  delay(10);
  digitalWrite(trig1, LOW);
  int duration1 = pulseIn(echo1, HIGH);
  int distance1 = (duration1/2)/29.1;

//1번 초음파 센서길이가 10보다 크거나 같으면 초록불
  if(distance1 >= 10){
    MyData.State1 = 0;
    digitalWrite(ledR1, LOW);
    digitalWrite(ledG1, HIGH);
    digitalWrite(ledB1, LOW);
//10보다 작고 0보다 크면 빨간불
  }else if(0 < distance1 < 10){
    MyData.State1 = 1;
    digitalWrite(ledR1, HIGH);
    digitalWrite(ledG1, LOW);
    digitalWrite(ledB1, LOW);
  }

  digitalWrite(trig2, HIGH);
  delay(10);
  digitalWrite(trig2, LOW);
  int duration2 = pulseIn(echo2, HIGH);
  int distance2 = (duration2/2)/29.1;

  if(distance2 >= 10){
    MyData.State2 = 0;
    digitalWrite(ledR2, LOW);
    digitalWrite(ledG2, HIGH);
    digitalWrite(ledB2, LOW);
  }else if(0 < distance2 < 10){
    MyData.State2 = 1;
    digitalWrite(ledR2, HIGH);
    digitalWrite(ledG2, LOW);
    digitalWrite(ledB2, LOW);
  }

  digitalWrite(trig3, HIGH);
  delay(10);
  digitalWrite(trig3, LOW);
  int duration3 = pulseIn(echo3, HIGH);
  int distance3 = (duration3/2)/29.1;

  if(distance3 >= 10){
    MyData.State3 = 0;
    digitalWrite(ledR3, LOW);
    digitalWrite(ledG3, HIGH);
    digitalWrite(ledB3, LOW);
  }else if(0 < distance3 < 10){
    MyData.State3 = 1;
    digitalWrite(ledR3, HIGH);
    digitalWrite(ledG3, LOW);
    digitalWrite(ledB3, LOW);
  }

//마이데이터에 담아서 송신
  Transceiver.SendStruct(&MyData, sizeof(MyData));
  
  Serial.print("Sending: "); 
  Serial.print(MyData.Count); 
  Serial.print(", "); 
  Serial.print(MyData.State1);
  Serial.print(", ");  
  Serial.print(MyData.State2);
  Serial.print(", "); 
  Serial.println(MyData.State3);
  Serial.print(", "); 
  Serial.println(MyData.Store_number);
  delay(5000);

  
}
