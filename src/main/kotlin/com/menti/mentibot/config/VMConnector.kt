package com.menti.mentibot.config

import com.sun.tools.attach.VirtualMachine
import org.springframework.stereotype.Component
import javax.management.MBeanServerConnection
import javax.management.remote.JMXConnectorFactory
import javax.management.remote.JMXServiceURL

@Component
class VMConnector {

    val connectedVm: VirtualMachine

    val mbeanServerConnection: MBeanServerConnection

    init {
        //jvm
        val vmDescriptor = VirtualMachine.list().filter { it.displayName().contains("mentibot") }[0]
        connectedVm = VirtualMachine.attach(vmDescriptor)

        var connectorAddress =
            connectedVm.agentProperties.getProperty("com.sun.management.jmxremote.localConnectorAddress")
        if (connectorAddress == null) {
            connectedVm.startLocalManagementAgent()
            connectorAddress =
                connectedVm.agentProperties.getProperty("com.sun.management.jmxremote.localConnectorAddress")
        }

        mbeanServerConnection = JMXConnectorFactory.connect(JMXServiceURL(connectorAddress)).mBeanServerConnection
    }
}