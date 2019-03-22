# ImageSyncUDP

## Overview
An images transfer app. The client phone requests images to the server, once the server phone confirms, the server phone transfers two images which are stored in its sd card, to the cient phone. It uses UDP (User Datagram Protocol).

## Prerequisites
   - Two android phones & usb cords.
   - Two computer with android studio installed.

## Installation & Run

### Installation
  - Download the app from this repository, unzip the file, then use andorid studio open the unzipped app folder. Download the app to both computers.
  - Connect two phones to two computers, one of phone-computer setups is the server and another one is the client.
  - Make sure two phones are connected to the same WiFi Network.
  - Use Android Studio to install the apps to both phones.
  
### Run
  - Once the app is installed on both phones successfully, two buttons (Client, Server) will show on the screen.
  - Quit the app on both phone, and go to setting on both phone, give the app on both phones the permission for storage.
  - Re-run the apps by Android Studio.
  - Tap the Client button on one phone, and Server button on the other. Now can see there's message "Waiting for Request." in the concole of Server side's Android Studio.
  - On the Client phone, tap "FETCH_REQUEST" button, then can see the message "Received Request." on the Server side's Android Studio.
  - Once you can see the message, tap the "If you want to share the photos, press me!" button. And the button will disappear. (Make sure you press the "FETCH_REQUESTION" button of client side first!!!).
  - The client is requesting the image segments while the server is sending them as you can see in the concoles.
  
  ## Development
  
  ### Technologies
   - Network Protocol: [User Datagram Protocol](https://en.wikipedia.org/wiki/User_Datagram_Protocol)
   - Developing: [Android Studio](https://developer.android.com/studio)
    
  ### Current Issues
   - The "If you want to share the photos, press me!" button supposes to be hidden before the client request.
   - The images can be received by the client but it's broken. (it seems to only have half of the image.)
   - Some GUI improvements.
  
  
  
