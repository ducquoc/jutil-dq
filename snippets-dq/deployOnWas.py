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
#import java

print "[DEBUG] WebSphere admin: scripting deployOnWas.py" 

def printHelp():
    print "Usage: "
    print "  deployOnWas.py earOrWarFileWithExtension [appName] [nodeName] [serverName]"
    print "Example wsadmin (Linux/MacOSX `wsadmin.sh`, Windows `wsadmin.bat`): "
    print "  wsadmin.sh -lang jython -user wsadmin -password SECRET -host localhost -f /tmp/deployOnWas.py /tmp/myapp-1.0.ear"
    print "  wsadmin.sh -lang jython -user wsadmin -password SECRET -host localhost -port 8880 -f /tmp/deployOnWas.py build/libs/myapp-1.0.ear"

def getDefaultWasAppName(earFile):
    baseNameFile = os.path.basename(earFile)
    # if baseNameFile.endswith(".war"):
    #     return baseNameFile.replace(".war","_war")
    return os.path.splitext(baseNameFile)[0]


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

    print "[DEBUG] Install/Update file: " + earFile + " appName=" + appName + " nodeName=" + nodeName + " serverName=" + serverName

    #print AdminConfig.list("Server")
    server = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName)
    #print AdminConfig.list("Node")
    node = AdminConfig.getid("/Node:"+nodeName+"/")
    #print AdminConfig.list("Deployment")
    app = AdminConfig.getid("/Deployment:"+appName+"/")
    #print "[DEBUG] " + app + " Node:" + node + " Server:" + server
    if (len(app) > 0):
        print "[DEBUG] already existing app, will update existing application using default merge"
        AdminApplication.updateApplicationUsingDefaultMerge(appName,earFile)
    else:
        print "[DEBUG] not existing app, will install new deployment (not cluster yet)"
        #AdminApplication.installAppWithNodeAndServerOptions(appName,earFile,nodeName,serverName)
        virtualHostName = "default_host"
        if (len(sys.argv) > 4):
            virtualHostName = sys.argv[4]
        bindOpt = ["-usedefaultbindings", "-defaultbinding.virtual.host", virtualHostName ]
        ##"-defaultbinding.datasource.jndi", dsJNDIName, "-defaultbinding.datasource.username", dsUserName,"-defaultbinding.datasource.password", dsPassword, "-defaultbinding.cf.jndi", connFactory, "-defaultbinding.ejbjndi.prefix", EJBprefix
        opts = bindOpt
        opts += ["-appname", appName, "-node", nodeName, "-server", serverName]
        if (earFile.endswith(".war")):
            contextRoot = "/" + appName.replace("-SNAPSHOT","").replace("_war","")
            opts += ["-contextroot", contextRoot]
        AdminApp.install(earFile, opts)

    print "[DEBUG] Saving to master configuration..."
    AdminConfig.save() #if (AdminUtilities._AUTOSAVE_ == "true")
    print "[DEBUG] Trying to start the new deployment (might sync node first): " + appName
    appManager = AdminControl.queryNames('node='+nodeName+',type=ApplicationManager,process='+serverName+',*')
    AdminControl.invoke(appManager, 'startApplication', appName)

