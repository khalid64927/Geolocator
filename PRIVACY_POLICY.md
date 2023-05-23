## GeoFence Demo: Privacy policy

Welcome to the Geo Fence Demo app for Android!

This is an open source Android app developed by Mohammed Khalid Hamid . 
The source code is available on GitHub under the MIT license; the app is also available on 
Google Play.

As an avid Android user myself, I take privacy very seriously.
I know how irritating it is when apps collect your data without your knowledge.

I hereby state, to the best of my knowledge and belief, that I have not programmed this app to 
collect any personally identifiable information. All data (app preferences (like theme, etc.) and 
alarms) created by the you (the user) is stored on your device only, and can be simply erased by 
clearing the app's data or uninstalling it.

### Explanation of permissions requested in the app

The list of permissions required by the app can be found in the `AndroidManifest.xml` file:

https://github.com/khalid64927/Geolocator/blob/master/app/src/main/AndroidManifest.xml#L6-L9

https://github.com/khalid64927/Geolocator/blob/master/Geolocator/src/main/AndroidManifest.xml#L5-L9

<br/>

|                   Permission                    | Why it is required                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
|:-----------------------------------------------:|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|     `android.permission.POST_NOTIFICATIONS`     | This is required to show local notification when Geo fence is entered or exited.                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| `android.permission.ACCESS_BACKGROUND_LOCATION` | Required to monitor the user location in background which user has to grant at runtime and this help to trigger geo fence .                                                                                                                                                                                                                                                                                                                                                                                           |
|         `android.permission.WAKE_LOCK`          | Required to show local notification about geo fence.                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| `android.permission.ACCESS_FINE_LOCATION` and `android.permission.ACCESS_COARSE_LOCATION`  | Enables the app to get precise location from wifi or GPS hardware which user also has to grant at runtime.                                                                                                                                                                                                                                                                                                                                                                                                            |
|   `android.permission.RECEIVE_BOOT_COMPLETED`   | When your device restarts, all alarms set in the system are lost. This permission enables the app to receive a message from the system once the system has rebooted and you have unlocked your device the first time. When this message is received, the app creates a service to set all the active alarms in the system.                                                                                                                                                                                            |

 <hr style="border:1px solid gray">

If you find any security vulnerability that has been inadvertently caused by me, or have any question 
regarding how the app protectes your privacy, please send me an email or post a discussion on GitHub, 
and I will surely try to fix it/help you.

Yours sincerely,  
Mohammed Khalid Hamid.  
Singapore.  
contactgp3456@gmail.com