; $Id: build.nsi,v 1.3 2005/10/20 07:20:44 danreese Exp $
;
; Copyright (c) 2005 Daniel L. Reese
; NSIS documentation can be found at http://nsis.sourceforge.net/Docs/

;--------------------------------
;Include Modern UI

	!include "MUI.nsh"

;--------------------------------
;General

	;Name and file
	Name "NSIS Ant Task"
	OutFile "dist/${name}-${version}.exe"

	;Default installation folder
	InstallDir "$PROGRAMFILES\$(^Name)"

	;Get installation folder from registry if available
	InstallDirRegKey HKCU "Software\$(^Name)" ""

	;Compression Algorithm
	;SetCompressor /SOLID lzma

;--------------------------------
;Interface Settings

	!define MUI_ABORTWARNING

;--------------------------------
;Pages

	!insertmacro MUI_PAGE_LICENSE "LICENSE.txt"
	!insertmacro MUI_PAGE_DIRECTORY
	!insertmacro MUI_PAGE_INSTFILES

	!insertmacro MUI_UNPAGE_CONFIRM
	!insertmacro MUI_UNPAGE_INSTFILES

;--------------------------------
;Languages

	!insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Installer Sections

Section "Install Files" section_1

	;Add files
	SetOutPath "$INSTDIR"
	FILE "LICENSE.txt"
	FILE "dist\${name}-${version}.jar"

	SetOutPath "$INSTDIR\src"
	FILE /r /x CVS src\*.*

	SetOutPath "$INSTDIR\api"
	FILE /r /x CVS dist\api\*.*

	;Store installation folder
	WriteRegStr HKCU "Software\$(^Name)" "" $INSTDIR

	;Create uninstaller
	WriteUninstaller "$INSTDIR\Uninstall.exe"

SectionEnd

;--------------------------------
;Uninstaller Section

Section "Uninstall"

	RMDir /r "$INSTDIR"

	DeleteRegKey /ifempty HKCU "Software\$(^Name)"

SectionEnd
