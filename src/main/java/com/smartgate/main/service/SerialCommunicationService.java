package com.smartgate.main.service;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.smartgate.main.DateTime;
import com.smartgate.main.entity.Notifications;
import com.smartgate.main.entity.SecurityAlerts;
import com.smartgate.main.repository.NotificationsRepository;
import com.smartgate.main.repository.SecurityAlertsRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class SerialCommunicationService {
    @Autowired
    private StudentsService studentsService; // Unused but included based on your code
    @Autowired
    private SecurityAlertsService securityAlertsService; // Unused but included based on your code
    @Autowired
    private SecurityAlertsRepository securityAlertsRepository;
    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private NotificationsRepository notificationsRepository;

    private SerialPort serialPort;

    public SerialCommunicationService() {
        initializeSerialPort(); // Initialize the serial port
    }

    private void initializeSerialPort() {
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length == 0) {
            System.out.println("No serial ports found.");
            return;
        }

        serialPort = ports[0]; // Select the first available port
        serialPort.setComPortParameters(9600, 8, 1, 0); // Set connection parameters
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (serialPort.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.out.println("Failed to open the port.");
            return; // Exit if the port failed to open
        }
    }

    public void sendCommand(String command) {
        if (serialPort != null && serialPort.isOpen()) {
            try {
                serialPort.getOutputStream().write((command + "\n").getBytes());
                serialPort.getOutputStream().flush();
            } catch (IOException e) {
                System.out.println("Error sending command: " + e.getMessage());
            }
        } else {
            System.out.println("Serial port is not open.");
        }
    }

    @PostConstruct // Automatically called after the object is constructed
    public void initializeSerialListener() {
        if (serialPort != null && serialPort.isOpen()) { // Ensure the port is open
            serialPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; // Listen for data events
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                        byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                        serialPort.readBytes(readBuffer, readBuffer.length);

                        String data = new String(readBuffer, StandardCharsets.UTF_8).trim();
                        System.out.println("Received Data: " + data+"s");

                        // Immediately handle data once received from Arduino
                        handleData(data);
                    }
                }
            });
        } else {
            System.out.println("Serial port is not open. Cannot initialize listener.");
        }
    }

    // Handle the received data from the Arduino
    private void handleData(String data) {
        DateTime dateTime = new DateTime();
        Notifications notifications = notificationsRepository.getByAlertType("securityalerts");

        // Trim the data to remove any leading or trailing whitespace/newline characters
        data = data.trim();

        if ("1".equals(data)) {
            SecurityAlerts alerts = new SecurityAlerts();
            alerts.setAnomaly("trespassing");
            alerts.setDate(dateTime.getCurrentDate());
            alerts.setTime(dateTime.getCurrentTime());
            securityAlertsRepository.save(alerts);

            Long hasNotif = 1L;
            Long counterNotif = notifications.getHasNotif() + hasNotif;
            notifications.setHasNotif(counterNotif);
            notificationsService.updateNotifications(notifications.getId(), notifications);

            System.out.println("Trespassing Alert!!!");
        } else {
            System.out.println("Comparison failed. Result was: '" + data + "'");
        }
    }

}
