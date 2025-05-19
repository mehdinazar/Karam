@echo off
setlocal

:: تنظیمات
set AUTH_TOKEN=lcwAWyBKCjRdwkMsHmGPioTTGJuk6trvZWO80RzmEv8nLZs8Ks5ltDV4ASR0Lmh447MINoQeamcniN4fpXokgw
set APP_ID=7dd13f71-4d7e-48c4-86c0-09f56230e7d0
set FILE_NAME=app-release.apk
set FILE_PATH=app/build/outputs/apk/release/app-release.apk

:: دریافت سایز فایل
for %%A in ("%FILE_PATH%") do set FILE_SIZE=%%~zA

:: درخواست اول - دریافت URL آپلود
curl -H "Authorization: %AUTH_TOKEN%" "https://api.bitrise.io/release-management/v1/connected-apps/%APP_ID%/installable-artifacts/new/upload-url?file_name=%FILE_NAME%&file_size_bytes=%FILE_SIZE%" > upload_url.json

:: خواندن URL از فایل JSON
for /f "tokens=* usebackq" %%a in (`type upload_url.json ^| findstr /C:"upload_url"`) do set UPLOAD_URL=%%a
set UPLOAD_URL=%UPLOAD_URL:"upload_url": "=%
set UPLOAD_URL=%UPLOAD_URL:",=%
set UPLOAD_URL=%UPLOAD_URL:"=%

:: آپلود فایل
curl -X "PUT" -H "Content-Type: application/vnd.android.package-archive" -H "X-Goog-Content-Length-Range: 0,%FILE_SIZE%" --upload-file "%FILE_PATH%" "%UPLOAD_URL%"

:: بررسی وضعیت
curl -H "Authorization: %AUTH_TOKEN%" "https://api.bitrise.io/release-management/v1/connected-apps/%APP_ID%/installable-artifacts/new/status"

endlocal 