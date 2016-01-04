::this bat will query info of system and copy them to system's clipboard for future use
@echo on
@echo ---------------------------------------------------------
@echo ^|  Automation testing is obtaining system information   ^|
@echo ^|  Please do not close this console window.             ^|
@echo ---------------------------------------------------------
@echo off
setlocal ENABLEDELAYEDEXPANSION
set filePath=%userprofile%\Desktop\tempinfo.xml

echo ^<info^>>%filePath%

@echo on
@echo Querying Host Name
@echo off
echo ^<hostname^>>>%filePath%
hostname>>%filePath%
echo ^<^/hostname^>>>%filePath%

@echo on
@echo Querying Time Zone
@echo off
echo ^<timezone^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic timezone get description^, standardname /value') do (
	set /a row+=1
	set /a y=!row!%%4
	if !y!==3 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<description^>%%j^<^/description^>>>%filePath%) )
	if !y!==0 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<standardname^>%%j^<^/standardname^>>>%filePath%) )
)
echo ^<^/timezone^>>>%filePath%

@echo on
@echo Querying Bios
@echo off
echo ^<bios^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic bios get Name^, Version /value') do (
	set /a row+=1
	set /a y=!row!%%4
	if !y!==3 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<name^>%%j^<^/name^>>>%filePath%) )
	if !y!==0 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<version^>%%j^<^/version^>>>%filePath%) )
)
echo ^<^/bios^>>>%filePath%

@echo on
@echo Querying Physical Memory
@echo off
echo ^<physicalmemory^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic memphysical get Description^, Manufacturer^, MaxCapacity^, MemoryDevices^, Model^, Name^, Status^, Version /value') do (
	set /a row+=1
	set /a y=!row!%%10
 	if "!y!"=="3"  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<description^>%%j^<^/description^>>>%filePath%) )
 	if "!y!"=="4" ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<manufacturer^>%%j^<^/manufacturer^>>>%filePath%) )
 	if "!y!"=="5"  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<maxcapacity^>%%j^<^/maxcapacity^>>>%filePath%) )
 	if "!y!"=="6" ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<memorydevices^>%%j^<^/memorydevices^>>>%filePath%) )
 	if "!y!"=="7"  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<model^>%%j^<^/model^>>>%filePath%) )
 	if "!y!"=="8"  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<name^>%%j^<^/name^>>>%filePath%))
 	if "!y!"=="9"  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<status^>%%j^<^/status^>>>%filePath%) )
 	if "!y!"=="0"  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<version^>%%j^<^/version^>>>%filePath%) )
)
echo ^<^/physicalmemory^>>>%filePath%

@echo on
@echo Querying Memorychips
@echo off
echo ^<memorychips^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic MEMORYCHIP get Capacity^, Description^, Manufacturer^, MemoryType^, Model^, Name^, Speed^, Status^, TotalWidth /value') do (
	set /a row+=1
	set /a y=!row!%%11
	if !y!==3 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varA=^<capacity^>%%j^<^/capacity^>)) 
	if !y!==4 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varB=^<description^>%%j^<^/description^>))
	if !y!==5 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varC=^<manufacturer^>%%j^<^/manufacturer^>) )
	if !y!==6 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varD=^<memorytype^>%%j^<^/memorytype^>) )
	if !y!==7 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varE=^<model^>%%j^<^/model^>) )
	if !y!==8 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varF=^<name^>%%j^<^/name^>) )
	if !y!==9 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varG=^<speed^>%%j^<^/speed^>) )
	if !y!==10 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varH=^<status^>%%j^<^/status^>) )
	if !y!==0 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varI=^<totalwidth^>%%j^<^/totalwidth^>) && echo ^<chip^>>>%filePath%&echo !varA!>>%filePath%&echo !varB!>>%filePath%&echo !varC!>>%filePath%&echo !varD!>>%filePath%&echo !varE!>>%filePath%&echo !varF!>>%filePath%&echo !varG!>>%filePath%&echo !varH!>>%filePath%&echo !varI!>>%filePath%&echo ^<^/chip^>>>%filePath%)
)
echo ^<^/memorychips^>>>%filePath%

@echo on
@echo Querying Virtual Memory
@echo off
echo ^<virtualmemory^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic PAGEFILE get AllocatedBaseSize^, CurrentUsage^, Description^, Name^, PeakUsage^, Status^, TempPageFile /value') do (
	set /a row+=1
	set /a y=!row!%%9
  	if !y!==3 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<allocatedbasesize^>%%j^<^/allocatedbasesize^>>>%filePath%))
	if !y!==4 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<currentusage^>%%j^<^/currentusage^>>>%filePath%) )
	if !y!==5 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<description^>"%%j"^<^/description^>>>%filePath%) )
	if !y!==6 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<name^>%%j^<^/name^>>>%filePath%) )
	if !y!==7 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<peakusage^>%%j^<^/peakusage^>>>%filePath%) )
	if !y!==8 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<status^>%%j^<^/status^>>>%filePath%) )
	if !y!==0 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<temppagefile^>%%j^<^/temppagefile^>>>%filePath%) )
)
echo ^<^/virtualmemory^>>>%filePath%
@echo on
@echo Querying Video Controller
@echo off
echo ^<videocontrollers^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic path Win32_VideoController get AcceleratorCapabilities^,AdapterRAM^,Availability^,DeviceID^,DriverVersion^,InstalledDisplayDrivers^,LastErrorCode^,Name^,Status^,VideoProcessor /value') do (
	set /a row+=1
	set /a y=!row!%%12
	if !y!==3 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varA=^<acceleratorcapabilities^>%%j^<^/acceleratorcapabilities^>)) 
	if !y!==4 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varB=^<adapterram^>%%j^<^/adapterram^>))
	if !y!==5 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varC=^<availability^>%%j^<^/availability^>) )
	if !y!==6 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varD=^<deviceid^>%%j^<^/deviceid^>) )
	if !y!==7 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varE=^<driverversion^>%%j^<^/driverversion^>) )
	if !y!==8 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varF=^<installeddisplaydrivers^>%%j^<^/installeddisplaydrivers^>) )
	if !y!==9 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varG=^<lasterrorcode^>%%j^<^/lasterrorcode^>) )
	if !y!==10 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varH=^<name^>%%j^<^/name^>) )
	if !y!==11 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varI=^<status^>%%j^<^/status^>) )
	if !y!==0 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varJ=^<videoprocessor^>%%j^<^/videoprocessor^>) && echo ^<videocontroller^>>>%filePath%&echo !varA!>>%filePath%&echo !varB!>>%filePath%&echo !varC!>>%filePath%&echo !varD!>>%filePath%&echo !varE!>>%filePath%&echo !varF!>>%filePath%&echo !varG!>>%filePath%&echo !varH!>>%filePath%&echo !varI!>>%filePath%&echo !varJ!>>%filePath%&echo ^<^/videocontroller^>>>%filePath%)
)
echo ^<^/videocontrollers^>>>%filePath%

@echo on
@echo Querying CPU
@echo off
echo ^<cpu^>>>%filePath%
set tee=0
for /f "tokens=1,* delims==" %%a in ('wmic cpu get CpuStatus^, Description^,ExtClock^, L2CacheSize^, Manufacturer^, MaxClockSpeed^, Name^, Status^, Version /value') do (
  set /a tee+=1
  if "!tee!" == "3" echo ^<cpustatus^>%%b^<^/cpustatus^>>>%filePath%
  if "!tee!" == "4" echo ^<description^>%%b^<^/description^>>>%filePath%
  if "!tee!" == "5" echo ^<extclock^>%%b^<^/extclock^>>>%filePath%
  if "!tee!" == "6" echo ^<l2cachesize^>%%b^<^/l2cachesize^>>>%filePath%
  if "!tee!" == "7" echo ^<manufacturer^>%%b^<^/manufacturer^>>>%filePath%
  if "!tee!" == "8" echo ^<maxclockspeed^>%%b^<^/maxclockspeed^>>>%filePath%
  if "!tee!" == "9" echo ^<name^>%%b^<^/name^>>>%filePath%
  if "!tee!" == "10" echo ^<status^>%%b^<^/status^>>>%filePath%
  if "!tee!" == "11" echo ^<version^>%%b^<^/version^>>>%filePath%
)
echo ^<^/cpu^>>>%filePath%

@echo on
@echo Querying Network Configuration
@echo off
echo ^<networkconfigs^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic NicConfig get DefaultIPGateway^, Description^, DNSDomain^, DNSDomainSuffixSearchOrder^, DNSHostName^, IPAddress^, MACAddress /value') do (
	set /a row+=1
	set /a y=!row!%%9
 	if !y!==3 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varA=^<defaultipgateway^>%%j^<^/defaultipgateway^>)) 
  	if !y!==4 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varB=^<description^>%%j^<^/description^>) )
 	if !y!==5 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varC=^<dnsdomain^>%%j^<^/dnsdomain^>))
 	if !y!==6 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varD=^<dnsdomainsuffixsearchorder^>%%j^<^/dnsdomainsuffixsearchorder^>) )
 	if !y!==7 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varE=^<dnshostname^>%%j^<^/dnshostname^>) )
 	if !y!==8 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varF=^<ipaddress^>%%j^<^/ipaddress^>) )
 	if !y!==0 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varG=^<macaddress^>%%j^<^/macaddress^>) &&echo ^<config^>>>%filePath%&echo !varA!>>%filePath%&echo !varB!>>%filePath%&echo !varC!>>%filePath%&echo !varD!>>%filePath%&echo !varE!>>%filePath%&echo !varF!>>%filePath%&echo !varG!>>%filePath%&echo ^<^/config^>>>%filePath% )
)
echo ^<^/networkconfigs^>>>%filePath%

@echo on
@echo Querying OS Info
@echo off
echo ^<os^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic os get Name^, Version^, OSArchitecture /value') do (
	set /a row+=1
	set /a y=!row!%%5
	if !y!==3 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<name^>%%j^<^/name^>>>%filePath%) )
	if !y!==4 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<osarchitecture^>%%j^<^/osarchitecture^>>>%filePath%) )
	if !y!==0 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (echo ^<version^>%%j^<^/version^>>>%filePath%) )
)
echo ^<^/os^>>>%filePath%

@echo on
@echo Querying NTP Server
@echo off
echo ^<ntp^>>>%filePath%
w32tm /query /source>>%filePath%
echo ^<^/ntp^>>>%filePath%

@echo on
@echo Querying Logical Disks
@echo off
echo ^<logicaldisks^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic LOGICALDISK get Availability^,Description^,FileSystem^,FreeSpace^,LastErrorCode^,Name^,Size^,Status^,VolumeName^,VolumeSerialNumber /value') do (
	set /a row+=1
	set /a y=!row!%%12
  if !y!==3 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varA=^<availability^>%%j^<^/availability^>))
  if !y!==4 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varB=^<description^>%%j^<^/description^>) )
  if !y!==5 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varC=^<filesystem^>%%j^<^/filesystem^>) )
  if !y!==6 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varD=^<freespace^>%%j^<^/freespace^>) )
  if !y!==7 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varE=^<lasterrorcode^>%%j^<^/lasterrorcode^>) )
  if !y!==8 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varF=^<name^>%%j^<^/name^>) )
  if !y!==9 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varG=^<size^>%%j^<^/size^>) )
  if !y!==10 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varH=^<status^>%%j^<^/status^>) )
  if !y!==11 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varI=^<volumename^>%%j^<^/volumename^>) )
  if !y!==0 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varJ=^<volumeserialnumber^>%%j^<^/volumeserialnumber^>) &&echo ^<drive^>>>%filePath%&echo !varA!>>%filePath%&echo !varB!>>%filePath%&echo !varC!>>%filePath%&echo !varD!>>%filePath%&echo !varE!>>%filePath%&echo !varF!>>%filePath%&echo !varG!>>%filePath%&echo !varH!>>%filePath%&echo !varI!>>%filePath%&echo !varJ!>>%filePath%&echo ^<^/drive^>>>%filePath% )
)
echo ^<^/logicaldisks^>>>%filePath%

@echo on
@echo Querying Physical Disks
@echo off
echo ^<physicaldisks^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic DISKDRIVE get Description^,Index^,InterfaceType^,LastErrorCode^,Manufacturer^, MediaType^, Model^, Name^, NumberOfMediaSupported^,Partitions^,Size^,Status /value') do (
	set /a row+=1
	set /a y=!row!%%14

 if !y!==3 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varA=^<description^>%%j^<^/description^>))
 if !y!==4 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varB=^<index^>%%j^<^/index^>) )
 if !y!==5 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varC=^<interfacetype^>%%j^<^/interfacetype^>) )
 if !y!==6 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varD=^<lasterrorcode^>%%j^<^/lasterrorcode^>) )
 if !y!==7 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varE=^<manufacturer^>%%j^<^/manufacturer^>) )
 if !y!==8 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varF=^<mediatype^>%%j^<^/mediatype^>) )
 if !y!==9 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varG=^<model^>%%j^<^/model^>) )
 if !y!==10 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varH=^<name^>%%j^<^/name^>) )
 if !y!==11 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varI=^<mumberofmediasupported^>%%j^<^/mumberofmediasupported^>) )
 if !y!==12 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varJ=^<partitions^>%%j^<^/partitions^>) )
 if !y!==13 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varK=^<size^>%%j^<^/size^>) )
 if !y!==0 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varL=^<status^>%%j^<^/status^>) &&echo ^<device^>>>%filePath%&echo !varA!>>%filePath%&echo !varB!>>%filePath%&echo !varC!>>%filePath%&echo !varD!>>%filePath%&echo !varE!>>%filePath%&echo !varF!>>%filePath%&echo !varG!>>%filePath%&echo !varH!>>%filePath%&echo !varI!>>%filePath%&echo !varJ!>>%filePath%&echo !varK!>>%filePath%&echo !varL!>>%filePath%&echo ^<^/device^>>>%filePath% )
)
echo ^<^/physicaldisks^>>>%filePath%

@echo on
@echo Querying Host File
@echo off
::a raw text about hosts file
echo ^<localhost^>>>%filePath%
if exist "%WinDir%\System\drivers\etc\hosts" (type "%WinDir%\System\drivers\etc\hosts">>%filePath%&goto getdone)
if exist "%WinDir%\System32\drivers\etc\hosts" type "%WinDir%\System32\drivers\etc\hosts">>%filePath%
:getdone
echo ^<^/localhost^>>>%filePath%

::a text in xml format
::echo ^<criticalevents^>>>%filePath%
::for /F "delims=" %%i in ('wevtutil el') do (
::	wevtutil qe "%%i" /q:"*[System[(Level=1)]]">>%filePath%
::) 
::echo ^<^/criticalevents^>>>%filePath%

@echo on
@echo Querying Firewall
@echo off
echo ^<firewall^>>>%filePath%
set nodename=na
for /f "tokens=*" %%a in ('netsh advfirewall show allprofiles') do (
	echo "%%a"| find "Domain Profile Settings:" > nul && set nodename=domainprofile&& ( echo ^<!nodename!^>>>%filePath%)
	echo "%%a"| find "Private Profile Settings:" > nul && set nodename=privateprofile&& ( echo ^<!nodename!^>>>%filePath%)
	echo "%%a"| find "Public Profile Settings:" > nul && set nodename=puplicprofile&& ( echo ^<!nodename!^>>>%filePath%)
    echo "%%a"| find "State" > nul && ( for /f "tokens=1,* delims= " %%i in ("%%a") do ( echo ^<state^>%%j^<^/state^>>>%filePath%))
    echo "%%a"| find "Firewall Policy" > nul && ( for /f "tokens=1,2,* delims= " %%i in ("%%a") do ( echo ^<firewallpolicy^>%%k^<^/firewallpolicy^>>>%filePath%)) && ( echo ^<^/!nodename!^>>>%filePath%)
)
echo ^<^/firewall^>>>%filePath%

@echo on
@echo Querying UAC Status
@echo off
::a raw text about UAC
echo ^<uac^>>>%filePath%
set tee=0
for /f "tokens=*" %%a in ('reg query HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Policies\System /v EnableLUA') do (
set /a tee+=1
if "!tee!"=="2" (for /f "tokens=1,2,3,* delims= " %%i in ("%%a") do (echo ^<level^>%%k^<^/level^>>>%filePath%))
)
echo ^<^/uac^>>>%filePath%

::a raw xml from Server Manager Configuration through powsershell
set tempps=%userprofile%\Desktop\smconfigps.ps1
set tempSMCQuery=%userprofile%\Desktop\smconfig.xml
set tempSMCQueryRes=%userprofile%\Desktop\smconfigRes.txt
@echo on
@echo Querying Server Manager Configuration
@echo off
echo Get-WindowsFeature ^| Export-Clixml %tempSMCQuery%>%tempps%
powershell Set-ExecutionPolicy Unrestricted
powershell %tempps%>%tempSMCQueryRes% 2>&1
set isError=0
type %tempSMCQueryRes%|find "FullyQualifiedErrorId">nul &&set isError=1 ||set isError=0
echo ^<servermanagerconfiguration^>>>%filePath%
if %isError%==1  (echo ^<Objs^>>>%filePath% &type %tempSMCQueryRes%>>%filePath% &echo ^<^/Objs^>>>%filePath%) else type %tempSMCQuery%>>%filePath% 
echo ^<^/servermanagerconfiguration^>>>%filePath%
del %tempSMCQuery%
del %tempps%
del %tempSMCQueryRes%

@echo on
@echo Querying Installed Software Products
@echo off
echo ^<products^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic product get Name^,Version^,InstallDate^,InstallState /value') do (
	set /a row+=1
    set /a y=!row!%%6
	if !y!==3 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varA=^<installdate^>%%j^<^/installdate^>))
	if !y!==4 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varB=^<installstate^>%%j^<^/installstate^>) ) 
	if !y!==5 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varC=^<name^>%%j^<^/name^>) )
	if !y!==0 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varD=^<version^>%%j^<^/version^>) &&echo ^<product^>>>%filePath%&echo !varA!>>%filePath%&echo !varB!>>%filePath%&echo !varC!>>%filePath%&echo !varD!>>%filePath%&echo ^<^/product^>>>%filePath% ) 
)
echo ^<^/products^>>>%filePath%

@echo on
@echo Querying System drivers
@echo off
echo ^<systemdrivers^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic SYSDRIVER get displayname^, name^,pathname^,ServiceType^,StartMode^, StartName^,status /value') do (
	set /a row+=1
	set /a y=!row!%%9
	 ::echo %%a>>%filePath%
	if !y!==3  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varA=^<displayname^>%%j^<^/displayname^>))
	if !y!==4  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varB=^<name^>%%j^<^/name^>))
	if !y!==5  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varC=^<pathname^>%%j^<^/pathname^>))
	if !y!==6  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varD=^<servicetype^>%%j^<^/servicetype^>))
	if !y!==7  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varE=^<startmode^>%%j^<^/startmode^>))
	if !y!==8  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varF=^<startname^>%%j^<^/startname^>%))
	if !y!==0  ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varG=^<status^>%%j^<^/status^>) && echo ^<driver^>>>%filePath%&echo !varA!>>%filePath%&echo !varB!>>%filePath%&echo !varC!>>%filePath%&echo !varD!>>%filePath%&echo !varE!>>%filePath%&echo !varF!>>%filePath%&echo !varG!>>%filePath%&echo ^<^/driver^>>>%filePath% )
)
echo ^<^/systemdrivers^>>>%filePath%

@echo on
@echo Querying Installed Windows Updates
@echo off
echo ^<windowsupdates^>>>%filePath%
set row=0
for /f "tokens=*" %%a in ('wmic qfe get Description^, FixComments^, HotFixID^, InstallDate^, InstalledBy^, InstalledOn^, Name^, ServicePackInEffect^, Status /value') do (
	set /a row+=1
	set /a y=!row!%%11
	if !y!==3 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varA=^<description^>%%j^<^/description^>))
	if !y!==4 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varB=^<fixcomments^>%%j^<^/fixcomments^>) )
	if !y!==5 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varC=^<hotfixid^>%%j^<^/hotfixid^>) )
	if !y!==6 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varD=^<installdate^>%%j^<^/installdate^>) )
	if !y!==7 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varE=^<installedby^>%%j^<^/installedby^>) )
	if !y!==8 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varF=^<installedon^>%%j^<^/installedon^>) )
	if !y!==9 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varG=^<name^>%%j^<^/name^>) )
	if !y!==10 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varH=^<servicepackineffect^>%%j^<^/servicepackineffect^>) )
	if !y!==0 ( for /f "tokens=1,* delims==" %%i in ("%%a") do (set varI=^<status^>%%j^<^/status^>) && echo ^<update^>>>%filePath%&echo !varA!>>%filePath%&echo !varB!>>%filePath%&echo !varC!>>%filePath%&echo !varD!>>%filePath%&echo !varE!>>%filePath%&echo !varF!>>%filePath%&echo !varG!>>%filePath%&echo !varH!>>%filePath%&echo !varI!>>%filePath%&echo ^<^/update^>>>%filePath% )
)
echo ^<^/windowsupdates^>>>%filePath%

@echo on
@echo Querying MPIO Configuraion
@echo off
echo ^<MPIOConfiguraion^>>>%filePath%
set tempMPIOConfiguraionLog=%userprofile%\Desktop\MPIOConfiguraion.log
mpclaim -v %tempMPIOConfiguraionLog%
if %ERRORLEVEL% GTR 0 (goto mpioqueryerror)
type %tempMPIOConfiguraionLog%>>%filePath%
:mpioqueryerror
echo ^<^/MPIOConfiguraion^>>>%filePath%

echo ^<^/info^>>>%filePath%
::the suffix @e0 is used to tell out the bat is end
echo @e0>>%filePath%
echo cpc@%filePath%@e0|clip
del %0