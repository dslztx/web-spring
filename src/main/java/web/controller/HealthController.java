package web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import me.dslztx.assist.util.ObjectAssist;

enum ServiceStatus {
    START, ONLINE, STOP, OFFLINE;
}

@Controller
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    private static volatile ServiceStatus serviceStatus = null;

    @RequestMapping(value = "/health/start", method = RequestMethod.GET)
    @ResponseBody
    public static String serviceStart() {
        logger.info("change the status to [START]");

        serviceStatus = ServiceStatus.START;

        return serviceStatus.toString();
    }

    @RequestMapping(value = "/health/stop", method = RequestMethod.GET)
    @ResponseBody
    public static String serviceStop() {
        logger.info("change the status to [STOP]");

        serviceStatus = ServiceStatus.STOP;

        return serviceStatus.toString();
    }

    @RequestMapping(value = "/health/online", method = RequestMethod.GET)
    @ResponseBody
    public static String serviceOnline() {
        logger.info("change the status to [ONLINE]");

        serviceStatus = ServiceStatus.ONLINE;

        return serviceStatus.toString();
    }

    @RequestMapping(value = "/health/offline", method = RequestMethod.GET)
    @ResponseBody
    public static String serviceOffline() {
        logger.info("change the status to [OFFLINE]");

        serviceStatus = ServiceStatus.OFFLINE;

        return serviceStatus.toString();
    }

    @RequestMapping(value = "/health/status", method = RequestMethod.GET)
    @ResponseBody
    public String status(HttpServletRequest request, HttpServletResponse response) {
        logger.info("get the status...");

        if (serviceStatus == ServiceStatus.ONLINE) {
            response.setStatus(HttpServletResponse.SC_OK);
            return ServiceStatus.ONLINE.toString();
        } else {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ObjectAssist.isNotNull(serviceStatus) ? serviceStatus.toString() : "";
        }
    }
}
