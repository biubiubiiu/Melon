for /l %%x in (1, 1, 100) do (
   adb shell am force-stop app.melon
   echo %%x
   timeout /T 2 /NOBREAK
   adb shell am start-activity -W -n app.melon/.splash.SplashActivity
)
PAUSE