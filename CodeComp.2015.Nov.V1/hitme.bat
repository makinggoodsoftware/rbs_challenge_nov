cd /d %~dp0
start cmd.exe /k "%cd%\Server\RBS.CodeComp.Server.Host.exe"
start cmd.exe /k "%cd%\ExecutableClients\C#Client\RBS.CodeComp.Client.exe" Bill test1
