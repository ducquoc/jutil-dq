#########
# Simple script to deploy (install/update) application on WebSphere application server, using WSAdmin tool.
# 
# @see: http://webcache.googleusercontent.com/search?q=cache:https://ducquoc.wordpress.com/2011/02/20/websphere-thin-client/
# @see: https://www.ibm.com/support/knowledgecenter/en/SS7JFU_8.5.5/com.ibm.websphere.express.doc/ae/rxml_commandline.html
# @see: https://www.ibm.com/support/knowledgecenter/en/SSAW57_8.5.5/com.ibm.websphere.nd.multiplatform.doc/ae/cxml_jython.html
# @see: https://www.ibm.com/support/knowledgecenter/en/SSAW57_8.5.5/com.ibm.websphere.nd.multiplatform.doc/ae/txml_7libserver.html
#########
import sys
import os
import datetime
#import java

print "[DEBUG] " + datetime.datetime.now().strftime("%m/%d/%Y, %H:%M:%S") + " WebSphere admin: scripting deployOnWas.py" 
rollingPrefix = "backup" #"rolling"

def printHelp():
    print "Usage: "
    print "  deployOnWas.py earOrWarFileWithExtension [appName] [nodeName] [serverName]"
    print "Example wsadmin (Linux/MacOSX `wsadmin.sh`, Windows `wsadmin.bat`): "
    print "  wsadmin.sh -lang jython -user wsadmin -password SECRET -host localhost -f /tmp/deployOnWas.py /tmp/myapp-1.0.ear"
    print "  wsadmin.sh -lang jython -user wsadmin -password SECRET -host localhost -port 8880 -f /tmp/deployOnWas.py build/libs/myapp-1.0.ear"
    print " rolling-update mode support: the 1st param (appName) should have prefix: " + rollingPrefix
    print "  wsadmin.bat -lang jython -user wsadmin -password SECRET -host 172.17.0.3 -f C:\\tmp\\deployOnWas.py C:\\tmp\\myapp.ear "+rollingPrefix+"-myapp"

def getDefaultWasAppName(earFile):
    baseNameFile = os.path.basename(earFile)
    # if baseNameFile.endswith(".war"):
    #     return baseNameFile.replace(".war","_war")
    return os.path.splitext(baseNameFile)[0]

def isRunningWasApp(appName):
    runningStatus = AdminControl.completeObjectName("type=Application,name="+appName+",*")
    return len(runningStatus) > 0

def isInstalledWasApp(appName):
    appDeployment = AdminConfig.getid("/Deployment:"+appName+"/")
    return len(appDeployment) > 0

def installAppplicationWithOptions(earFile, options):
    #AdminApplication.installAppWithNodeAndServerOptions(appName,earFile,nodeName,serverName)
    AdminApp.install(earFile, options)

def updateApplicationReplaceMergeEntire(appName, earFile, options):
    #AdminApplication.installAppWithNodeAndServerOptions(appName,earFile,nodeName,serverName)
    #AdminApplication.updateApplicationUsingDefaultMerge(appName,earFile)
    AdminApp.update(appName, "app", options + ["-operation","update", "-contents",earFile])

def trySaveToMasterConfiguration():
    AdminConfig.save()

def tryStartApplication(appName, appManager):
    #if (appManager is not None and len(appManager) > 0):
    AdminControl.invoke(appManager, 'startApplication', appName)

def tryStopApplication(appName, appManager):
    #if (appManager is not None and len(appManager) > 0):
    AdminControl.invoke(appManager, 'stopApplication', appName)

#__main__

if (len(sys.argv) < 1):
    printHelp()
    sys.exit()
else:
    earFile = sys.argv[0]
    appName = getDefaultWasAppName(earFile)
    if (len(sys.argv) > 1):
        appName = sys.argv[1]
    nodeName = "DefaultNode01"
    if (len(sys.argv) > 2):
        nodeName = sys.argv[2]
    serverName = "server1"
    if (len(sys.argv) > 3):
        serverName = sys.argv[3]
    virtualHostName = "default_host"
    if (len(sys.argv) > 4):
        virtualHostName = sys.argv[4]

    #cellName = nodeName + "Cell01" #jython 2.1 not support ternary tail: if ... else
    #rollingUpdateMode: for faster "rollback" and shorter "down time"
    shortAppName = appName
    if (appName.startswith(rollingPrefix)):
        shortAppName = appName.replace(rollingPrefix+"-","").replace(rollingPrefix+"_","").replace(rollingPrefix,"")

    appNameToStart = appName
    appNameToStop = shortAppName
    if (appName.startswith(rollingPrefix)):
        appRunning = isRunningWasApp(appName)
        if (appRunning): 
            shortAppRunning = isRunningWasApp(shortAppName)
            if (not shortAppRunning):
                appNameToStart = shortAppName
                appNameToStop = appName

    print "[DEBUG] Install/Update file: " + earFile + " appName=" + appNameToStart + " nodeName=" + nodeName + " serverName=" + serverName

    #print AdminConfig.list("Server")
    server = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName)
    #print AdminConfig.list("Node")
    node = AdminConfig.getid("/Node:"+nodeName+"/")
    #print AdminConfig.list("Deployment")
    app = AdminConfig.getid("/Deployment:"+appNameToStart+"/")
    #print "[DEBUG] " + app + " Node:" + node + " Server:" + server
    bindOpt = ["-usedefaultbindings", "-defaultbinding.virtual.host", virtualHostName ]
    ##"-defaultbinding.datasource.jndi", dsJNDIName, "-defaultbinding.datasource.username", dsUserName,"-defaultbinding.datasource.password", dsPassword, "-defaultbinding.cf.jndi", connFactory, "-defaultbinding.ejbjndi.prefix", EJBprefix
    opts = bindOpt
    if (len(app) > 0):
        print "[DEBUG] already existing app, will update existing application using default merge"
        #vHostBindings = AdminApp.view(appNameToStart, '-MapWebModToVH')
        updateApplicationReplaceMergeEntire(appNameToStart, earFile, opts)
    else:
        print "[DEBUG] not existing app, will install new deployment (not cluster yet)"
        opts += ["-appname", appNameToStart, "-node", nodeName, "-server", serverName]
        if (earFile.endswith(".war")):
            contextRoot = "/" + appName.replace("-SNAPSHOT","").replace("_war","")
            opts += ["-contextroot", contextRoot]
        installAppplicationWithOptions(earFile, opts)

    print "[DEBUG] Saving to master configuration..."
    trySaveToMasterConfiguration() #if (AdminUtilities._AUTOSAVE_ == "true")
    print "[DEBUG] " + datetime.datetime.now().strftime("%m/%d/%Y, %H:%M:%S") + " wsadmin saved to master config!"

    appManager = AdminControl.queryNames('node='+nodeName+',type=ApplicationManager,process='+serverName+',*')
    if (appName.startswith(rollingPrefix) and isRunningWasApp(appNameToStop)):
        print "[DEBUG] Trying to stop the old deployment (for rolling update): " + appNameToStop
        tryStopApplication(appNameToStop, appManager)

    if (not isRunningWasApp(appNameToStart)):
        print "[DEBUG] Trying to start the new deployment (might sync node first): " + appNameToStart
        tryStartApplication(appNameToStart, appManager)
    if (isRunningWasApp(appNameToStart)): print "[DEBUG] started: " + appNameToStart
